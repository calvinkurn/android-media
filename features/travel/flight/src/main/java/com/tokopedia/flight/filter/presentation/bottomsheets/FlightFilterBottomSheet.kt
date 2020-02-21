package com.tokopedia.flight.filter.presentation.bottomsheets

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.di.DaggerFlightFilterComponent
import com.tokopedia.flight.filter.di.FlightFilterComponent
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.OnFlightFilterListener
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapter
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightSortViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_flight_filter.*
import kotlinx.android.synthetic.main.fragment_flight_filter.view.*
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterBottomSheet : BottomSheetUnify(), OnFlightFilterListener, FlightFilterSortListener {

    var adapter: FlightFilterSortAdapter? = null
    var listener: FlightFilterBottomSheetListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightFilterViewModel: FlightFilterViewModel

    private lateinit var flightFilterComponent: FlightFilterComponent
    private lateinit var mChildView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightFilterViewModel = viewModelProvider.get(FlightFilterViewModel::class.java)
            if (arguments?.containsKey(ARG_FILTER_MODEL) == true) {
                flightFilterViewModel.init(
                        arguments?.getInt(ARG_SORT) ?: TravelSortOption.CHEAPEST,
                        arguments?.getParcelable(ARG_FILTER_MODEL) as FlightFilterModel,
                        arguments?.getBoolean(ARG_IS_RETURN) ?: false)
            }
        }

        initBottomSheet()
        initAdapter()
        initView()
        showLoading()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightFilterViewModel.flightCount.observe(this, Observer {
            renderFlightCount(it)
        })

        flightFilterViewModel.filterViewData.observe(this, Observer {
            if (it.isNotEmpty()) {
                renderList(it)
                hideLoading()
            }
        })
    }

    override fun getFlightSearchStaticticModel(): FlightSearchStatisticModel? = flightFilterViewModel.statisticModel.value

    override fun getFlightFilterModel(): FlightFilterModel? = flightFilterViewModel.filterModel.value

    private fun initInjector() {
        if (!::flightFilterComponent.isInitialized) {
            flightFilterComponent = DaggerFlightFilterComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(requireActivity().application as Application))
                    .build()
        }
        flightFilterComponent.inject(this)
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_filter_label))
        setAction(getString(R.string.flight_reset_label)) {
            flightFilterViewModel.resetFilter()
        }

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_filter, null)
        setChild(mChildView)
    }

    private fun initAdapter() {
        val typeFactory = FlightFilterSortAdapterTypeFactory(this, flightFilterViewModel.getSelectedSort(),
                flightFilterViewModel.filterModel.value ?: FlightFilterModel())
        adapter = FlightFilterSortAdapter(typeFactory)
    }

    private fun initView() {
        with(mChildView) {
            rvFlightFilter.setHasFixedSize(true)
            rvFlightFilter.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rvFlightFilter.adapter = adapter

            btnFlightFilterSave.setOnClickListener {
                listener?.onSaveFilter(flightFilterViewModel.getSelectedSort(), flightFilterViewModel.filterModel.value)
                dismiss()
            }
        }
    }

    private fun renderFlightCount(flightCount: Int) {
        with(mChildView) {
            btnFlightFilterSave.visibility = View.VISIBLE
            btnFlightFilterSave.text = getString(R.string.flight_there_has_x_flights, flightCount)
        }
    }

    override fun onSortChanged(selectedSortOption: Int) {
        flightFilterViewModel.setSelectedSort(selectedSortOption)
    }

    override fun onClickSeeAllSort() {
        val flightSortBottomSheet = FlightSortBottomSheet.newInstance(flightFilterViewModel.getSelectedSort())
        flightSortBottomSheet.listener = object : FlightSortBottomSheet.ActionListener {
            override fun onSortOptionClicked(selectedSortOption: Int) {
                flightFilterViewModel.setSelectedSort(selectedSortOption)
                with(rvFlightFilter.findViewHolderForAdapterPosition(FlightFilterViewModel.SORT_ORDER) as FlightSortViewHolder) {
                    this.performClickOnSortId(selectedSortOption)
                }
            }
        }
        flightSortBottomSheet.setShowListener { flightSortBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        fragmentManager?.let {
            flightSortBottomSheet.show(it, TAG_FLIGHT_SORT)
        }
    }

    override fun onTransitFilterChanged(transitTypeList: List<TransitEnum>) {
        flightFilterViewModel.filterTransit(transitTypeList)
    }

    override fun onDepartureTimeFilterChanged(departureTimeList: List<DepartureTimeEnum>) {
        flightFilterViewModel.filterDepartureTime(departureTimeList)
    }

    override fun onArrivalTimeFilterChanged(arrivalTimeList: List<DepartureTimeEnum>) {
        flightFilterViewModel.filterArrivalTime(arrivalTimeList)
    }


    private fun renderList(data: List<BaseFilterSortModel>) {
        adapter?.clearAllElements()
        adapter?.addElement(data)
        adapter?.notifyDataSetChanged()
    }

    private fun showLoading() {
        with(mChildView) {
            containerLoading.visibility = View.VISIBLE
            containerContent.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        with(mChildView) {
            containerLoading.visibility = View.GONE
            containerContent.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TAG_FILTER = "TagFilterBottomSheet"
        private const val TAG_FLIGHT_SORT = "TagFlightSortBottomSheet"

        private const val ARG_SORT = "ARG_SORT"
        private const val ARG_FILTER_MODEL = "ARG_FILTER_MODEL"
        private const val ARG_IS_RETURN = "ARG_IS_RETURN"

        fun getInstance(selectedSortOption: Int, filterModel: FlightFilterModel, isReturn: Boolean = false): FlightFilterBottomSheet =
                FlightFilterBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putInt(ARG_SORT, selectedSortOption)
                        putParcelable(ARG_FILTER_MODEL, filterModel)
                        putBoolean(ARG_IS_RETURN, isReturn)
                    }
                }
    }

    interface FlightFilterBottomSheetListener {
        fun onSaveFilter(sortOption: Int, flightFilterModel: FlightFilterModel?)
    }

}