package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.insight.TopadsRecomGroupBsAdapter
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_choose_group_insight_bottomsheet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEBOUNCE_CONST: Long = 200

class TopAdsRecomGroupBottomSheet : BottomSheetUnify() {
    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter
    private lateinit var adapter: TopadsRecomGroupBsAdapter
    var onItemClick: ((groupId: Int) -> Unit)? = null
    var onNewGroup: ((name: String) -> Unit)? = null
    var groupList: List<GroupListDataItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        val childView = View.inflate(context, R.layout.topads_choose_group_insight_bottomsheet, null)
        setChild(childView)
        setSheetValues()
        adapter = TopadsRecomGroupBsAdapter(::onGroupSelect)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun initialState() {
        group_name_input?.textFieldInput?.text?.clear()
        setEmptyNameError()
        if (groupList.isEmpty()) {
            group_name_input.visible()
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
        submit_butt?.isEnabled = true
    }

    fun initInjector() {
        DaggerTopAdsDashboardComponent.builder().baseAppComponent(
                (activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
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
            group_name_input?.textFieldInput?.text?.clear()
            setEmptyNameError()
            if (!isChecked) {
                submit_butt?.isEnabled = adapter.isChecked() != -1
                group_name_input.gone()
                search.visible()
                recyclerView.visible()
            } else {
                submit_butt?.isEnabled = false
                group_name_input?.requestFocus()
                group_name_input.visible()
                search.gone()
                recyclerView.gone()
            }
        }
    }

    private fun onButtonSubmit() {
        submit_butt?.setOnClickListener {
            submit_butt?.isLoading = true
            if (contentSwitch.isChecked || !contentSwitch.isVisible)
                onNewGroup?.invoke(group_name_input?.textFieldInput?.text?.toString() ?: ""
                ) else
                onItemClick?.invoke(adapter.getCheckedPosition()
                )
        }
    }

    private fun setAdapter() {
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.setShimmer()
    }

    private fun setGroupName() {
        group_name_input?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                val text = s.toString()
                if(text.isEmpty()){
                    setEmptyNameError()
                 }else {
                    s?.let {
                        coroutineScope.launch {
                            delay(DEBOUNCE_CONST)
                            topAdsDashboardPresenter.validateGroup(text, ::onSuccessGroupName)
                        }
                    }
                }
            }
        })
    }

    fun setEmptyNameError(){
        group_name_input?.setError(true)
        submit_butt?.isEnabled = false
        group_name_input?.setMessage(getString(R.string.topads_dash_name_empty_error))
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName) {
        if (data.errors.isEmpty()) {
            group_name_input?.setError(false)
            submit_butt?.isEnabled = true
            group_name_input?.setMessage("")
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun onErrorGroupName(error: String) {
        group_name_input?.setError(true)
        submit_butt?.isEnabled = false
        group_name_input?.setMessage(error)
    }

    private fun getData() {
        topAdsDashboardPresenter.getGroupList(search.searchBarTextField.text.toString(), ::onSuccessGroupList)
    }

    private fun onSuccessGroupList(groupList: List<GroupListDataItem>) {
        if (groupList.isEmpty()) {
            emptyText.visible()
            submit_butt?.isEnabled = false
        } else {
            submit_butt?.isEnabled = true
            emptyText.gone()
            val groupIds: List<String> = groupList.map {
                it.groupId.toString()
            }
            topAdsDashboardPresenter.getCountProductKeyword(resources, groupIds, ::onSuccessCount)
        }
        adapter.setItems(groupList)
    }

    private fun onSuccessCount(list: List<CountDataItem>) {
        adapter.setKeyProductCount(list)
    }

    fun show(
            fragmentManager: FragmentManager, groupList: List<GroupListDataItem>
    ) {
        this.groupList = groupList
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_TAG = "insight_bottom_sheet"
        fun getInstance() = TopAdsRecomGroupBottomSheet()
    }

}