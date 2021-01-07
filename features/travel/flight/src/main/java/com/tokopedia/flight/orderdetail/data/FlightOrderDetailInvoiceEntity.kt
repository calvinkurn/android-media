package com.tokopedia.flight.orderdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/11/2020
 */
class FlightOrderDetailInvoiceEntity(@SerializedName("data")
                                     @Expose
                                     val data: String = "") {
    class Response(@SerializedName("flightGetInvoice")
                   @Expose
                   val flightGetInvoice: FlightOrderDetailInvoiceEntity = FlightOrderDetailInvoiceEntity())
}