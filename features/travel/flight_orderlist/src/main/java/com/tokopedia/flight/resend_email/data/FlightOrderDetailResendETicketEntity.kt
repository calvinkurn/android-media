package com.tokopedia.flight.resend_email.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 12/11/2020
 */
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