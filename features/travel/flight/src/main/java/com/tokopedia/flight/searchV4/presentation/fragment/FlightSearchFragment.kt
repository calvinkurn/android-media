package com.tokopedia.flight.searchV4.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.di.DaggerFlightSearchComponent
import com.tokopedia.flight.searchV4.di.FlightSearchComponent
import com.tokopedia.flight.searchV4.presentation.activity.FlightSearchActivity
import com.tokopedia.flight.searchV4.presentation.viewmodel.FlightSearchViewModel
import javax.inject.Inject

/**
 * @author by furqan on 06/04/2020
 */
open class FlightSearchFragment : BaseListFragment<FlightJourneyModel, FlightSearchAdapterTypeFactory>(),
        FlightSearchAdapterTypeFactory.OnFlightSearchListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightSearchViewModel: FlightSearchViewModel

    lateinit var flightSearchComponent: FlightSearchComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightSearchViewModel = viewModelProvider.get(FlightSearchViewModel::class.java)

            arguments?.let {
                flightSearchViewModel.flightSearchPassData = it.getParcelable(FlightSearchActivity.EXTRA_PASS_DATA)!!
            }

            if (savedInstanceState == null) {
                flightSearchViewModel.filterModel = flightSearchViewModel.buildFilterModel(FlightFilterModel())
                flightSearchViewModel.flightAirportCombine = flightSearchViewModel.buildAirportCombineModel()
            }

            flightSearchViewModel.fetchSearchDataCloud(isReturnTrip())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory =
            FlightSearchAdapterTypeFactory(this)

    override fun getScreenName(): String = FlightAnalytics.Screen.SEARCH

    override fun initInjector() {
        if (!::flightSearchComponent.isInitialized) {
            flightSearchComponent = DaggerFlightSearchComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(requireActivity().application))
                    .build()
        }
        flightSearchComponent.inject(this)
    }

    override fun onItemClicked(t: FlightJourneyModel?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {}

    override fun onDetailClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(journeyViewModel: FlightJourneyModel?, adapterPosition: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowAllClicked() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowBestPairingClicked() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open fun getLayout(): Int = R.layout.fragment_search_flight

    open fun isReturnTrip(): Boolean = false

    companion object {
        fun newInstance(flightSearchPassDataModel: FlightSearchPassDataModel): FlightSearchFragment =
                FlightSearchFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(FlightSearchActivity.EXTRA_PASS_DATA, flightSearchPassDataModel)
                    }
                }
    }
}