package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationGetPassengerUseCase
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 15/07/2020
 */
class FlightCancellationPassengerViewModel @Inject constructor(
        private val getCancellablePassengerUseCase: FlightCancellationGetPassengerUseCase,
        private val flightAnalytics: FlightAnalytics,
        private val userSession: UserSessionInterface,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var invoiceId: String = ""

    private val passengerRelationsMap: HashMap<String, FlightCancellationPassengerModel> = hashMapOf()
    val selectedCancellationPassengerList: MutableList<FlightCancellationModel> = arrayListOf()

    private val mutableCancellationPassengerList = MutableLiveData<List<FlightCancellationModel>>()
    val cancellationPassengerList: LiveData<List<FlightCancellationModel>>
        get() = mutableCancellationPassengerList

    fun trackOnNext() {
        for (item in selectedCancellationPassengerList) {
            val route = "${item.flightCancellationJourney.departureAirportId}${item.flightCancellationJourney.arrivalAirportId}"
            val departureDate = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, item.flightCancellationJourney.departureTime)

            flightAnalytics.eventClickNextOnCancellationPassenger(
                    "$route - ${item.flightCancellationJourney.airlineName} - $departureDate - $invoiceId",
                    userSession.userId
            )
        }
    }

    fun canGoNext(): Boolean {
        var canGoNext = false

        for (cancellation in selectedCancellationPassengerList) {
            if (cancellation.passengerModelList.isNotEmpty()) {
                canGoNext = true
            }
        }

        return canGoNext
    }

    fun getCancellablePassenger(invoiceId: String, flightCancellationJourneyList: List<FlightCancellationJourney>) {
        this.invoiceId = invoiceId
        launchCatchError(dispatcherProvider.main, block = {
            val cancellationPassengers = getCancellablePassengerUseCase.fetchCancellablePassenger(invoiceId)

            val selectedList = arrayListOf<FlightCancellationModel>()
            val cancellationList = arrayListOf<FlightCancellationModel>()
            val passengerRelations = hashMapOf<String, FlightCancellationPassengerModel>()

            for (cancellation in cancellationPassengers) {
                for (journey in flightCancellationJourneyList) {
                    if (cancellation.flightCancellationJourney.journeyId == journey.journeyId) {
                        val cancellationModel = FlightCancellationModel(invoiceId, journey, cancellation.passengerModelList)
                        cancellationList.add(cancellationModel)
                        selectedList.add(FlightCancellationModel(invoiceId, journey, arrayListOf()))

                        passengerRelations.putAll(buildPassengerRelationsMap(cancellationModel.passengerModelList))
                    }
                }
            }

            // sort journey by departure date
            if (cancellationList.size > 1) {
                val firstJourney = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        cancellationList[0].flightCancellationJourney.departureTime)
                val secondJourney = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        cancellationList[1].flightCancellationJourney.departureTime)

                if (firstJourney.after(secondJourney)) {
                    val temp: FlightCancellationModel = cancellationList[0]
                    cancellationList[0] = cancellationList[1]
                    cancellationList[1] = temp
                }
            }
            if (selectedList.size > 1) {
                val firstJourney = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        selectedList[0].flightCancellationJourney.departureTime)
                val secondJourney = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        selectedList[1].flightCancellationJourney.departureTime)

                if (firstJourney.after(secondJourney)) {
                    val temp: FlightCancellationModel = selectedList[0]
                    selectedList[0] = selectedList[1]
                    selectedList[1] = temp
                }
            }

            selectedCancellationPassengerList.clear()
            selectedCancellationPassengerList.addAll(selectedList)
            passengerRelationsMap.clear()
            passengerRelationsMap.putAll(passengerRelations)

            mutableCancellationPassengerList.postValue(cancellationList)
        }) {
            it.printStackTrace()
        }
    }

    fun checkPassenger(passengerModel: FlightCancellationPassengerModel, position: Int): Boolean {
        var shouldNotifyRelationChecked = false
        if (position >= 0 && !selectedCancellationPassengerList[position].passengerModelList.contains(passengerModel)) {
            selectedCancellationPassengerList[position].passengerModelList.add(passengerModel)
            if (passengerModel.relations.isNotEmpty()) {
                checkAllRelations(passengerModel)
                shouldNotifyRelationChecked = true
            }
        }
        return shouldNotifyRelationChecked
    }

    fun uncheckPassenger(passengerModel: FlightCancellationPassengerModel, position: Int) {
        if (position >= 0) {
            selectedCancellationPassengerList[position].passengerModelList.remove(passengerModel)

            if (passengerModel.relations.isNotEmpty()) {
                uncheckAllRelations(passengerModel)
            }
        }
    }

    fun isPassengerChecked(passengerModel: FlightCancellationPassengerModel): Boolean {
        for (cancellation in selectedCancellationPassengerList) {
            if (passengerModel in cancellation.passengerModelList) {
                return true
            } else if (passengerModel.statusString.isNotEmpty()) {
                return true
            }
        }
        return false
    }

    private fun checkAllRelations(passengerModel: FlightCancellationPassengerModel) {
        for (relationId in passengerModel.relations) {
            passengerRelationsMap[relationId]?.let {
                for (cancellation in selectedCancellationPassengerList) {
                    if (relationId.contains(cancellation.flightCancellationJourney.journeyId) &&
                            !cancellation.passengerModelList.contains(it)) {
                        cancellation.passengerModelList.add(it)
                    }
                }
            }
        }
    }

    private fun uncheckAllRelations(passengerModel: FlightCancellationPassengerModel) {
        for (relationId in passengerModel.relations) {
            passengerRelationsMap[relationId]?.let {
                for (cancellation in selectedCancellationPassengerList) {
                    cancellation.passengerModelList.remove(it)
                }
            }
        }
    }

    private fun buildPassengerRelationsMap(passengerList: List<FlightCancellationPassengerModel>): Map<String, FlightCancellationPassengerModel> {
        val passengerRelations = hashMapOf<String, FlightCancellationPassengerModel>()
        for (passenger in passengerList) {
            passengerRelations[passenger.relationId] = passenger
        }
        return passengerRelations
    }

}