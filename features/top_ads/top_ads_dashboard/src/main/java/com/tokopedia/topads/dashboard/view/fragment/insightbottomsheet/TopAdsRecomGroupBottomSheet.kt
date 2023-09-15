package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsRecomGroupBsAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.*
import kotlinx.coroutines.*
import javax.inject.Inject
import com.tokopedia.unifyprinciples.Typography

const val DEBOUNCE_CONST: Long = 200

class TopAdsRecomGroupBottomSheet : BottomSheetUnify() {

    private lateinit var contentSwitch: ContentSwitcherUnify
    private lateinit var groupNameInput: TextFieldUnify
    private lateinit var search: SearchBarUnify
    private lateinit var emptyText: Typography
    private lateinit var recyclerView: RecyclerView
    private lateinit var submitButton: UnifyButton

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    private lateinit var adapter: TopadsRecomGroupBsAdapter
    var onItemClick: ((groupIdAndType: Pair<String, String>) -> Unit)? = null
    var onNewGroup: ((name: String) -> Unit)? = null
    private var groupList: List<GroupListDataItem> = listOf()
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        val childView =
            View.inflate(context, R.layout.topads_choose_group_insight_bottomsheet, null)
        setChild(childView)
        initView(childView)
        setSheetValues()
        adapter = TopadsRecomGroupBsAdapter(::onGroupSelect)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun initView(view: View) {
        contentSwitch = view.findViewById(R.id.contentSwitch)
        groupNameInput = view.findViewById(R.id.group_name_input)
        search = view.findViewById(R.id.search)
        emptyText = view.findViewById(R.id.emptyText)
        recyclerView = view.findViewById(R.id.recyclerView)
        submitButton = view.findViewById(R.id.submit_butt)
    }

    private fun initialState() {
        groupNameInput?.textFieldInput?.text?.clear()
        setEmptyNameError()
        if (groupList.isEmpty()) {
            groupNameInput.visible()
            contentSwitch?.gone()
            search.gone()
            recyclerView?.gone()
        } else {
            adapter.setItems(groupList)
            val groupIds: List<String> = groupList.map {
                it.groupId
            }
            topAdsDashboardPresenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
    }

    private fun setSheetValues() {
        showCloseIcon = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.topads_headline_recom_grp_bs_title))
    }

    private fun onGroupSelect() {
        submitButton?.isEnabled = true
    }

    fun initInjector() {
        DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (activity?.application as BaseMainApplication).baseAppComponent
        ).build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
        setAdapter()
        initialState()
        Utils.setSearchListener(search, context, view, ::getData)
        setGroupName()
        onButtonSubmit()
        contentSwitch?.setOnCheckedChangeListener { _, isChecked ->
            groupNameInput?.textFieldInput?.text?.clear()
            setEmptyNameError()
            if (!isChecked) {
                submitButton?.isEnabled = adapter.isChecked() != -1
                groupNameInput.gone()
                search.visible()
                recyclerView.visible()
            } else {
                submitButton?.isEnabled = false
                groupNameInput?.requestFocus()
                groupNameInput.visible()
                search.gone()
                recyclerView.gone()
            }
        }
    }

    private fun onButtonSubmit() {
        submitButton?.setOnClickListener {
            submitButton?.isLoading = true
            if (contentSwitch.isChecked || !contentSwitch.isVisible)
                onNewGroup?.invoke(
                    groupNameInput?.textFieldInput?.text?.toString() ?: ""
                ) else
                onItemClick?.invoke(
                    adapter.getCheckedPosition()
                )
        }
    }

    private fun setAdapter() {
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.setShimmer()
    }

    private fun setGroupName() {
        groupNameInput?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = s.toString()
                if (text.isEmpty()) {
                    setEmptyNameError()
                } else {
                    s?.let {
                        coroutineScope.launch {
                            delay(DEBOUNCE_CONST)
                            if (activity != null && isAdded) {
                                topAdsDashboardPresenter.validateGroup(text, ::onSuccessGroupName)
                            }
                        }
                    }
                }
            }
        })
    }

    fun setEmptyNameError() {
        groupNameInput?.setError(true)
        submitButton?.isEnabled = false
        groupNameInput?.setMessage(getString(R.string.topads_dash_name_empty_error))
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        if (data.errors.isEmpty()) {
            groupNameInput?.setError(false)
            submitButton?.isEnabled = true
            groupNameInput?.setMessage("")
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun onErrorGroupName(error: String) {
        groupNameInput?.setError(true)
        submitButton?.isEnabled = false
        groupNameInput?.setMessage(error)
    }

    private fun getData() {
        topAdsDashboardPresenter.getGroupList(
            search.searchBarTextField.text.toString(),
            ::onSuccessGroupList
        )
    }

    private fun onSuccessGroupList(groupList: List<GroupListDataItem>) {
        if (groupList.isEmpty()) {
            emptyText.visible()
            submitButton?.isEnabled = false
        } else {
            submitButton?.isEnabled = true
            emptyText.gone()
            val groupIds: List<String> = groupList.map {
                it.groupId
            }
            topAdsDashboardPresenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
        adapter.setItems(groupList)
    }

    private fun onSuccessCount(list: List<CountDataItem>) {
        adapter.setKeyProductCount(list)
    }

    fun show(
        fragmentManager: FragmentManager, groupList: List<GroupListDataItem>,
    ) {
        this.groupList = groupList
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_TAG = "insight_bottom_sheet"
        fun getInstance() = TopAdsRecomGroupBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

}
