package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

data class VoucherValidationPartialResponse(
    @SerializedName("VoucherValidationPartial")
    val voucherValidationPartial: VoucherValidationPartial = VoucherValidationPartial()
) {
    data class VoucherValidationPartial(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
    ) {
        data class Data(
            @SerializedName("total_available_quota")
            val totalAvailableQuota: Int = 0,
            @SerializedName("validation_date")
            val validationDate: List<ValidationDate> = listOf(),
            @SerializedName("validation_error")
            val validationError: ValidationError = ValidationError(),
            @SerializedName("validation_product")
            val validationProduct: List<ValidationProduct> = listOf()
        ) {
            data class ValidationError(
                @SerializedName("benefit_idr")
                val benefitIdr: String = "",
                @SerializedName("benefit_max")
                val benefitMax: String = "",
                @SerializedName("benefit_percent")
                val benefitPercent: String = "",
                @SerializedName("benefit_type")
                val benefitType: String = "",
                @SerializedName("code")
                val code: String = "",
                @SerializedName("coupon_name")
                val couponName: String = "",
                @SerializedName("coupon_type")
                val couponType: String = "",
                @SerializedName("date_end")
                val dateEnd: String = "",
                @SerializedName("date_start")
                val dateStart: String = "",
                @SerializedName("hour_end")
                val hourEnd: String = "",
                @SerializedName("hour_start")
                val hourStart: String = "",
                @SerializedName("image")
                val image: String = "",
                @SerializedName("image_square")
                val imageSquare: String = "",
                @SerializedName("is_public")
                val isPublic: String = "",
                @SerializedName("min_purchase")
                val minPurchase: String = "",
                @SerializedName("min_purchase_type")
                val minPurchaseType: String = "",
                @SerializedName("minimum_tier_level")
                val minimumTierLevel: String = "",
                @SerializedName("quota")
                val quota: String = ""
            )

            data class ValidationProduct(
                @SerializedName("is_eligible")
                val isEligible: Boolean = false,
                @SerializedName("is_variant")
                val isVariant: Boolean = false,
                @SerializedName("parent_product_id")
                val parentProductId: Long = 0,
                @SerializedName("reason")
                val reason: String = "",
                @SerializedName("variant")
                val variant: List<Variant> = listOf()
            ) {
                data class Variant(
                    @SerializedName("is_eligible")
                    val isEligible: Boolean = false,
                    @SerializedName("price")
                    val price: String = "",
                    @SerializedName("price_fmt")
                    val priceFmt: String = "",
                    @SerializedName("product_id")
                    val productId: Long = 0,
                    @SerializedName("product_name")
                    val productName: String = "",
                    @SerializedName("reason")
                    val reason: String = "",
                    @SerializedName("sku")
                    val sku: String = "",
                    @SerializedName("stock")
                    val stock: Int = 0
                )
            }

            data class ValidationDate(
                @SerializedName("date_end")
                val endDate: String = "",
                @SerializedName("date_start")
                val startDate: String = "",
                @SerializedName("hour_end")
                val endHour: String = "",
                @SerializedName("hour_start")
                val startHour: String = "",
                @SerializedName("total_live_time")
                val totalLiveTime: String = "",
                @SerializedName("available")
                val available: Boolean = false,
                @SerializedName("not_available_reason")
                val notAvailableReason: String = "",
                @SerializedName("type")
                val type: Int = 0
            )
        }

        data class Header(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("messages")
            val messages: List<String> = listOf(),
            @SerializedName("reason")
            val reason: String = ""
        )
    }
}
