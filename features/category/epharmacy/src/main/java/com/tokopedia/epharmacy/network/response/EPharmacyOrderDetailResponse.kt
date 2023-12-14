package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.unifycomponents.UnifyButton

data class EPharmacyOrderDetailResponse(
    @SerializedName("getConsultationOrderDetail")
    @Expose
    val getConsultationOrderDetail: GetConsultationOrderDetail?
) {
    data class GetConsultationOrderDetail(
        @SerializedName("data")
        @Expose
        val ePharmacyOrderData: EPharmacyOrderData?
    ) {

        data class EPharmacyOrderButtonModel(
            @SerializedName("action_type")
            @Expose
            val actionType: String?,
            @SerializedName("app_url")
            @Expose
            val appUrl: String?,
            @SerializedName("label")
            @Expose
            val label: String?,
            @SerializedName("caption")
            @Expose
            val caption: String?,
            @SerializedName("type")
            @Expose
            val type: String?,
            @SerializedName("variant_color")
            @Expose
            val variantColor: String?
        )

        data class EPharmacyOrderData(
            @SerializedName("consultation_data")
            @Expose
            val consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?,
            @SerializedName("consultation_source")
            @Expose
            val consultationSource: ConsultationSource?,
            @SerializedName("group_id")
            @Expose
            val groupId: String?,
            @SerializedName("invoice_number")
            @Expose
            val invoiceNumber: String?,
            @SerializedName("invoice_url")
            @Expose
            val invoiceUrl: String?,
            @SerializedName("order_id")
            @Expose
            val orderId: String?,
            @SerializedName("order_status")
            @Expose
            val orderStatus: String?,
            @SerializedName("order_status_desc")
            @Expose
            val orderStatusDesc: String?,
            @SerializedName("payment_amount")
            @Expose
            val paymentAmount: Int?,
            @SerializedName("payment_amount_str")
            @Expose
            val paymentAmountStr: String?,
            @SerializedName("item_price")
            @Expose
            val itemPrice: Double?,
            @SerializedName("item_price_str")
            @Expose
            val itemPriceStr: String?,
            @SerializedName("payment_method")
            @Expose
            val paymentMethod: String?,
            @SerializedName("order_indicator_color")
            @Expose
            val orderIndicatorColor: String?,
            @SerializedName("payment_date")
            @Expose
            val paymentDate: String?,
            @SerializedName("order_expired_date")
            @Expose
            val orderExpiredDate: String?,
            @SerializedName("ticker")
            @Expose
            val ticker: Ticker?,
            @SerializedName("cta")
            @Expose
            val cta: List<EPharmacyOrderButtonModel?>?,
            @SerializedName("tri_dots")
            @Expose
            val triDots: List<EPharmacyOrderButtonModel?>?,
            @SerializedName("help_button")
            @Expose
            val helpButton: EPharmacyOrderButtonModel?

        ) {

            data class ConsultationSource(
                @SerializedName("enabler_logo_url")
                @Expose
                val enablerLogoUrl: String?,
                @SerializedName("enabler_name")
                @Expose
                val enablerName: String?,
                @SerializedName("id")
                @Expose
                val id: String?,
                @SerializedName("service_name")
                @Expose
                val serviceName: String?,
                @SerializedName("note")
                @Expose
                val note: String?,
                @SerializedName("operating_schedule")
                @Expose
                val operatingSchedule: OperatingSchedule?,
                @SerializedName("price")
                @Expose
                val price: Double?,
                @SerializedName("price_str")
                @Expose
                val priceStr: String?,
                @SerializedName("pwa_link")
                @Expose
                val pwaLink: String?
            ) {
                data class OperatingSchedule(
                    @SerializedName("close_days")
                    @Expose
                    val closeDays: List<String?>?,
                    @SerializedName("daily")
                    @Expose
                    val daily: Daily?,
                    @SerializedName("duration")
                    @Expose
                    val duration: String?
                ) {
                    data class Daily(
                        @SerializedName("close_time")
                        @Expose
                        val closeTime: String?,
                        @SerializedName("open_time")
                        @Expose
                        val openTime: String?
                    )
                }
            }

            data class Ticker(
                @SerializedName("message")
                @Expose
                val message: String?,
                @SerializedName("type_int")
                @Expose
                val typeInt: Int?
            )
        }
    }
}

class OrderButtonData(
    val cta: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>?,
    val triDots: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>?,
    val orderTrackingData: OrderTrackingData?
) {
    companion object {
        private const val STRING_BUTTON_TYPE_ALTERNATE = "alternate"
        private const val STRING_BUTTON_TYPE_MAIN = "main"
        private const val STRING_BUTTON_TYPE_TRANSACTION = "transaction"

        private const val STRING_BUTTON_VARIANT_FILLED = "filled"
        private const val STRING_BUTTON_VARIANT_GHOST = "ghost"
        private const val STRING_BUTTON_VARIANT_TEXT_ONLY = "text_only"

        fun mapButtonType(typeString: String?): Int {
            return when (typeString) {
                STRING_BUTTON_TYPE_ALTERNATE -> UnifyButton.Type.ALTERNATE
                STRING_BUTTON_TYPE_MAIN -> UnifyButton.Type.MAIN
                STRING_BUTTON_TYPE_TRANSACTION -> UnifyButton.Type.TRANSACTION
                else -> UnifyButton.Type.MAIN
            }
        }

        fun mapButtonVariant(variantString: String?): Int {
            return when (variantString) {
                STRING_BUTTON_VARIANT_FILLED -> UnifyButton.Variant.FILLED
                STRING_BUTTON_VARIANT_GHOST -> UnifyButton.Variant.GHOST
                STRING_BUTTON_VARIANT_TEXT_ONLY -> UnifyButton.Variant.TEXT_ONLY
                else -> UnifyButton.Variant.FILLED
            }
        }
    }
}

class OrderTrackingData(
    private val enablerName: String?,
    private val ePharmacyGroupId: String?,
    private val tConsultationId: String?,
    private val orderId: String?,
    private val orderStatus: String?,
    private val tickerMessage: String?
) {
    override fun toString(): String {
        val label = "$enablerName - $ePharmacyGroupId - $tConsultationId - $orderId - $orderStatus"
        if (!tickerMessage.isNullOrBlank()) {
            return "$label - $tickerMessage"
        }
        return label
    }
}
