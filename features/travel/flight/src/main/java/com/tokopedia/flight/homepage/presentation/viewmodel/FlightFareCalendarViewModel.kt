package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.usecase.GetFlightFareUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FlightFareCalendarViewModel @Inject constructor(private val dispatcherProvider: CoroutineDispatchers,
                                                      private val getFlightFareUseCase: GetFlightFareUseCase)
    : BaseViewModel(dispatcherProvider.io) {

    val fareFlightCalendarData = MutableLiveData<List<FlightFareAttributes>>()

    private val departureFares = HashMap<String, Long>()
    private val returnFares = HashMap<String, Long>()

    fun getFareFlightCalendar(mapParam: HashMap<String, Any>,
                              minDate: Date, maxDate: Date,
                              isReturn: Boolean = false,
                              departureDate: String = ""
    ) {
        launchCatchError(block = {
            val attributes: ArrayList<FlightFareAttributes> = arrayListOf()
            getFlightFareUseCase.apply {
                params = mapParam
                this.minDate = minDate
                this.maxDate = maxDate
            }
            attributes.addAll(getFlightFareUseCase.executeOnBackground())

            if (isReturn) {
                val returnAttributes = getReturnFlightCalendar(mapParam)
                storeRoundTripCalendarFare(attributes, returnAttributes, departureDate)
            } else {
                fareFlightCalendarData.postValue(attributes)
            }

        }) {
            fareFlightCalendarData.postValue(arrayListOf())
        }
    }

    private suspend fun getReturnFlightCalendar(mapParam: HashMap<String, Any>): ArrayList<FlightFareAttributes> {
        val returnAttributes = ArrayList<FlightFareAttributes>()
        getFlightFareUseCase.apply {
            createReturnParam(mapParam)
        }
        returnAttributes.addAll(getFlightFareUseCase.executeOnBackground())
        return returnAttributes
    }

    private fun storeRoundTripCalendarFare(departureFareAttributes: ArrayList<FlightFareAttributes>,
                                           returnFareAttributes: ArrayList<FlightFareAttributes>,
                                           departureDate: String) {
        if (departureFareAttributes.isNotEmpty()) {
            departureFares.clear()
            departureFareAttributes.forEach {
                departureFares[it.dateFare] = it.cheapestPriceNumeric
            }
        }

        if (returnFareAttributes.isNotEmpty()) {
            returnFares.clear()
            returnFareAttributes.forEach {
                returnFares[it.dateFare] = it.cheapestPriceNumeric
            }
        }

        if (departureDate.isNotEmpty()) {
            calculateRoundTripFareCalendar(departureDate)
        }
    }

    fun calculateRoundTripFareCalendar(departureDate: String) {
        val dotFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("in", "id"))
        val attributes = arrayListOf<FlightFareAttributes>()

        val departureFareNumeric = departureFares[departureDate]
        departureFareNumeric?.let {
            returnFares.forEach { (date, price) ->
                val priceNumberic = ((price + departureFareNumeric) / 1000)

                attributes.add(FlightFareAttributes(dateFare = date,
                        cheapestPriceNumeric = priceNumberic,
                        displayedFare = dotFormat.format(priceNumberic)
                ))
            }
        }

        fareFlightCalendarData.postValue(attributes)
    }
}
