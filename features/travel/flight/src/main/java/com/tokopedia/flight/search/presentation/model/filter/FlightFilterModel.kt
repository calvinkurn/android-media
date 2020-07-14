package com.tokopedia.flight.search.presentation.model.filter

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel

/**
 * @author by furqan on 05/03/2020
 */
class FlightFilterModel() : Parcelable, Cloneable {

    var priceMin = 0
    var priceMax = Integer.MAX_VALUE
    var durationMin = 0
    var durationMax = Integer.MAX_VALUE
    var transitTypeList = mutableListOf<TransitEnum>()
    var airlineList = mutableListOf<String>()
    var departureTimeList = mutableListOf<DepartureTimeEnum>()
    var arrivalTimeList = mutableListOf<DepartureTimeEnum>()
    var refundableTypeList = mutableListOf<RefundableEnum>()
    var facilityList = mutableListOf<FlightFilterFacilityEnum>()
    var isHasFilter = false
    var isSpecialPrice = false
    var isBestPairing = false
    var isReturn = false
    var journeyId = ""

    constructor(parcel: Parcel) : this() {
        priceMin = parcel.readInt()
        priceMax = parcel.readInt()
        durationMin = parcel.readInt()
        durationMax = parcel.readInt()
        transitTypeList = mutableListOf()
        parcel.readList(transitTypeList, TransitEnum::class.java.classLoader)
        airlineList = parcel.createStringArrayList() ?: mutableListOf()
        departureTimeList = mutableListOf()
        parcel.readList(departureTimeList, DepartureTimeEnum::class.java.classLoader)
        arrivalTimeList = mutableListOf()
        parcel.readList(arrivalTimeList, DepartureTimeEnum::class.java.classLoader)
        refundableTypeList = mutableListOf()
        parcel.readList(refundableTypeList, RefundableEnum::class.java.classLoader)
        facilityList = mutableListOf()
        parcel.readList(facilityList, FlightFilterFacilityEnum::class.java.classLoader)
        isHasFilter = parcel.readByte() != 0.toByte()
        isSpecialPrice = parcel.readByte() != 0.toByte()
        isBestPairing = parcel.readByte() != 0.toByte()
        isReturn = parcel.readByte() != 0.toByte()
        journeyId = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(priceMin)
        parcel.writeInt(priceMax)
        parcel.writeInt(durationMin)
        parcel.writeInt(durationMax)
        parcel.writeList(transitTypeList)
        parcel.writeStringList(airlineList)
        parcel.writeList(departureTimeList)
        parcel.writeList(arrivalTimeList)
        parcel.writeList(refundableTypeList)
        parcel.writeList(facilityList)
        parcel.writeByte(if (isHasFilter) 1 else 0)
        parcel.writeByte(if (isSpecialPrice) 1 else 0)
        parcel.writeByte(if (isBestPairing) 1 else 0)
        parcel.writeByte(if (isReturn) 1 else 0)
        parcel.writeString(journeyId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightFilterModel> {
        override fun createFromParcel(parcel: Parcel): FlightFilterModel {
            return FlightFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightFilterModel?> {
            return arrayOfNulls(size)
        }
    }

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
                facilityList.size > 0
    }

    fun copy(): FlightFilterModel {
        val flightFilterModel = FlightFilterModel()
        flightFilterModel.apply {
            this.priceMax = priceMax
            this.priceMin = priceMin
            this.durationMax = durationMax
            this.durationMin = durationMin
            this.transitTypeList = getCopyOfTransitList()
            this.airlineList = getCopyOfAirlineList()
            this.departureTimeList = getCopyOfDepartureList()
            this.arrivalTimeList = getCopyOfArrivalList()
            this.facilityList = getCopyOfFacilityList()
            this.refundableTypeList = getCopyOfRefundableList()
            this.isSpecialPrice = isSpecialPrice
            this.isBestPairing = isBestPairing
            this.isReturn = isReturn
            this.journeyId = journeyId
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