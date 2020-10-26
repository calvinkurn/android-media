package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_DEADLINE
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_LABEL
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_SORT
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_STATUS_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.FILTER_TYPE_ORDER
import com.tokopedia.sellerorder.filter.di.SomFilterComponent
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapter
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterAdapterTypeFactory
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SomFilterBottomSheet(private var somFilterFinishListener: SomFilterFinishListener,
                           private var somListOrderParam: SomListGetOrderListParam? = null) : BottomSheetUnify(),
        SomFilterListener, SomFilterDateBottomSheet.CalenderListener, HasComponent<SomFilterComponent> {

    @Inject
    lateinit var somFilterViewModel: SomFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var rvSomFilter: RecyclerView? = null
    private var btnShowOrder: UnifyButton? = null
    private var somFilterAdapter: SomFilterAdapter? = null
    private var cacheManager: SaveInstanceCacheManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { bundle ->
            val cacheManagerId = bundle.getString(SOM_FILTER_CACHE_MANAGER_ID).orEmpty()
            cacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
            somListOrderParam = cacheManager?.get(KEY_SOM_FILTER_ORDER_LIST, SomListGetOrderListParam::class.java)
        }
        initInject()
        val view = View.inflate(requireContext(), R.layout.bottomsheet_som_filter_list, null)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        btnShowOrder = view.findViewById(R.id.btnShowOrder)
        setChild(view)
        setTitle(TITLE_FILTER)
        showKnob = true
        isDragable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        clickShowOrder()
        somFilterViewModel.getSomFilterData()
        observeSomFilter()
    }

    override fun onDateClicked(position: Int) {
        clickDateFilter()
    }

    override fun onFilterChipsClicked(somFilterData: SomFilterChipsUiModel, idFilter: String, position: Int, chipType: String) {
        when (idFilter) {
            FILTER_SORT, FILTER_LABEL, FILTER_DEADLINE -> {
                somFilterViewModel.updateFilterSelected(idFilter, position)
                somFilterViewModel.updateParamSom(idFilter)
            }
            FILTER_STATUS_ORDER, FILTER_TYPE_ORDER, FILTER_COURIER -> {
                somFilterViewModel.updateFilterManySelected(idFilter, chipType)
                somFilterViewModel.updateParamSom(idFilter)
            }
        }
    }

    override fun onSeeAllFilter(somFilterData: SomFilterUiModel, position: Int) {

    }

    override fun onBtnSaveCalendarClicked(somListOrderParam: SomListGetOrderListParam) {
        this.somListOrderParam = somListOrderParam
    }

    override fun getComponent(): SomFilterComponent? {
        return activity?.run {
            DaggerSomFilterBottomSheetComponent
                    .builder()
                    .somFilterComponent(SomComponentInstance.getSomComponent(application))
                    .build()
        }
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
            somListOrderParam?.let { it1 -> somFilterFinishListener.onClickShowOrder(it1) }
            dismiss()
        }
    }

    private fun clickDateFilter() {
        val somFilterDateBottomSheet = somListOrderParam?.let {
            SomFilterDateBottomSheet.newInstance(it, this)
        }
        childFragmentManager.let {
            somFilterDateBottomSheet?.show(it, SOM_FILTER_BOTTOM_SHEET_TAG)
        }
    }

    private fun observeSomFilter() {
        observe(somFilterViewModel.filterResult) {
            somFilterAdapter?.hideLoading()
            when (it) {
                is Success -> {
                    somFilterAdapter?.updateData(it.data)
                }
                is Fail -> { }
            }
        }
        observe(somFilterViewModel.updateFilterSelected) {
            somFilterAdapter?.updateData(it)
        }
        observe(somFilterViewModel.somFilterUiModelData) {
            somListOrderParam = it
        }
    }

    companion object {
        const val TITLE_FILTER = "Filter"
        const val SOM_FILTER_CACHE_MANAGER_ID = "som_filter cache_manager_id"
        const val KEY_SOM_FILTER_ORDER_LIST = "key_som_filter_order_list"
        const val SOM_FILTER_BOTTOM_SHEET_TAG = "SomFilterBottomSheetTag"

        fun createInstance(somFilterFinishListener: SomFilterFinishListener, somListOrderParam: SomListGetOrderListParam, somFilterCacheId: Int): SomFilterBottomSheet {
            return SomFilterBottomSheet(somFilterFinishListener, somListOrderParam, somFilterCacheId)
        }
    }

    interface SomFilterFinishListener {
        fun onClickShowOrder(filterData: SomListGetOrderListParam)
    }
}