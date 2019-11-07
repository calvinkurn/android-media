package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightOrderInsuranceEntity(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("paid_amount")
        @Expose
        val paidAmount: String = "",
        @SerializedName("paid_amount_numeric")
        @Expose
        val paidAmountNumeric: Long = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("tagline")
        @Expose
        val tagline: String = "")
