package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class EPharmacyDataResponse(
    @SerializedName("epharmacy_button")
    val epharmacyButton: EpharmacyButton?,
    @SerializedName("epharmacy_ticker")
    val epharmacyTicker: EpharmacyTicker?,
    @SerializedName("invoice_ref_num")
    val invoiceRefNum: String?,
    @SerializedName("order_pdf")
    val orderPdf: String?,
    @SerializedName("prescription_status")
    val prescriptionStatus: String?,
    @SerializedName("payment_date")
    val paymentDate: String?,
    @SerializedName(value = "prescription_images", alternate = ["prescriptions"])
    val prescriptionImages: ArrayList<PrescriptionImage?>?,
    @SerializedName("products")
    val ePharmacyProducts: List<EPharmacyProduct?>?,
    @SerializedName("shop_id")
    val shopId: Long?,
    @SerializedName("shop_type")
    val shopType: String?,
    @SerializedName("shop_name")
    val shopName: String?,
    @SerializedName("shop_location")
    val shopLocation: String?,
    @SerializedName("is_reupload_enabled")
    val isReUploadEnabled: Boolean?
)

data class EpharmacyButton(
    @SerializedName("key")
    var key: String?,
    @SerializedName("text")
    var text: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("applink")
    val appLink: String?,
    @SerializedName("desktop_url")
    val desktopUrl: String?
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
    var prescriptionId: Long?,
    @SerializedName("reject_reason")
    val rejectReason: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("is_uploading")
    var isUploading: Boolean = false,
    @SerializedName("is_upload_success")
    var isUploadSuccess: Boolean = true,
    @SerializedName("is_deletable")
    var isDeletable: Boolean = false,
    @SerializedName("is_upload_failed")
    var isUploadFailed: Boolean = false,
    @SerializedName("local_path")
    var localPath: String? = "",
    @SerializedName("prescription_data")
    var prescriptionData: PrescriptionData?
){
    data class PrescriptionData(
        @SerializedName("format")
        var format:String?,
        @SerializedName("value")
        var value: String?
    )
}

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
    val quantity: Int?,
    @SerializedName("item_weight")
    val itemWeight: Int?,
    @SerializedName("shop_id")
    var shopId: Long?,
    @SerializedName("store_name")
    var shopName: String?,
    @SerializedName("store_location")
    var shopLocation: String?,
    @SerializedName("store_type")
    var shopType: String?,
)

data class Header(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("server_prosess_time")
    val serverProsessTime: String?
)