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
    @SerializedName(value = "products", alternate = ["productsInfo"])
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
    val isReUploadEnabled: Boolean = false,
    @SerializedName("error_msg")
    val errorMessage: String?,
    @SerializedName("checkoutId")
    val checkoutId: String?,
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
    @SerializedName("expired_at")
    val expiredAt: String?,
    @SerializedName(value = "prescription_id")
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
    var isDeletable: Boolean = true,
    @SerializedName("is_upload_failed")
    var isUploadFailed: Boolean = false,
    @SerializedName("local_path")
    var localPath: String? = "",
    @SerializedName(value  = "prescription_data" )
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
    @SerializedName(value = "is_ethical_drug", alternate = ["isEthicalDrug"])
    val isEthicalDrug: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName(value = "product_id", alternate = ["productId"])
    val productId: String?,
    @SerializedName(value = "product_image", alternate = ["productImage"])
    val productImage: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("item_weight")
    val itemWeight: Int?,
    @SerializedName(value = "shop_id", alternate = ["shopId"])
    var shopId: Long?,
    @SerializedName(value = "store_name", alternate = ["shopName"])
    var shopName: String?,
    @SerializedName(value = "store_location", alternate = ["shopLocation"])
    var shopLocation: String?,
    @SerializedName(value = "store_type", alternate = ["shopType"])
    var shopType: String?,
    @SerializedName(value = "products")
    val ePharmacyProducts: List<EPharmacyProduct?>?,
    @SerializedName(value = "divider")
    var divider: Boolean = true,
)