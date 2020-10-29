package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.common.di.component.HasComponent
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
    private var orderStatus: String = ""

    private var somFilterFinishListener: SomFilterFinishListener? = null
    private var somListOrderParam: SomListGetOrderListParam? = null

    private var fm: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        orderStatus = arguments?.getString(KEY_ORDER_STATUS).orEmpty()
        val somFilterUiModelList = arguments?.getParcelableArrayList<SomFilterUiModel>(KEY_SOM_FILTER_LIST)
                ?: arrayListOf()
        somListOrderParam?.statusList = arguments?.getIntegerArrayList(KEY_ORDER_STATUS_ID_LIST)?.toList() ?: listOf()
        somFilterViewModel.setSomFilterUiModel(somFilterUiModelList.toList())
        somFilterViewModel.setSomListGetOrderListParam(somListOrderParam ?: SomListGetOrderListParam())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
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

    override fun onBtnSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        somListOrderParam = somFilterViewModel.getSomListGetOrderListParam()
        somListOrderParam?.startDate = startDate.first
        somListOrderParam?.endDate = endDate.first
        somFilterAdapter?.updateDateFilterText(startDate.second, endDate.second)
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

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        val view = inflater.inflate(R.layout.bottomsheet_som_filter_list, container, false)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        btnShowOrder = view.findViewById(R.id.btnShowOrder)
        setChild(view)
        setTitle(TITLE_FILTER)
        showKnob = true
    }

    fun show() {
        fm?.let {
            show(it, SOM_FILTER_BOTTOM_SHEET_TAG)
        }
    }

    fun setFragmentManager(fm: FragmentManager): SomFilterBottomSheet {
        this.fm = fm
        return this
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
        val somFilterDateBottomSheet = SomFilterDateBottomSheet.newInstance()
        somFilterDateBottomSheet.setCalendarListener(this)
        somFilterDateBottomSheet.setFragmentManager(childFragmentManager)
        somFilterDateBottomSheet.show()
    }

    private fun observeSomFilter() {
        observe(somFilterViewModel.filterResult) {
            finishSomFilterData()
            when (it) {
                is Success -> {
                    if (it.data.isEmpty()) {
                        somFilterAdapter?.setEmptyState(EmptyModel())
                    } else {
                        showHideBottomSheetReset()
                        somFilterAdapter?.updateData(it.data)
                    }
                }
                is Fail -> {
                }
            }
        }
        observe(somFilterViewModel.updateFilterSelected) {
            showHideBottomSheetReset()
            somFilterAdapter?.updateData(it)
        }
        observe(somFilterViewModel.somFilterUiModelData) {
            somListOrderParam = it
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
        bottomSheetAction.visibility = View.GONE
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.reset)
        }
        bottomSheetAction.setOnClickListener {
            somFilterViewModel.resetFilterSelected(orderStatus)
        }
    }

    private fun checkIsSelected(): Boolean {
        somFilterViewModel.getSomFilterUiModel().forEach {
            it.somFilterData.filter { somChips -> somChips.name != orderStatus }.onEach { chips ->
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

        fun createInstance(orderStatus: String,
                           orderStatusIdList: List<Int>,
                           somFilterUiModelList: List<SomFilterUiModel>): SomFilterBottomSheet {
            val fragment = SomFilterBottomSheet()
            val args = Bundle()
            args.putString(KEY_ORDER_STATUS, orderStatus)
            args.putIntegerArrayList(KEY_SOM_FILTER_LIST, ArrayList(orderStatusIdList))
            args.putParcelableArrayList(KEY_SOM_FILTER_LIST, ArrayList(somFilterUiModelList))
            fragment.arguments = args
            return fragment
        }
    }

    interface SomFilterFinishListener {
        fun onClickShowOrderFilter(filterData: SomListGetOrderListParam, somFilterUiModelList: List<SomFilterUiModel>, idFilter: String, orderStatus: String)
    }
}