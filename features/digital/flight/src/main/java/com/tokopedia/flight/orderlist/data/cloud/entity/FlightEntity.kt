package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightEntity(
        @SerializedName("email")
        @Expose
        val email: String,
        @SerializedName("phone")
        @Expose
        val phone: String,
        @SerializedName("invoice_uri")
        @Expose
        val invoiceUri: String,
        @SerializedName("eticket_uri")
        @Expose
        val eticketUri: String,
        @SerializedName("total_adult")
        @Expose
        val totalAdult: String,
        @SerializedName("total_adult_numeric")
        @Expose
        val totalAdultNumeric: Int,
        @SerializedName("total_child")
        @Expose
        val totalChild: String,
        @SerializedName("total_child_numeric")
        @Expose
        val totalChildNumeric: Int,
        @SerializedName("total_infant")
        @Expose
        val totalInfant: String,
        @SerializedName("total_infant_numeric")
        @Expose
        val totalInfantNumeric: Int,
        @SerializedName("currency")
        @Expose
        val currency: String,
        @SerializedName("pdf")
        @Expose
        val pdf: String,
        @SerializedName("journeys")
        @Expose
        val journeys: List<JourneyEntity>,
        @SerializedName("passengers")
        @Expose
        val passengers: List<PassengerEntity>,
        @SerializedName("payment")
        @Expose
        val payment: PaymentInfoEntity,
        @SerializedName("insurances")
        @Expose
        val insurances: List<FlightOrderInsuranceEntity>,
        @SerializedName("cancellations")
        @Expose
        val cancellations: List<CancellationEntity>,
        @SerializedName("contact_us_url")
        @Expose
        val contactUsUrl: String)
