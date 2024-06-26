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
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.common.util.StatusBarColorUtil
import com.tokopedia.sellerorder.common.util.Utils.copyInt
import com.tokopedia.sellerorder.common.util.Utils.copyListParcelable
import com.tokopedia.sellerorder.common.util.Utils.updateShopActive
import com.tokopedia.sellerorder.databinding.BottomsheetSomFilterListBinding
import com.tokopedia.sellerorder.filter.di.DaggerSomFilterComponent
import com.tokopedia.sellerorder.filter.di.SomFilterComponent
import com.tokopedia.sellerorder.filter.presentation.activity.SomSubFilterActivity
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapterTypeFactory
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterCancelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterEmptyUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModelWrapper
import com.tokopedia.sellerorder.filter.presentation.model.SomSubFilterListWrapper
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SomFilterBottomSheet :
    BottomSheetUnify(),
    SomFilterListener,
    SomFilterDateBottomSheet.CalenderListener,
    HasComponent<SomFilterComponent> {

    @Inject
    lateinit var somFilterViewModel: SomFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var somFilterAdapter: SomFilterAdapter? = null
    private var isApplyFilter: Boolean = false
    private var statusList = listOf<Int>()

    private var somFilterFinishListener: SomFilterFinishListener? = null
    private var somListOrderParam: SomListGetOrderListParam? = null
    private var somFilterUiModelListCopy = listOf<SomFilterUiModel>()

    private var statusBarColorUtil: StatusBarColorUtil? = null

    private var binding by autoClearedNullable<BottomsheetSomFilterListBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        getDataFromArgumentOrCacheManager()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setStatusBarColor()
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
            val cancelWrapper = SomFilterCancelWrapper(statusList, somFilterUiModelListCopy)
            somFilterFinishListener?.onClickOverlayBottomSheet(cancelWrapper)
        }
    }

    private fun getDataFromArgumentOrCacheManager() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, arguments?.getString(KEY_CACHE_MANAGER_ID)) }
        val somFilterUiModelList = cacheManager?.get(KEY_SOM_FILTER_LIST, SomFilterUiModelWrapper::class.java) ?: SomFilterUiModelWrapper()
        somListOrderParam = cacheManager?.get(KEY_SOM_LIST_GET_ORDER_PARAM, SomListGetOrderListParam::class.java)
        somFilterUiModelListCopy = somFilterUiModelList.somFilterUiModelList.copyListParcelable()
        val statusListFilter = arguments?.getIntegerArrayList(KEY_ORDER_STATUS_ID_LIST)?.toList()
        statusList = statusListFilter?.copyInt() ?: listOf()
        somListOrderParam?.statusList = statusListFilter ?: listOf()
        somFilterViewModel.setSomFilterUiModel(somFilterUiModelList.somFilterUiModelList)
        somListOrderParam?.let { somFilterViewModel.setSomListGetOrderListParam(it) }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetSomFilterListBinding.inflate(inflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomFilterDialogStyle)
        setChild(binding?.root)
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

    override fun onFilterChipsClicked(
        somFilterData: SomFilterChipsUiModel,
        idFilter: String,
        position: Int,
        chipType: String,
        orderStatus: String
    ) {
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
        val intentSomSubFilter = SomSubFilterActivity.newInstance(
            requireContext(),
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
                    somFilterViewModel.updateSomFilterSeeAll(
                        idFilter,
                        somSubFilterList?.somSubFilterList
                            ?: listOf()
                    )
                }
            }
        }
    }

    private fun cleanupResources() {
        somListOrderParam = null
        somFilterAdapter = null
        somFilterFinishListener = null
        statusList = emptyList()
        somFilterUiModelListCopy = emptyList()
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
        updateShopActive()
    }

    override fun onDestroy() {
        removeObservers(somFilterViewModel.filterResult)
        removeObservers(somFilterViewModel.updateFilterSelected)
        removeObservers(somFilterViewModel.somFilterOrderListParam)
        removeObservers(somFilterViewModel.resetFilterResult)
        undoStatusBarColor()
        cleanupResources()
        super.onDestroy()
    }

    private fun adjustBottomSheetPadding() {
        bottomSheetWrapper.setPadding(0, 16.toPx(), 0, bottomSheetWrapper.paddingBottom)
        (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(
            16.toPx(),
            0,
            16.toPx(),
            16.toPx()
        )
    }

    fun show(fm: FragmentManager?) {
        if (!isVisible) {
            isApplyFilter = false
            fm?.let {
                show(it, SOM_FILTER_BOTTOM_SHEET_TAG)
            }
        }
    }

    private fun loadSomFilterData() {
        somFilterAdapter?.showLoading()
        binding?.btnShowOrder?.hide()
        somFilterViewModel.getSomFilterData()
    }

    private fun finishSomFilterData() {
        somFilterAdapter?.hideLoading()
        binding?.btnShowOrder?.show()
    }

    private fun initRecyclerView() {
        val somFilterAdapterTypeFactory = SomFilterAdapterTypeFactory(this)
        somFilterAdapter = SomFilterAdapter(somFilterAdapterTypeFactory)
        binding?.rvSomFilter?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = somFilterAdapter
        }
    }

    private fun initInject() {
        component?.inject(this)
    }

    private fun setStatusBarColor() {
        activity?.let {
            statusBarColorUtil = StatusBarColorUtil(it)
        }
        statusBarColorUtil?.setStatusBarColor()
    }

    private fun undoStatusBarColor() {
        statusBarColorUtil?.undoSetStatusBarColor()
        statusBarColorUtil?.activityRef?.clear()
        statusBarColorUtil = null
    }

    private fun clickShowOrder() {
        binding?.btnShowOrder?.setOnClickListener {
            isApplyFilter = true
            SomAnalytics.eventClickTerapkanOnFilterPage(getFilterTextReset())
            val copySomFilterUiModel = somFilterViewModel.getSomFilterUiModel()
            somListOrderParam?.let {
                somFilterFinishListener?.onClickShowOrderFilter(
                    it,
                    copySomFilterUiModel,
                    FILTER_STATUS_ORDER
                )
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
        val startDate = somFilterViewModel.getStartDate()
        val endDate = somFilterViewModel.getEndDate()
        if (startDate != null && endDate != null) {
            somFilterDateBottomSheet.selectedDates = listOf(startDate, endDate)
        }
        somFilterDateBottomSheet.setCalendarListener(this)
        somFilterDateBottomSheet.show(childFragmentManager)
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
                SomErrorHandler.logExceptionToServer(
                    errorTag = SomErrorHandler.SOM_TAG,
                    throwable = it.throwable,
                    errorType =
                    SomErrorHandler.SomMessage.GET_FILTER_DATA_ERROR,
                    deviceId = userSession.deviceId.orEmpty()
                )
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
        if (checkIsSelected()) {
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
            somFilterViewModel.resetFilterSelected()
        }
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
        const val KEY_SOM_FILTER_LIST = "key_som_filter_list"
        const val KEY_ORDER_STATUS_ID_LIST = "key_order_status_id_list"
        const val KEY_SOM_LIST_GET_ORDER_PARAM = "key_som_list_get_order_param"
        const val KEY_CACHE_MANAGER_ID = "key_cache_manager_id"
        const val REQUEST_CODE_FILTER_SEE_ALL = 901
        const val RESULT_CODE_FILTER_SEE_ALL = 801

        fun createInstance(
            orderStatusIdList: List<Int>,
            cacheManagerId: String
        ): SomFilterBottomSheet {
            val fragment = SomFilterBottomSheet()
            val args = Bundle()
            args.putIntegerArrayList(KEY_ORDER_STATUS_ID_LIST, ArrayList(orderStatusIdList))
            args.putString(KEY_CACHE_MANAGER_ID, cacheManagerId)
            fragment.arguments = args
            return fragment
        }
    }

    interface SomFilterFinishListener {
        fun onClickShowOrderFilter(
            filterData: SomListGetOrderListParam,
            somFilterUiModelList: List<SomFilterUiModel>,
            idFilter: String
        )

        fun onClickOverlayBottomSheet(filterCancelWrapper: SomFilterCancelWrapper)
    }
}
