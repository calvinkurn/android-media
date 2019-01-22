package com.tokopedia.flight.search.data.db.mapper

import android.text.TextUtils
import com.google.gson.Gson
import com.tokopedia.flight.search.data.api.single.response.FlightSearchData
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.flight_dbflow.FlightSearchReturnRouteDB
import com.tokopedia.flight_dbflow.FlightSearchSingleRouteDB

/**
 * Created by Rizky on 25/10/18.
 */
class FlightSearchDataMapper {

    fun mapToFlightSearchSingleRouteDB(flightSearchData: FlightSearchData): FlightSearchSingleRouteDB {
        val gson = Gson()
        val attributes = flightSearchData.attributes
        val routeList = attributes.routes
        val routes = gson.toJson(routeList)
        val fare = attributes.fare
        var airline = ""
        var refundableCount = 0
        var i = 0
        val sizei = routeList.size
        while (i < sizei) {
            if (routeList[i].refundable!!) {
                refundableCount++
            }
            val airlineID = routeList[i].airline
            if (!TextUtils.isEmpty(airlineID) && !airline.contains(airlineID)) {
                if (!TextUtils.isEmpty(airline)) {
                    airline += "-"
                }
                airline += routeList[i].airline
            }
            i++
        }
        val isRefundable = when (refundableCount) {
            routeList.size -> RefundableEnum.REFUNDABLE.id
            0 -> RefundableEnum.NOT_REFUNDABLE.id
            else -> RefundableEnum.PARTIAL_REFUNDABLE.id
        }

        return FlightSearchSingleRouteDB(
                flightSearchData.attributes.term,
                flightSearchData.id,
                flightSearchData.flightType,
                attributes.aid,
                attributes.departureAirport,
                attributes.departureTime,
                attributes.departureTimeInt,
                attributes.arrivalAirport,
                attributes.arrivalTime,
                attributes.arrivalTimeInt,
                attributes.totalTransit + attributes.totalStop,
                attributes.addDayArrival,
                attributes.duration,
                attributes.durationMinute,
                attributes.total,
                attributes.totalNumeric,
                attributes.beforeTotal,
                routes,
                fare.adult,
                fare.adultNumeric,
                fare.child,
                fare.childNumeric,
                fare.infant,
                fare.infantNumeric,
                airline,
                isRefundable)
    }

    fun mapToFlightSearchReturnRouteDB(flightSearchData: FlightSearchData): FlightSearchReturnRouteDB {
        val gson = Gson()
        val attributes = flightSearchData.attributes
        val routeList = attributes.routes
        val routes = gson.toJson(routeList)
        val fare = attributes.fare
        var airline = ""
        var refundableCount = 0
        var i = 0
        val sizei = routeList.size
        while (i < sizei) {
            if (routeList[i].refundable!!) {
                refundableCount++
            }
            val airlineID = routeList[i].airline
            if (!TextUtils.isEmpty(airlineID) && !airline.contains(airlineID)) {
                if (!TextUtils.isEmpty(airline)) {
                    airline += "-"
                }
                airline += routeList[i].airline
            }
            i++
        }
        val isRefundable = when (refundableCount) {
            routeList.size -> RefundableEnum.REFUNDABLE.id
            0 -> RefundableEnum.NOT_REFUNDABLE.id
            else -> RefundableEnum.PARTIAL_REFUNDABLE.id
        }

        return FlightSearchReturnRouteDB(
                flightSearchData.attributes.term,
                flightSearchData.id,
                flightSearchData.flightType,
                attributes.aid,
                attributes.departureAirport,
                attributes.departureTime,
                attributes.departureTimeInt,
                attributes.arrivalAirport,
                attributes.arrivalTime,
                attributes.arrivalTimeInt,
                attributes.totalTransit + attributes.totalStop,
                attributes.addDayArrival,
                attributes.duration,
                attributes.durationMinute,
                attributes.total,
                attributes.totalNumeric,
                attributes.beforeTotal,
                routes,
                fare.adult,
                fare.adultNumeric,
                fare.child,
                fare.childNumeric,
                fare.infant,
                fare.infantNumeric,
                airline,
                isRefundable)
    }

}