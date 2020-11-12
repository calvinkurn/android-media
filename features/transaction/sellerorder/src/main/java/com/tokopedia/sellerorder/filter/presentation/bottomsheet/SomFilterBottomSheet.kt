package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.Utils.copyInt
import com.tokopedia.sellerorder.common.util.Utils.copyListParcelable
import com.tokopedia.sellerorder.filter.di.DaggerSomFilterComponent
import com.tokopedia.sellerorder.filter.di.SomFilterComponent
import com.tokopedia.sellerorder.filter.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.filter.presentation.activity.SomSubFilterActivity.Companion.KEY_SOM_ORDER_PARAM_CACHE
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapterTypeFactory
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterCancelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SomFilterBottomSheet(private val mActivity: FragmentActivity?) : BottomSheetUnify(),
        SomFilterListener, SomFilterDateBottomSheet.CalenderListener,
        HasComponent<SomFilterComponent> {

    @Inject
    lateinit var somFilterViewModel: SomFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var rvSomFilter: RecyclerView? = null
    private var btnShowOrder: UnifyButton? = null
    private var somFilterAdapter: SomFilterAdapter? = null
    private var orderStatus: String = ""
    private var filterDate: String = ""
    private var isApplyFilter: Boolean = false
    private var statusList = listOf<Int>()

    private var somFilterFinishListener: SomFilterFinishListener? = null
    private var somListOrderParam: SomListGetOrderListParam? = null
    private var somFilterUiModelListCopy = listOf<SomFilterUiModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        val somFilterUiModelList = arguments?.getParcelableArrayList<SomFilterUiModel>(KEY_SOM_FILTER_LIST)
                ?: arrayListOf()
        orderStatus = arguments?.getString(KEY_ORDER_STATUS).orEmpty()
        filterDate = arguments?.getString(KEY_FILTER_DATE).orEmpty()
        somFilterViewModel.setIsRequestCancelFilterApplied(arguments?.getBoolean(KEY_IS_REQUEST_CANCEL_FILTER_APPLIED, false) ?: false)
        somFilterUiModelListCopy = somFilterUiModelList.copyListParcelable()
        val statusListFilter = arguments?.getIntegerArrayList(KEY_ORDER_STATUS_ID_LIST)?.toList()
        statusList = statusListFilter?.copyInt() ?: listOf()
        somListOrderParam?.statusList = statusListFilter ?: listOf()
        somFilterViewModel.setSomFilterUiModel(somFilterUiModelList)
        somFilterViewModel.setSomListGetOrderListParam(somListOrderParam
                ?: SomListGetOrderListParam())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadSomFilterData()
        clickShowOrder()
        observeSomFilter()
        bottomSheetReset()
        adjustBottomSheetPadding()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(!isApplyFilter) {
            val cancelWrapper = SomFilterCancelWrapper(orderStatus, statusList, somFilterUiModelListCopy, filterDate)
            somFilterFinishListener?.onClickOverlayBottomSheet(cancelWrapper)
        }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottomsheet_som_filter_list, container, false)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        btnShowOrder = view.findViewById(R.id.btnShowOrder)
        setChild(view)
        isFullpage = true
        isDragable = true
        setTitle(TITLE_FILTER)
        showKnob = true
        showCloseIcon = false
        isHideable = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    override fun onDateClicked(position: Int) {
        clickDateFilter()
    }

    override fun onFilterChipsClicked(somFilterData: SomFilterChipsUiModel, idFilter: String,
                                      position: Int, chipType: String, orderStatus: String) {
        when (idFilter) {
            FILTER_SORT, FILTER_LABEL, FILTER_DEADLINE, FILTER_STATUS_ORDER -> {
                somFilterViewModel.updateFilterSelected(idFilter, position, chipType, filterDate)
                somFilterViewModel.updateParamSom(idFilter)
            }
            FILTER_TYPE_ORDER, FILTER_COURIER -> {
                somFilterViewModel.updateFilterManySelected(idFilter, chipType, position, filterDate)
                somFilterViewModel.updateParamSom(idFilter)
            }
        }
    }

    override fun onSeeAllFilter(somFilterData: SomFilterUiModel, position: Int, idFilter: String) {
        val somFilterChipsList = somFilterViewModel.getSomFilterUiModel()
                .find { it.nameFilter == idFilter }?.somFilterData ?: listOf()
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_SOM_LIST_GET_ORDER_PARAM, somFilterViewModel.getSomListGetOrderListParam())
        val intentSomSubFilter = SomSubFilterActivity.newInstance(this.activity,
                filterDate,
                idFilter,
                somFilterChipsList,
                cacheManager?.id ?: ""
        )
        startActivityForResult(intentSomSubFilter, REQUEST_CODE_FILTER_SEE_ALL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, data?.getStringExtra(KEY_CACHE_MANAGER_ID)) }
        when (requestCode) {
            REQUEST_CODE_FILTER_SEE_ALL -> {
                if (resultCode == RESULT_CODE_FILTER_SEE_ALL) {
                    this.somListOrderParam = cacheManager?.get(KEY_SOM_ORDER_PARAM_CACHE, SomListGetOrderListParam::class.java)
                    somFilterViewModel.setSomListGetOrderListParam(somListOrderParam
                            ?: SomListGetOrderListParam())
                    val idFilter = data?.getStringExtra(SomSubFilterActivity.KEY_ID_FILTER) ?: ""
                    val filterDate = data?.getStringExtra(SomSubFilterActivity.KEY_FILTER_DATE)
                            ?: ""
                    val somSubFilterList =
                            data?.getParcelableArrayListExtra<SomFilterChipsUiModel>(SomSubFilterActivity.KEY_SOM_LIST_FILTER_CHIPS)?.toList()
                                    ?: listOf()
                    somFilterViewModel.updateSomFilterSeeAll(idFilter, somSubFilterList, filterDate)
                }
            }
        }
    }

    override fun onBtnSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        somListOrderParam = somFilterViewModel.getSomListGetOrderListParam()
        somListOrderParam?.startDate = startDate.first
        somListOrderParam?.endDate = endDate.first
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        filterDate = date
        somFilterAdapter?.updateDateFilterText(date)
        showHideBottomSheetReset()
    }

    override fun getComponent(): SomFilterComponent? {
        return activity?.run {
            DaggerSomFilterComponent
                    .builder()
                    .somComponent(SomComponentInstance.getSomComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        removeObservers(somFilterViewModel.filterResult)
        removeObservers(somFilterViewModel.updateFilterSelected)
        removeObservers(somFilterViewModel.somFilterOrderListParam)
        super.onDestroy()
    }

    private fun adjustBottomSheetPadding() {
        bottomSheetWrapper.setPadding(0, bottomSheetWrapper.paddingTop, 0, bottomSheetWrapper.paddingBottom)
        (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(16.toPx(), bottomSheetHeader.top,
                16.toPx(), 16.toPx())
    }

    fun show() {
        isApplyFilter = false
        mActivity?.supportFragmentManager?.let {
            show(it, SOM_FILTER_BOTTOM_SHEET_TAG)
        }
    }

    private fun loadSomFilterData() {
        somFilterAdapter?.showLoading()
        btnShowOrder?.hide()
        somFilterViewModel.getSomFilterData(orderStatus, filterDate)
    }

    private fun finishSomFilterData() {
        somFilterAdapter?.hideLoading()
        btnShowOrder?.show()
    }

    private fun initRecyclerView() {
        val somFilterLayoutManager = LinearLayoutManager(requireContext())
        val somFilterAdapterTypeFactory = SomFilterAdapterTypeFactory(this)
        somFilterAdapter = SomFilterAdapter(somFilterAdapterTypeFactory)
        rvSomFilter?.apply {
            layoutManager = somFilterLayoutManager
            adapter = somFilterAdapter
        }
    }

    private fun initInject() {
        component?.inject(this)
    }

    private fun clickShowOrder() {
        btnShowOrder?.setOnClickListener {
            isApplyFilter = true
            SomAnalytics.eventClickTerapkanOnFilterPage(getFilterTextReset())
            somListOrderParam = somFilterViewModel.getSomListGetOrderListParam()
            val copySomFilterUiModel = somFilterViewModel.getSomFilterUiModel()
            somListOrderParam?.let {
                somFilterFinishListener?.onClickShowOrderFilter(it, copySomFilterUiModel, FILTER_STATUS_ORDER, filterDate, somFilterViewModel.isRequestCancelFilterApplied())
            }
            dismissAllowingStateLoss()
        }
    }

    private fun getFilterTextReset(): String {
        val filterTextList = mutableSetOf<String>()
        somFilterViewModel.getSomFilterUiModel().forEach {
            it.somFilterData.filter { somFilter -> somFilter.isSelected }.forEach { somFilterChips ->
                filterTextList.add(somFilterChips.key)
                if (somFilterChips.childStatus.isNotEmpty()) {
                    somFilterChips.childStatus.filter { somFilterChips.isSelected }.forEach { childStatus ->
                        filterTextList.add(childStatus.key)
                    }
                }
            }
        }
        return filterTextList.joinToString(separator = ",")
    }

    private fun clickDateFilter() {
        val somFilterDateBottomSheet = SomFilterDateBottomSheet.newInstance()
        somFilterDateBottomSheet.setCalendarListener(this)
        somFilterDateBottomSheet.setFragmentManager(childFragmentManager)
        if(!somFilterDateBottomSheet.isAdded) {
            somFilterDateBottomSheet.show()
        }
    }

    private fun observeSomFilter() {
        observe(somFilterViewModel.filterResult) {
            finishSomFilterData()
            when (it) {
                is Success -> {
                    if (it.data.isEmpty()) {
                        somFilterAdapter?.setEmptyState(EmptyModel())
                    } else {
                        somFilterAdapter?.updateData(it.data)
                        showHideBottomSheetReset()
                    }
                }
                is Fail -> { }
            }
        }
        observe(somFilterViewModel.updateFilterSelected) {
            when (it) {
                is Success -> {
                    somFilterAdapter?.updateData(it.data)
                    showHideBottomSheetReset()
                }
                is Fail -> { }
            }
        }
        observe(somFilterViewModel.somFilterOrderListParam) {
            when (it) {
                is Success -> {
                    somListOrderParam = it.data
                }
                is Fail -> { }
            }
        }
    }

    private fun showHideBottomSheetReset() {
        if (checkIsSelected() || filterDate.isNotBlank()) {
            bottomSheetAction.show()
        } else {
            bottomSheetAction.hide()
        }
    }

    private fun bottomSheetReset() {
        bottomSheetAction.hide()
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.reset)
        }
        bottomSheetAction.setOnClickListener {
            SomAnalytics.eventClickResetButtonOnFilterPage()
            actionResetFilter()
            somFilterViewModel.resetFilterSelected()
        }
    }

    private fun actionResetFilter() {
        somListOrderParam = somFilterViewModel.getSomListGetOrderListParam()
        somListOrderParam?.startDate = ""
        somListOrderParam?.endDate = ""
        somListOrderParam?.let { somFilterViewModel.setSomListGetOrderListParam(it) }
        filterDate = ""
    }

    private fun checkIsSelected(): Boolean {
        somFilterViewModel.getSomFilterUiModel().forEach {
            it.somFilterData.forEach { chips ->
                if (chips.isSelected) {
                    return true
                }
            }
        }
        return false
    }

    fun setSomFilterFinishListener(somFilterFinishListener: SomFilterFinishListener) {
        this.somFilterFinishListener = somFilterFinishListener
    }

    companion object {
        const val TITLE_FILTER = "Filter"
        const val SOM_FILTER_BOTTOM_SHEET_TAG = "SomFilterBottomSheetTag"
        const val SOM_FILTER_DATE_BOTTOM_SHEET_TAG = "SomFilterDateBottomSheetTag"
        const val KEY_ORDER_STATUS = "key_order_status"
        const val KEY_SOM_FILTER_LIST = "key_som_filter_list"
        const val KEY_ORDER_STATUS_ID_LIST = "key_order_status_id_list"
        const val KEY_FILTER_DATE = "key_filter_date"
        const val KEY_SOM_LIST_GET_ORDER_PARAM = "key_som_list_get_order_param"
        const val KEY_CACHE_MANAGER_ID = "key_cache_manager_id"
        const val KEY_IS_REQUEST_CANCEL_FILTER_APPLIED = "key_is_request_cancel_filter_applied"
        const val SIZE_ACTION_RESET = 14f
        const val REQUEST_CODE_FILTER_SEE_ALL = 901
        const val RESULT_CODE_FILTER_SEE_ALL = 801

        fun createInstance(orderStatus: String,
                           orderStatusIdList: List<Int>,
                           somFilterUiModelList: List<SomFilterUiModel>,
                           mActivity: FragmentActivity?,
                           filterDate: String,
                           isRequestCancelFilterApplied: Boolean
        ): SomFilterBottomSheet {
            val fragment = SomFilterBottomSheet(mActivity)
            val args = Bundle()
            args.putString(KEY_ORDER_STATUS, orderStatus)
            args.putIntegerArrayList(KEY_ORDER_STATUS_ID_LIST, ArrayList(orderStatusIdList))
            args.putParcelableArrayList(KEY_SOM_FILTER_LIST, ArrayList(somFilterUiModelList))
            args.putString(KEY_FILTER_DATE, filterDate)
            args.putBoolean(KEY_IS_REQUEST_CANCEL_FILTER_APPLIED, isRequestCancelFilterApplied)
            fragment.arguments = args
            return fragment
        }
    }

    interface SomFilterFinishListener {
        fun onClickShowOrderFilter(filterData: SomListGetOrderListParam,
                                   somFilterUiModelList: List<SomFilterUiModel>,
                                   idFilter: String,
                                   filterDate: String,
                                   isRequestCancelFilterApplied: Boolean
        )

        fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper)
    }
}