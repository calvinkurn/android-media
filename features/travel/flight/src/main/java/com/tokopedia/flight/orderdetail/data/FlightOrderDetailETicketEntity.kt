package com.tokopedia.flight.orderdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/11/2020
 */
class FlightOrderDetailETicketEntity(@SerializedName("data")
                                     @Expose
                                     val data: String = "") {
    class Response(@SerializedName("flightGetETicket")
                   @Expose
                   val flightGetEticket: FlightOrderDetailETicketEntity = FlightOrderDetailETicketEntity())
}

class FlightOrderDetailResendETicketEntity(@SerializedName("meta")
                                           @Expose
                                           val meta: Meta = Meta()) {
    class Meta(@SerializedName("status")
               @Expose
               val status: String = "")

    class Response(@SerializedName("flightResendEmail")
                   @Expose
                   val flightResendEmail: FlightOrderDetailResendETicketEntity = FlightOrderDetailResendETicketEntity())
}