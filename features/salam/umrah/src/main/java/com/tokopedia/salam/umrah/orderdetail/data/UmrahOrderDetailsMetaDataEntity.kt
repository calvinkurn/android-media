package com.tokopedia.salam.umrah.orderdetail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 14/10/2019
 */
class UmrahOrderDetailsMetaDataEntity (
        @SerializedName("travel_agent")
        @Expose
        val travelAgent: String = "",
        @SerializedName("booking_code")
        @Expose
        val bookingCode: String = "",
        @SerializedName("invoiceButton")
        @Expose
        val invoiceButton: Button = Button(),
        @SerializedName("productDetailButton")
        @Expose
        val productDetailButton: Button = Button(),
        @SerializedName("evoucherButton")
        @Expose
        val evoucherButton: Button = Button()
)

class Button(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("appURL")
        @Expose
        val appURL: String = "",
        @SerializedName("webURL")
        @Expose
        val webURL: String = ""
)