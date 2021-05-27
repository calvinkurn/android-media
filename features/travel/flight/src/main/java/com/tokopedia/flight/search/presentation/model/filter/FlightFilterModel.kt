package com.tokopedia.flight.search.presentation.model.filter

import android.os.Parcelable
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.search.presentation.model.statistics.FlightSearchStatisticModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 05/03/2020
 */
@Parcelize
class FlightFilterModel(
        var priceMin: Int = 0,
        var priceMax: Int = Integer.MAX_VALUE,
        var durationMin: Int = 0,
        var durationMax: Int = Integer.MAX_VALUE,
        var transitTypeList: MutableList<TransitEnum> = mutableListOf<TransitEnum>(),
        var airlineList: MutableList<String> = mutableListOf<String>(),
        var departureTimeList: MutableList<DepartureTimeEnum> = mutableListOf<DepartureTimeEnum>(),
        var arrivalTimeList: MutableList<DepartureTimeEnum> = mutableListOf<DepartureTimeEnum>(),
        var refundableTypeList: MutableList<RefundableEnum> = mutableListOf<RefundableEnum>(),
        var facilityList: MutableList<FlightFilterFacilityEnum> = mutableListOf<FlightFilterFacilityEnum>(),
        var isHasFilter: Boolean = false,
        var isSpecialPrice: Boolean = false,
        var isBestPairing: Boolean = false,
        var isReturn: Boolean = false,
        var journeyId: String = "",
        var isSeatDistancing: Boolean = false,
        var isFreeRapidTest: Boolean = false,
        var canFilterSeatDistancing: Boolean = false,
        var canFilterFreeRapidTest: Boolean = false,
        var departureArrivalTime: String = ""
) : Parcelable, Cloneable {

    fun setHasFilter(searchStatisticModel: FlightSearchStatisticModel?) {
        val priceMinStat = searchStatisticModel?.minPrice ?: 0
        val priceMaxStat = searchStatisticModel?.maxPrice ?: Int.MAX_VALUE
        val durMinStat = searchStatisticModel?.minDuration ?: 0
        val durMaxStat = searchStatisticModel?.maxDuration ?: Int.MAX_VALUE

        isHasFilter = priceMin > priceMinStat ||
                priceMax < priceMaxStat ||
                durationMin > durMinStat ||
                durationMax < durMaxStat ||
                transitTypeList.size > 0 ||
                airlineList.size > 0 ||
                departureTimeList.size > 0 ||
                arrivalTimeList.size > 0 ||
                refundableTypeList.size > 0 ||
                facilityList.size > 0 ||
                (canFilterSeatDistancing && isSeatDistancing) ||
                (canFilterFreeRapidTest && isFreeRapidTest)
    }

    fun copy(): FlightFilterModel {
        val flightFilterModel = FlightFilterModel()
        flightFilterModel.let {
            this.priceMax = it.priceMax
            this.priceMin = it.priceMin
            this.durationMax = it.durationMax
            this.durationMin = it.durationMin
            this.transitTypeList = it.getCopyOfTransitList()
            this.airlineList = it.getCopyOfAirlineList()
            this.departureTimeList = it.getCopyOfDepartureList()
            this.arrivalTimeList = it.getCopyOfArrivalList()
            this.facilityList = it.getCopyOfFacilityList()
            this.refundableTypeList = it.getCopyOfRefundableList()
            this.isSpecialPrice = it.isSpecialPrice
            this.isBestPairing = it.isBestPairing
            this.isReturn = it.isReturn
            this.journeyId = it.journeyId
            this.isSeatDistancing = it.isSeatDistancing
            this.isFreeRapidTest = it.isFreeRapidTest
            this.canFilterSeatDistancing = it.canFilterSeatDistancing
            this.canFilterFreeRapidTest = it.canFilterFreeRapidTest
        }
        return flightFilterModel
    }

    private fun getCopyOfTransitList(): MutableList<TransitEnum> {
        val transitEnumList = mutableListOf<TransitEnum>()
        for (item in transitTypeList) {
            transitEnumList.add(item)
        }
        return transitEnumList
    }

    private fun getCopyOfDepartureList(): MutableList<DepartureTimeEnum> {
        val departureTimeEnumList = mutableListOf<DepartureTimeEnum>()
        for (item in departureTimeList) {
            departureTimeEnumList.add(item)
        }
        return departureTimeEnumList
    }

    private fun getCopyOfArrivalList(): MutableList<DepartureTimeEnum> {
        val arrivalTimeEnumList = mutableListOf<DepartureTimeEnum>()
        for (item in arrivalTimeEnumList) {
            arrivalTimeEnumList.add(item)
        }
        return arrivalTimeEnumList
    }

    private fun getCopyOfFacilityList(): MutableList<FlightFilterFacilityEnum> {
        val facilityEnumList = mutableListOf<FlightFilterFacilityEnum>()
        for (item in facilityList) {
            facilityEnumList.add(item)
        }
        return facilityEnumList
    }

    private fun getCopyOfAirlineList(): MutableList<String> {
        val airlineCopyList = mutableListOf<String>()
        for (item in airlineList) {
            airlineCopyList.add(item)
        }
        return airlineList
    }

    private fun getCopyOfRefundableList(): MutableList<RefundableEnum> {
        val refundableEnumList = mutableListOf<RefundableEnum>()
        for (item in refundableTypeList) {
            refundableEnumList.add(item)
        }
        return refundableEnumList
    }


}