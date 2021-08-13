package com.tokopedia.flight.filter.presentation.bottomsheets

import android.app.Application
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.di.DaggerFlightFilterComponent
import com.tokopedia.flight.filter.di.FlightFilterComponent
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.FlightFilterSortListener
import com.tokopedia.flight.filter.presentation.OnFlightFilterListener
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapter
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortAdapterTypeFactory
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterWidgetAirlineViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightSortViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel
import com.tokopedia.flight.filter.presentation.viewmodel.FlightFilterViewModel.Companion.SORT_DEFAULT_VALUE
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.statistics.AirlineStat
import com.tokopedia.flight.search.presentation.model.statistics.FlightSearchStatisticModel
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
                        arguments?.getInt(ARG_SORT) ?: SORT_DEFAULT_VALUE,
                        arguments?.getParcelable(ARG_FILTER_MODEL) ?: FlightFilterModel())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetAction.setTypeface(bottomSheetAction.typeface, Typeface.BOLD)
    }

    override fun getFlightSearchStaticticModel(): FlightSearchStatisticModel? = flightFilterViewModel.statisticModel.value

    override fun getFlightFilterModel(): FlightFilterModel? = flightFilterViewModel.filterModel.value

    override fun getFlightSelectedSort(): Int = flightFilterViewModel.selectedSort.value
            ?: SORT_DEFAULT_VALUE

    override fun onFlightFilterAirlineSaved(selectedAirlines: List<String>) {
        flightFilterViewModel.filterAirlines(selectedAirlines)
        with(rvFlightFilter.findViewHolderForAdapterPosition(FlightFilterViewModel.AIRLINE_ORDER) as FlightFilterWidgetAirlineViewHolder) {
            flightFilterViewModel.filterModel.value?.let {
                this.onSelectedAirlineChanged(it.airlineList)
            }
        }
    }

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
            btnFlightFilterSave.isLoading = true
            flightFilterViewModel.resetFilter()
            resetAllView()
        }

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_filter, null)
        setChild(mChildView)
    }

    private fun initAdapter() {
        val typeFactory = FlightFilterSortAdapterTypeFactory(this, getFlightSelectedSort(),
                getFlightFilterModel() ?: FlightFilterModel())
        adapter = FlightFilterSortAdapter(typeFactory)
    }

    private fun initView() {
        with(mChildView) {
            rvFlightFilter.setHasFixedSize(true)
            rvFlightFilter.adapter = adapter
            rvFlightFilter.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            rvFlightFilter.setHasFixedSize(true)

            btnFlightFilterSave.setOnClickListener {
                val filterModel = getFlightFilterModel()
                filterModel?.apply {
                    setHasFilter(getStatisticModel())
                }
                listener?.onSaveFilter(getFlightSelectedSort(),
                        filterModel,
                        Pair(getStatisticModel()?.minPrice ?: 0, getStatisticModel()?.maxPrice
                                ?: Int.MAX_VALUE))
                dismiss()
            }
        }
    }

    private fun renderFlightCount(flightCount: Int) {
        with(mChildView) {
            btnFlightFilterSave.isLoading = false
            btnFlightFilterSave.visibility = View.VISIBLE
            btnFlightFilterSave.text = getString(R.string.flight_there_has_x_flights, flightCount)
        }
    }

    override fun onSortChanged(selectedSortOption: Int) {
        flightFilterViewModel.setSelectedSort(selectedSortOption)
    }

    override fun onClickSeeAllSort() {
        val flightSortBottomSheet = FlightSortBottomSheet.newInstance(getFlightSelectedSort())
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


    override fun onClickSeeAllAirline() {
        val filterAirlineBottomSheet = FlightFilterAirlineBottomSheet.getInstance()
        filterAirlineBottomSheet.listener = this
        filterAirlineBottomSheet.setShowListener { filterAirlineBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        fragmentManager?.let {
            filterAirlineBottomSheet.show(it, FlightFilterAirlineBottomSheet.TAG_FILTER_AIRLINE)
        }
    }

    override fun getStatisticModel(): FlightSearchStatisticModel? = flightFilterViewModel.statisticModel.value

    override fun getAirlineList(): List<AirlineStat> =
            flightFilterViewModel.getAirlineList()

    private fun renderList(data: List<BaseFilterSortModel>) {
        adapter?.clearAllElements()
        adapter?.addElement(data)
        adapter?.notifyDataSetChanged()
    }

    override fun onAirlineChanged(checkedAirlines: List<String>) {
        flightFilterViewModel.filterAirlines(checkedAirlines)
    }

    override fun onFacilityChanged(selectedFacilities: List<FlightFilterFacilityEnum>) {
        flightFilterViewModel.filterFacilities(selectedFacilities)
    }

    override fun onPriceRangeChanged(minPrice: Int, maxPrice: Int) {
        flightFilterViewModel.filterPrices(minPrice, maxPrice)
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

    private fun resetAllView() {
        (rvFlightFilter.adapter as FlightFilterSortAdapter).resetFilter()
        (rvFlightFilter.adapter as FlightFilterSortAdapter).typeFactory.filterModel =
                getFlightFilterModel() ?: FlightFilterModel()
    }

    companion object {
        const val TAG_FILTER = "TagFilterBottomSheet"
        private const val TAG_FLIGHT_SORT = "TagFlightSortBottomSheet"

        private const val ARG_SORT = "ARG_SORT"
        private const val ARG_FILTER_MODEL = "ARG_FILTER_MODEL"

        fun getInstance(selectedSortOption: Int, filterModel: FlightFilterModel): FlightFilterBottomSheet =
                FlightFilterBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putInt(ARG_SORT, selectedSortOption)
                        putParcelable(ARG_FILTER_MODEL, filterModel)
                    }
                }
    }

    interface FlightFilterBottomSheetListener {
        fun onSaveFilter(sortOption: Int, flightFilterModel: FlightFilterModel?, statisticPricePair: Pair<Int, Int>)
    }

}