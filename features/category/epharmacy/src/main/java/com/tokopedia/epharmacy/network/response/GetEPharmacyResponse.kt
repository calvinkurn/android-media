package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class GetEPharmacyResponse(
    @SerializedName("data")
    val ePharmacyData: EPharmacyData?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("header")
    val header: Header?
) {
    data class EPharmacyData(
        @SerializedName("epharmacy_button")
        val epharmacyButton: List<EpharmacyButton?>?,
        @SerializedName("epharmacy_ticker")
        val epharmacyTicker: EpharmacyTicker?,
        @SerializedName("invoice_ref_num")
        val invoiceRefNum: String?,
        @SerializedName("order_pdf")
        val orderPdf: String?,
        @SerializedName("payment_date")
        val paymentDate: String?,
        @SerializedName("prescription_images")
        val prescriptionImages: List<PrescriptionImage?>?,
        @SerializedName("products")
        val ePharmacyProducts: List<EPharmacyProduct?>?,
        @SerializedName("shop_id")
        val shopId: String?,
        @SerializedName("shop_name")
        val shopName: String?
    ) {
        data class EpharmacyButton(
            @SerializedName("key")
            val key: String?,
            @SerializedName("text")
            val text: String?,
            @SerializedName("type")
            val type: String?,
            @SerializedName("uri")
            val uri: String?
        )

        data class EpharmacyTicker(
            @SerializedName("text")
            val text: String?
        )

        data class PrescriptionImage(
            @SerializedName("data")
            val `data`: String?,
            @SerializedName("expired_at")
            val expiredAt: String?,
            @SerializedName("prescription_id")
            val prescriptionId: String?,
            @SerializedName("reject_reason")
            val rejectReason: String?,
            @SerializedName("status")
            val status: String?
        )

        data class EPharmacyProduct(
            @SerializedName("is_ethical_drug")
            val isEthicalDrug: Boolean?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("product_id")
            val productId: Long?,
            @SerializedName("product_image")
            val productImage: String?,
            @SerializedName("quantity")
            val quantity: Int?
        )
    }

    data class Header(
        @SerializedName("code")
        val code: Int?,
        @SerializedName("server_prosess_time")
        val serverProsessTime: String?
    )
}