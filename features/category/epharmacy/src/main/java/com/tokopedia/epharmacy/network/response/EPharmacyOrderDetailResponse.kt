package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse

data class EPharmacyOrderDetailResponse(
    @SerializedName("getConsultationOrderDetail")
    @Expose
    val getConsultationOrderDetail: GetConsultationOrderDetail?
) {
    data class OrderButtonData(
        val cta: List<GetConsultationOrderDetail.Cta?>?,
        val triDots: List<GetConsultationOrderDetail.Cta?>?
    )
    data class GetConsultationOrderDetail(
        @SerializedName("cta")
        @Expose
        val cta: List<Cta?>?,
        @SerializedName("data")
        @Expose
        val ePharmacyOrderData: EPharmacyOrderData?,
        @SerializedName("tri_dots")
        @Expose
        val triDots: List<Cta?>?,

        val orderButtonData: OrderButtonData? = OrderButtonData(cta, triDots)
    ) {
        data class Cta(
            @SerializedName("action_type")
            @Expose
            val actionType: String?,
            @SerializedName("app_url")
            @Expose
            val appUrl: String?,
            @SerializedName("label")
            @Expose
            val label: String?,
            @SerializedName("type")
            @Expose
            val type: String?,
            @SerializedName("variant_color")
            @Expose
            val variantColor: String?,
            @SerializedName("web_url")
            @Expose
            val webUrl: String?
        )

        data class EPharmacyOrderData(
            @SerializedName("consultation_data")
            @Expose
            val consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?,
            @SerializedName("consultation_source")
            @Expose
            val consultationSource: ConsultationSource?,
            @SerializedName("invoice_number")
            @Expose
            val invoiceNumber: String?,
            @SerializedName("invoice_url")
            @Expose
            val invoiceUrl: String?,
            @SerializedName("order_id")
            @Expose
            val orderId: Long?,
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
            val ticker: Ticker?
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
                @SerializedName("type")
                @Expose
                val type: Int?
            )
        }
    }
}
