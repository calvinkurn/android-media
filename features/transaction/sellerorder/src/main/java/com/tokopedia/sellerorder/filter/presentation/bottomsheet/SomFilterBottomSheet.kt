package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.filter.di.DaggerSomFilterComponent
import com.tokopedia.sellerorder.filter.di.SomFilterComponent
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapterTypeFactory
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment.Companion.EXTRA_SOM_LIST_GET_ORDER_PARAM
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SomFilterBottomSheet : BottomSheetUnify(),
        SomFilterListener, SomFilterDateBottomSheet.CalenderListener, HasComponent<SomFilterComponent> {

    @Inject
    lateinit var somFilterViewModel: SomFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var rvSomFilter: RecyclerView? = null
    private var btnShowOrder: UnifyButton? = null
    private var somFilterAdapter: SomFilterAdapter? = null
    private var cacheManager: SaveInstanceCacheManager? = null
    private var cacheManagerId: String = ""
    private var orderStatus: String = ""

    private var somFilterFinishListener: SomFilterFinishListener? = null
    private var somListOrderParam: SomListGetOrderListParam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        cacheManagerId = arguments?.getString(SOM_FILTER_CACHE_MANAGER_ID).orEmpty()
        orderStatus = arguments?.getString(KEY_ORDER_STATUS).orEmpty()
        activity?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
        }
        val manager = if (savedInstanceState == null) {
            context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
        } else {
            cacheManager
        }
        somListOrderParam = manager?.get(EXTRA_SOM_LIST_GET_ORDER_PARAM, SomListGetOrderListParam::class.java)
        super.onCreate(savedInstanceState)
        initInject()
        somFilterViewModel.setSomListGetOrderListParam(somListOrderParam ?: SomListGetOrderListParam())
        val view = View.inflate(requireContext(), R.layout.bottomsheet_som_filter_list, null)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        btnShowOrder = view.findViewById(R.id.btnShowOrder)
        setChild(view)
        setTitle(TITLE_FILTER)
        showKnob = true
        isFullpage = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        clickShowOrder()
        loadSomFilterData()
        observeSomFilter()
        bottomSheetReset()
    }

    override fun onDateClicked(position: Int) {
        clickDateFilter()
    }

    override fun onFilterChipsClicked(somFilterData: SomFilterChipsUiModel, idFilter: String,
                                      position: Int, chipType: String, orderStatus: String) {
        this.orderStatus = orderStatus
        when (idFilter) {
            FILTER_SORT, FILTER_LABEL, FILTER_DEADLINE, FILTER_STATUS_ORDER -> {
                somFilterViewModel.updateFilterSelected(idFilter, position, chipType)
                somFilterViewModel.updateParamSom(idFilter)
            }
            FILTER_TYPE_ORDER, FILTER_COURIER -> {
                somFilterViewModel.updateFilterManySelected(idFilter, chipType, position)
                somFilterViewModel.updateParamSom(idFilter)
            }
        }
    }

    override fun onSeeAllFilter(somFilterData: SomFilterUiModel, position: Int) {

    }

    override fun onBtnSaveCalendarClicked(startDate: String, endDate: String) {
        somListOrderParam?.startDate = startDate
        somListOrderParam?.endDate = endDate
        somFilterAdapter?.updateDateFilterText(startDate, endDate)
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
        removeObservers(somFilterViewModel.somFilterUiModelData)
        removeObservers(somFilterViewModel.updateFilterSelected)
        removeObservers(somFilterViewModel.somFilterUiModelData)
        super.onDestroy()
    }

    private fun loadSomFilterData() {
        somFilterAdapter?.showLoading()
        btnShowOrder?.hide()
        somFilterViewModel.getSomFilterData(orderStatus)
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
            somListOrderParam?.let { somListParam ->
                somFilterFinishListener?.onClickShowOrderFilter(somListParam,
                        somFilterViewModel.getSomFilterUiModel(), FILTER_STATUS_ORDER, orderStatus)
            }
            dismiss()
        }
    }

    private fun clickDateFilter() {
        val somFilterDateBottomSheet = SomFilterDateBottomSheet.newInstance().apply {
            setCalendarListener(this@SomFilterBottomSheet)
        }
        childFragmentManager.let {
            somFilterDateBottomSheet.show(it, SOM_FILTER_DATE_BOTTOM_SHEET_TAG)
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
                    }
                }
                is Fail -> {
                }
            }
        }
        observe(somFilterViewModel.updateFilterSelected) {
            if (checkIsSelected()) {
                bottomSheetAction.show()
            } else {
                bottomSheetAction.hide()
            }
            somFilterAdapter?.updateData(it)
        }
        observe(somFilterViewModel.somFilterUiModelData) {
            somListOrderParam = it
        }
    }

    private fun bottomSheetReset() {
        bottomSheetAction.visibility = View.GONE
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.reset)
        }
        bottomSheetAction.setOnClickListener {
            somFilterViewModel.resetFilterSelected()
        }
    }

    private fun checkIsSelected(): Boolean {
        somFilterViewModel.getSomFilterUiModel()?.forEach {
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
        const val SOM_FILTER_CACHE_MANAGER_ID = "som_filter cache_manager_id"
        const val SOM_FILTER_BOTTOM_SHEET_TAG = "SomFilterBottomSheetTag"
        const val SOM_FILTER_DATE_BOTTOM_SHEET_TAG = "SomFilterDateBottomSheetTag"
        const val KEY_ORDER_STATUS = "key_order_status"

        fun createInstance(cacheObjectId: String, orderStatus: String): SomFilterBottomSheet {
            val fragment = SomFilterBottomSheet()
            val args = Bundle()
            args.putString(SOM_FILTER_CACHE_MANAGER_ID, cacheObjectId)
            args.putString(KEY_ORDER_STATUS, orderStatus)
            fragment.arguments = args
            return fragment
        }
    }

    interface SomFilterFinishListener {
        fun onClickShowOrderFilter(filterData: SomListGetOrderListParam, somFilterUiModel: List<SomFilterUiModel>?, idFilter: String, orderStatus: String)
    }
}