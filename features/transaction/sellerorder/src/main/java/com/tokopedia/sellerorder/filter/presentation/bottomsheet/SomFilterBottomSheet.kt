package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.StatusBarColorUtil
import com.tokopedia.sellerorder.common.util.Utils.copyInt
import com.tokopedia.sellerorder.common.util.Utils.copyListParcelable
import com.tokopedia.sellerorder.filter.di.DaggerSomFilterComponent
import com.tokopedia.sellerorder.filter.di.SomFilterComponent
import com.tokopedia.sellerorder.filter.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapterTypeFactory
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.*
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomFilterBottomSheet : BottomSheetUnify(),
        SomFilterListener, SomFilterDateBottomSheet.CalenderListener,
        HasComponent<SomFilterComponent> {

    @Inject
    lateinit var somFilterViewModel: SomFilterViewModel

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
    private var isStatusFilterAppliedFromAdvancedFilter = false

    private var fm: FragmentManager? = null

    private var statusBarColorUtil: StatusBarColorUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        getDataFromArgumentOrCacheManager()
        setShowListener {
            setStatusBarColor()
        }
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
        observeUpdateFilterSelected()
        observeOrderListParam()
        observeResetFilter()
        bottomSheetReset()
        adjustBottomSheetPadding()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isApplyFilter) {
            val cancelWrapper = SomFilterCancelWrapper(orderStatus, statusList, somFilterUiModelListCopy, filterDate, somFilterViewModel.isRequestCancelFilterApplied())
            somFilterFinishListener?.onClickOverlayBottomSheet(cancelWrapper)
        }
    }

    private fun getDataFromArgumentOrCacheManager() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, arguments?.getString(KEY_CACHE_MANAGER_ID)) }
        val somFilterUiModelList = cacheManager?.get(KEY_SOM_FILTER_LIST, SomFilterUiModelWrapper::class.java) ?: SomFilterUiModelWrapper()
        somListOrderParam = cacheManager?.get(KEY_SOM_LIST_GET_ORDER_PARAM, SomListGetOrderListParam::class.java)
        orderStatus = arguments?.getString(KEY_ORDER_STATUS).orEmpty()
        filterDate = arguments?.getString(KEY_FILTER_DATE).orEmpty()
        isStatusFilterAppliedFromAdvancedFilter = arguments?.getBoolean(KEY_IS_STATUS_FILTER_APPLIED_FROM_ADVANCED, false) ?: false
        somFilterViewModel.setIsRequestCancelFilterApplied(arguments?.getBoolean(KEY_IS_REQUEST_CANCEL_FILTER_APPLIED, false) ?: false)
        val somFilterUiModelListTransform = somFilterUiModelList.somFilterUiModelList.transformFilterStatus(isStatusFilterAppliedFromAdvancedFilter)
        somFilterUiModelListCopy = somFilterUiModelListTransform.copyListParcelable()
        val statusListFilter = arguments?.getIntegerArrayList(KEY_ORDER_STATUS_ID_LIST)?.toList()
        statusList = statusListFilter?.copyInt() ?: listOf()
        somListOrderParam?.statusList = statusListFilter ?: listOf()
        somFilterViewModel.setSomFilterUiModel(somFilterUiModelListTransform)
        somListOrderParam?.let { somFilterViewModel.setSomListGetOrderListParam(it) }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottomsheet_som_filter_list, container, false)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        btnShowOrder = view.findViewById(R.id.btnShowOrder)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomFilterDialogStyle)
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
            FILTER_SORT, FILTER_LABEL, FILTER_STATUS_ORDER -> {
                somFilterViewModel.updateFilterSelected(idFilter, position, chipType)
                somFilterViewModel.updateParamSom(idFilter)
            }
            FILTER_TYPE_ORDER, FILTER_COURIER -> {
                somFilterViewModel.updateFilterManySelected(idFilter, chipType, position)
                somFilterViewModel.updateParamSom(idFilter)
            }
        }
    }

    override fun onSeeAllFilter(somFilterData: SomFilterUiModel, position: Int, idFilter: String) {
        val somFilterChipsList = somFilterViewModel.getSomFilterUiModel()
                .find { it.nameFilter == idFilter }?.somFilterData ?: listOf()
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_SOM_LIST_GET_ORDER_PARAM, somFilterViewModel.getSomListGetOrderListParam())
        cacheManager?.put(SomSubFilterActivity.KEY_SOM_LIST_FILTER_CHIPS, SomSubFilterListWrapper(somFilterChipsList))
        val intentSomSubFilter = SomSubFilterActivity.newInstance(requireContext(),
                filterDate,
                idFilter,
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
                    this.somListOrderParam = cacheManager?.get(KEY_SOM_LIST_GET_ORDER_PARAM, SomListGetOrderListParam::class.java)
                    somListOrderParam?.let { somFilterViewModel.setSomListGetOrderListParam(it) }
                    val idFilter = data?.getStringExtra(SomSubFilterActivity.KEY_ID_FILTER) ?: ""
                    val somSubFilterList: SomSubFilterListWrapper? = cacheManager?.get(SomSubFilterActivity.KEY_SOM_LIST_FILTER_CHIPS, SomSubFilterListWrapper::class.java)
                    somFilterViewModel.updateSomFilterSeeAll(idFilter, somSubFilterList?.somSubFilterList
                            ?: listOf())
                }
            }
        }
    }

    override fun onBtnSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        somListOrderParam = somFilterViewModel.getSomListGetOrderListParam()
        somListOrderParam?.startDate = startDate.first
        somListOrderParam?.endDate = endDate.first
        somListOrderParam?.let { somFilterViewModel.setSomListGetOrderListParam(it) }
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

    override fun onResume() {
        super.onResume()
        dialog?.window?.setWindowAnimations(-1)
    }

    override fun onDestroy() {
        removeObservers(somFilterViewModel.filterResult)
        removeObservers(somFilterViewModel.updateFilterSelected)
        removeObservers(somFilterViewModel.somFilterOrderListParam)
        removeObservers(somFilterViewModel.resetFilterResult)
        undoStatusBarColor()
        super.onDestroy()
    }

    private fun List<SomFilterUiModel>.transformFilterStatus(isStatusFilterAppliedFromAdvancedFilter: Boolean): List<SomFilterUiModel> {
        if (!isStatusFilterAppliedFromAdvancedFilter) {
            this.filter { it.nameFilter == FILTER_STATUS_ORDER }.map {
                it.somFilterData.map { chips ->
                    if (chips.childStatus.isNotEmpty()) {
                        chips.childStatus.map { child ->
                            if(!child.isChecked) {
                                child.isChecked = true
                            }
                        }
                    }
                }
            }
        }
        return this
    }


    private fun adjustBottomSheetPadding() {
        bottomSheetWrapper.setPadding(0, 16.toPx(), 0, bottomSheetWrapper.paddingBottom)
        (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(16.toPx(), 0,
                16.toPx(), 16.toPx())
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        isApplyFilter = false
        fm?.let {
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

    private fun setStatusBarColor() {
        statusBarColorUtil = StatusBarColorUtil(requireActivity())
        statusBarColorUtil?.setStatusBarColor()
    }

    private fun undoStatusBarColor() {
        statusBarColorUtil?.undoSetStatusBarColor()
        statusBarColorUtil = null
    }

    private fun clickShowOrder() {
        btnShowOrder?.setOnClickListener {
            isApplyFilter = true
            SomAnalytics.eventClickTerapkanOnFilterPage(getFilterTextReset())
            val copySomFilterUiModel = somFilterViewModel.getSomFilterUiModel()
            somListOrderParam?.let {
                somFilterFinishListener?.onClickShowOrderFilter(it, copySomFilterUiModel, FILTER_STATUS_ORDER, filterDate, somFilterViewModel.isRequestCancelFilterApplied())
            }
            dismiss()
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
        if (!somFilterDateBottomSheet.isAdded) {
            somFilterDateBottomSheet.show()
        }
    }

    private fun observeSomFilter() = observe(somFilterViewModel.filterResult) {
        finishSomFilterData()
        when (it) {
            is Success -> {
                if (it.data.isEmpty()) {
                    somFilterAdapter?.setEmptyState(SomFilterEmptyUiModel())
                } else {
                    somFilterAdapter?.updateData(it.data)
                    showHideBottomSheetReset()
                }
            }
            is Fail -> {
            }
        }
    }

    private fun observeOrderListParam() = observe(somFilterViewModel.somFilterOrderListParam) {
        when (it) {
            is Success -> {
                somListOrderParam = it.data
            }
            is Fail -> {
            }
        }
    }

    private fun observeUpdateFilterSelected() = observe(somFilterViewModel.updateFilterSelected) {
        when (it) {
            is Success -> {
                somFilterAdapter?.updateChipsSelected(it.data.first, it.data.second)
                showHideBottomSheetReset()
            }
            is Fail -> {
            }
        }
    }


    private fun observeResetFilter() = observe(somFilterViewModel.resetFilterResult) {
        when (it) {
            is Success -> {
                somFilterAdapter?.resetFilterSelected(it.data)
                showHideBottomSheetReset()
            }
            else -> {
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
        somFilterViewModel.setIsRequestCancelFilterApplied(false)
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
        const val KEY_IS_STATUS_FILTER_APPLIED_FROM_ADVANCED = "key_is_status_filter_applied_from_advanced"
        const val REQUEST_CODE_FILTER_SEE_ALL = 901
        const val RESULT_CODE_FILTER_SEE_ALL = 801

        fun createInstance(orderStatus: String,
                           isStatusFilterAppliedFromAdvancedFilter: Boolean,
                           orderStatusIdList: List<Int>,
                           filterDate: String,
                           isRequestCancelFilterApplied: Boolean,
                           cacheManagerId: String
        ): SomFilterBottomSheet {
            val fragment = SomFilterBottomSheet()
            val args = Bundle()
            args.putString(KEY_ORDER_STATUS, orderStatus)
            args.putBoolean(KEY_IS_STATUS_FILTER_APPLIED_FROM_ADVANCED, isStatusFilterAppliedFromAdvancedFilter)
            args.putIntegerArrayList(KEY_ORDER_STATUS_ID_LIST, ArrayList(orderStatusIdList))
            args.putString(KEY_FILTER_DATE, filterDate)
            args.putBoolean(KEY_IS_REQUEST_CANCEL_FILTER_APPLIED, isRequestCancelFilterApplied)
            args.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
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