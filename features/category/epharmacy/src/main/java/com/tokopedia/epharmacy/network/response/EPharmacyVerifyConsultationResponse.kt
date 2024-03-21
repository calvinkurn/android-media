package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EPharmacyVerifyConsultationResponse(
    @SerializedName("verifyConsultationOrder")
    @Expose
    val verifyConsultationOrder: VerifyConsultationOrder?
) {
    data class VerifyConsultationOrder(
        @SerializedName("data")
        @Expose
        val verifyConsultationOrderData: VerifyConsultationOrderData?,
        @SerializedName("header")
        @Expose
        val header: VerifyConsultationOrderHeader?
    ) {
        data class VerifyConsultationOrderData(
            @SerializedName("is_order_created")
            @Expose
            val isOrderCreated: Boolean?,
            @SerializedName("pwa_link")
            @Expose
            val pwaLink: String?,
            @SerializedName("toko_consultation_id")
            @Expose
            val tokoConsultationId: String?
        )

        data class VerifyConsultationOrderHeader(
            @SerializedName("error_code")
            @Expose
            val errorCode: Int?,
            @SerializedName("error_message")
            @Expose
            val errorMessage: List<String?>?,
            @SerializedName("process_time")
            @Expose
            val processTime: Double?
        )
    }
}
