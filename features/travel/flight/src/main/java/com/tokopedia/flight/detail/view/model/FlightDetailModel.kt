package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightDetailModel(var id: String = "",
                             var term: String = "",
                             var departureAirport: String = "",
                             var departureAirportCity: String = "",
                             var departureTime: String = "",
                             var arrivalAirport: String = "",
                             var arrivalAirportCity: String = "",
                             var arrivalTime: String = "",
                             var totalTransit: Int = 0,
                             var total: String = "",
                             var totalNumeric: Int = 0,
                             var beforeTotal: String = "",
                             var isRefundable: RefundableEnum = RefundableEnum.NOT_REFUNDABLE,
                             var adultNumericPrice: Int = 0,
                             var childNumericPrice: Int = 0,
                             var infantNumericPrice: Int = 0,
                             var countAdult: Int = 0,
                             var countChild: Int = 0,
                             var countInfant: Int = 0,
                             var routeList: List<FlightDetailRouteModel> = arrayListOf(),
                             var airlineDataList: List<FlightAirlineModel> = arrayListOf(),
                             var flightClass: String = "") : Parcelable {

    fun build(flightJourneyModel: FlightJourneyModel?): FlightDetailModel? =
            flightJourneyModel?.let {
                id = it.id
                term = it.term
                departureAirport = it.departureAirport
                departureAirportCity = it.departureAirportCity
                arrivalAirport = it.arrivalAirport
                arrivalAirportCity = it.arrivalAirportCity
                totalTransit = it.totalTransit
                beforeTotal = it.beforeTotal
                isRefundable = when (it.isRefundable) {
                    RefundableEnum.REFUNDABLE -> RefundableEnum.REFUNDABLE
                    RefundableEnum.PARTIAL_REFUNDABLE -> RefundableEnum.PARTIAL_REFUNDABLE
                    else -> RefundableEnum.NOT_REFUNDABLE
                }
                total = it.total
                totalNumeric = it.totalNumeric
                adultNumericPrice = it.fare.adultNumeric
                childNumericPrice = it.fare.childNumeric
                infantNumericPrice = it.fare.infantNumeric
                departureTime = it.departureTime
                arrivalTime = it.arrivalTime
                airlineDataList = it.airlineDataList

                val flightDetailRouteInfoModelMapper = FlightDetailRouteInfoModelMapper()
                val flightDetailRouteModelMapper = FlightDetailRouteModelMapper(flightDetailRouteInfoModelMapper)
                routeList = flightDetailRouteModelMapper.transform(it.routeList, it.totalTransit)

                this
            }

    fun build(flightSearchPassDataModel: FlightSearchPassDataModel): FlightDetailModel {
        countAdult = flightSearchPassDataModel.flightPassengerModel.adult
        countChild = flightSearchPassDataModel.flightPassengerModel.children
        countInfant = flightSearchPassDataModel.flightPassengerModel.infant
        flightClass = flightSearchPassDataModel.flightClass.title
        return this
    }

}