package com.tokopedia.flight.searchV4.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.searchV4.domain.FlightComboKeyUseCase
import com.tokopedia.flight.searchV4.domain.FlightSearchJouneyByIdUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

/**
 * @author by furqan on 16/04/2020
 */
class FlightSearchReturnViewModel @Inject constructor(private val flightSearchJouneyByIdUseCase: FlightSearchJouneyByIdUseCase,
                                                      private val flightComboKeyUseCase: FlightComboKeyUseCase,
                                                      private val flightAnalytics: FlightAnalytics,
                                                      private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    lateinit var priceModel: FlightPriceModel
    var isBestPairing: Boolean = false
    var isViewOnlyBestPairing: Boolean = false

    private var selectedFlightDepartureId: String = ""

    private val mutableDepartureJourney = MutableLiveData<FlightJourneyModel>()
    val departureJourney: LiveData<FlightJourneyModel>
        get() = mutableDepartureJourney

    fun getDepartureJourneyDetail() {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            mutableDepartureJourney.postValue(flightSearchJouneyByIdUseCase.execute(selectedFlightDepartureId))
        }) {
            it.printStackTrace()
        }
    }

    fun setSelectedDepartureId(selectedDepartureId: String) {
        selectedFlightDepartureId = selectedDepartureId
    }

}