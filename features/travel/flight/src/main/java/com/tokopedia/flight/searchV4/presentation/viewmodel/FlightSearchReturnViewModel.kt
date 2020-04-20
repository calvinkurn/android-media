package com.tokopedia.flight.searchV4.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.presentation.model.FlightFareModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.searchV4.domain.FlightComboKeyUseCase
import com.tokopedia.flight.searchV4.domain.FlightSearchJouneyByIdUseCase
import com.tokopedia.flight.searchV4.presentation.model.SearchErrorEnum
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import timber.log.Timber
import java.util.concurrent.TimeUnit
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
    var selectedReturnJourney: FlightJourneyModel? = null
    var isBestPairing: Boolean = false
    var isViewOnlyBestPairing: Boolean = false
    var selectedFlightDepartureId: String = ""

    private val mutableDepartureJourney = MutableLiveData<FlightJourneyModel>()
    val departureJourney: LiveData<FlightJourneyModel>
        get() = mutableDepartureJourney

    private val mutableSearchErrorStringId = MutableLiveData<SearchErrorEnum>()
    val searchErrorStringId: LiveData<SearchErrorEnum>
        get() = mutableSearchErrorStringId

    init {
        mutableSearchErrorStringId.postValue(SearchErrorEnum.NOT_AVAILABLE)
    }

    fun getDepartureJourneyDetail() {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            mutableDepartureJourney.postValue(flightSearchJouneyByIdUseCase.execute(selectedFlightDepartureId))
        }) {
            it.printStackTrace()
        }
    }

    fun onFlightSearchSelected(flightSearchPassData: FlightSearchPassDataModel,
                               journeyModel: FlightJourneyModel? = null,
                               adapterPosition: Int = -1) {
        if (adapterPosition == -1) {
            flightAnalytics.eventSearchProductClickFromList(flightSearchPassData, journeyModel)
        } else {
            flightAnalytics.eventSearchProductClickFromList(flightSearchPassData, journeyModel, adapterPosition)
        }

        journeyModel?.let {
            launchCatchError(context = dispatcherProvider.ui(), block = {
                val departureJourney = if (mutableDepartureJourney.value != null) {
                    mutableDepartureJourney.value!!
                } else {
                    flightSearchJouneyByIdUseCase.execute(selectedFlightDepartureId)
                }

                val comboKey = flightComboKeyUseCase.execute(selectedFlightDepartureId, it.id)
                priceModel.comboKey = comboKey

                if (isValidReturnJourney(departureJourney, journeyModel)) {
                    priceModel.returnPrice = buildFare(journeyModel.fare, true)
                    selectedReturnJourney = journeyModel
                    mutableSearchErrorStringId.postValue(SearchErrorEnum.NO_ERRORS)
                } else {
                    mutableSearchErrorStringId.postValue(SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME)
                }
            }) {
                it.printStackTrace()
                mutableSearchErrorStringId.postValue(SearchErrorEnum.ERROR_PICK_JOURNEY)
            }
        }
    }

    fun onFlightSearchSelectFromDetail(flightSearchPassData: FlightSearchPassDataModel,
                                       selectedId: String) {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            val departureJourney = if (mutableDepartureJourney.value != null) {
                mutableDepartureJourney.value!!
            } else {
                flightSearchJouneyByIdUseCase.execute(selectedFlightDepartureId)
            }
            val returnJourney = flightSearchJouneyByIdUseCase.execute(selectedId)
            val comboKey = flightComboKeyUseCase.execute(selectedFlightDepartureId, selectedId)

            flightAnalytics.eventSearchProductClickFromDetail(flightSearchPassData, returnJourney)
            priceModel.returnPrice = buildFare(returnJourney.fare, true)
            priceModel.comboKey = comboKey

            if (isValidReturnJourney(departureJourney, returnJourney)) {
                selectedReturnJourney = returnJourney
                mutableSearchErrorStringId.postValue(SearchErrorEnum.NO_ERRORS)
            } else {
                mutableSearchErrorStringId.postValue(SearchErrorEnum.ERROR_RETURN_JOURNEY_TIME)
            }
        }) {
            it.printStackTrace()
            mutableSearchErrorStringId.postValue(SearchErrorEnum.ERROR_PICK_JOURNEY)
        }
    }

    private fun buildFare(journeyFare: FlightFareModel, isNeedCombo: Boolean): FlightFareModel =
            if (isNeedCombo) {
                FlightFareModel(
                        journeyFare.adult,
                        journeyFare.adultCombo,
                        journeyFare.child,
                        journeyFare.childCombo,
                        journeyFare.infant,
                        journeyFare.infantCombo,
                        journeyFare.adultNumeric,
                        journeyFare.adultNumericCombo,
                        journeyFare.childNumeric,
                        journeyFare.childNumericCombo,
                        journeyFare.infantNumeric,
                        journeyFare.infantNumericCombo
                )
            } else {
                FlightFareModel(
                        journeyFare.adult,
                        "",
                        journeyFare.child,
                        "",
                        journeyFare.infant,
                        "",
                        journeyFare.adultNumeric,
                        0,
                        journeyFare.childNumeric,
                        0,
                        journeyFare.infantNumeric,
                        0
                )
            }

    private fun isValidReturnJourney(departureModel: FlightJourneyModel, returnModel: FlightJourneyModel): Boolean {
        if (departureModel.routeList != null && returnModel.routeList != null &&
                departureModel.routeList.size > 0 && returnModel.routeList.size > 0) {
            val lastDepartureRoute = departureModel.routeList[departureModel.routeList.size - 1]
            val firstReturnRoute = returnModel.routeList[0]
            val departureArrivalTime = FlightDateUtil.stringToDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, lastDepartureRoute.arrivalTimestamp)
            val returnDepartureTime = FlightDateUtil.stringToDate(
                    FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, firstReturnRoute.departureTimestamp)
            val different = returnDepartureTime.time - departureArrivalTime.time
            return if (different >= 0) {
                val hours = different / ONE_HOUR
                Timber.d("diff : $hours")
                hours >= MIN_DIFF_HOURS
            } else {
                false
            }
        }
        return false
    }

    companion object {
        private val ONE_HOUR: Long = TimeUnit.HOURS.toMillis(1)
        private const val MIN_DIFF_HOURS = 6
    }
}