package com.tokopedia.flight.searchV4.data

import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchErrorEntity

/**
 * @author by furqan on 08/04/2020
 */
class FlightSearchThrowable : Throwable() {
    var errorList: List<FlightSearchErrorEntity> = arrayListOf()
}