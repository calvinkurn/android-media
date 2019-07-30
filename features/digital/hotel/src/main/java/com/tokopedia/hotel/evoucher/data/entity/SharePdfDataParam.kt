package com.tokopedia.hotel.evoucher.data.entity

import com.google.gson.annotations.SerializedName

class SharePdfDataParam(
        @SerializedName("template")
        val template: String = "order_success",
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("cancel_id")
        val cancelId: String = "",
        @SerializedName("email_target")
        val emailTarget: List<String> = arrayListOf(),
        @SerializedName("send_whatsapp")
        val sendWhatsapp: Boolean = true,
        @SerializedName("send_sms")
        val sendSms: Boolean = true)