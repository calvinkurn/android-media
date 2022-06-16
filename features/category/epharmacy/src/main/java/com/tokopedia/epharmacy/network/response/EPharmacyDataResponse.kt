package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.epharmacy.utils.GALLERY_IMAGE_VIEW_TYPE

data class EPharmacyDataResponse(
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
    val prescriptionImages: ArrayList<PrescriptionImage?>?,
    @SerializedName("products")
    val ePharmacyProducts: List<EPharmacyProduct?>?,
    @SerializedName("shop_id")
    val shopId: String?,
    @SerializedName("shop_type")
    val shopType: String?,
    @SerializedName("shop_name")
    val shopName: String?,
    @SerializedName("shop_location")
    val shopLocation: String?
)

data class EpharmacyButton(
    @SerializedName("key")
    val key: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String = "primary",
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
    var prescriptionId: String?,
    @SerializedName("reject_reason")
    val rejectReason: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("is_uploading")
    var isUploading: Boolean = false,
    @SerializedName("is_upload_success")
    var isUploadSuccess: Boolean = false,
    @SerializedName("is_deletable")
    var isDeletable: Boolean = false,
    @SerializedName("is_upload_failed")
    var isUploadFailed: Boolean = false,
    @SerializedName("view_type")
    var viewType: Int = GALLERY_IMAGE_VIEW_TYPE,
    @SerializedName("local_path")
    var localPath: String? = ""
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
    val quantity: Int?,
    @SerializedName("item_weight")
    val itemWeight: Int?,
    @SerializedName("shop_id")
    var shopId: String?,
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