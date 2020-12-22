package com.tokopedia.topads.dashboard.view.fragment.insightbottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.GroupListDataItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        val childView = View.inflate(context, R.layout.topads_choose_group_insight_bottomsheet, null)
        setChild(childView)
        showCloseIcon = false
        setTitle(getString(R.string.topads_headline_recom_grp_bs_title))
        adapter = TopadsRecomGroupBsAdapter(::onItemClicked)
    }

    private fun onItemClicked(pos: Int) {

    }

    fun initInjector() {
        DaggerTopAdsDashboardComponent.builder().baseAppComponent(
                (activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        bottomSheetBehaviorKnob(view, true)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
        adapter.setShimmer()
        Utils.setSearchListener(search, context, view, ::getData)
        submit_butt?.setOnClickListener {
            dismiss()
            onItemClick?.invoke(adapter.getCheckedPosition())
        }
        setGroupName()
        contentSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                group_name_input.visibility = View.GONE
                search.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            } else {
                group_name_input.visibility = View.VISIBLE
                search.visibility = View.GONE
                recyclerView.visibility = View.GONE
            }
        }
    }

    private fun setGroupName() {
        group_name_input?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                s?.let {
                    coroutineScope.launch {
                        delay(DEBOUNCE_CONST)
                        val text = s.toString().trim()
                        topAdsDashboardPresenter.validateGroup(text, ::onSuccessGroupName)

                    }
                }

            }

        })
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
        adapter.setItems(groupList)
    }

    fun show(
            fragmentManager: FragmentManager
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_TAG = "insight_bottom_sheet"
        fun getInstance() = TopAdsRecomGroupBottomSheet()
    }

}