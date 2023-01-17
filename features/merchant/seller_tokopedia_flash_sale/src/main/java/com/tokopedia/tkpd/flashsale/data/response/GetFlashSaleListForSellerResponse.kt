package com.tokopedia.tkpd.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerResponse(
    @SerializedName("getFlashSaleListForSeller")
    val getFlashSaleListForSeller: GetFlashSaleListForSeller = GetFlashSaleListForSeller()
) {
    data class GetFlashSaleListForSeller(
        @SerializedName("campaign_list")
        val campaignList: List<Campaign> = listOf(),
        @SerializedName("total_campaign")
        val totalCampaign: Int = 0
    ) {
        data class Campaign(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("cancellation_reason")
            val cancellationReason: String = "",
            @SerializedName("cover_image")
            val coverImage: String = "",
            @SerializedName("description")
            val description: String = "",
            @SerializedName("end_date_unix")
            val endDateUnix: Long = 0,
            @SerializedName("max_product_submission")
            val maxProductSubmission: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("has_eligible_products")
            val hasEligibleProduct: Boolean = false,
            @SerializedName("product_meta")
            val productMeta: ProductMeta = ProductMeta(),
            @SerializedName("remaining_quota")
            val remainingQuota: Int = 0,
            @SerializedName("review_end_date_unix")
            val reviewEndDateUnix: Long = 0,
            @SerializedName("review_start_date_unix")
            val reviewStartDateUnix: Long = 0,
            @SerializedName("slug")
            val slug: String = "",
            @SerializedName("start_date_unix")
            val startDateUnix: Long = 0,
            @SerializedName("status_id")
            val statusId: String = "",
            @SerializedName("status_text")
            val statusText: String = "",
            @SerializedName("submission_end_date_unix")
            val submissionEndDateUnix: Long = 0,
            @SerializedName("submission_start_date_unix")
            val submissionStartDateUnix: Long = 0,
            @SerializedName("use_multilocation")
            val useMultilocation: Boolean = false,
            @SerializedName("product_criteria")
            val productCriteria: List<ProductCriteria> = listOf()
        ) {
            data class ProductMeta(
                @SerializedName("accepted_product")
                val acceptedProduct: Int = 0,
                @SerializedName("rejected_product")
                val rejectedProduct: Int = 0,
                @SerializedName("total_product")
                val totalProduct: Int = 0,
                @SerializedName("total_product_stock")
                val totalProductStock: Int = 0,
                @SerializedName("total_stock_sold")
                val totalStockSold: Int = 0,
                @SerializedName("transferred_product")
                val transferredProduct: Int = 0,
                @SerializedName("float_total_sold_value")
                val totalSoldValue: Double = 0.0
            )
            data class ProductCriteria(
                @SerializedName("criteria_id")
                val criteriaId: Long = 0,
                @SerializedName("min_price")
                val minPrice: Double = 0.0,
                @SerializedName("max_price")
                val maxPrice: Double = 0.0,
                @SerializedName("min_final_price")
                val minFinalPrice: Double = 0.0,
                @SerializedName("max_final_price")
                val maxFinalPrice: Double = 0.0,
                @SerializedName("min_discount")
                val minDiscount: Int = 0,
                @SerializedName("min_custom_stock")
                val minCustomStock: Int = 0,
                @SerializedName("max_custom_stock")
                val maxCustomStock: Int = 0,
                @SerializedName("min_rating")
                val minRating: Int = 0,
                @SerializedName("min_product_score")
                val minProductScore: Int = 0,
                @SerializedName("min_qty_sold")
                val minQuantitySold: Int = 0,
                @SerializedName("max_qty_sold")
                val maxQuantitySold: Int = 0,
                @SerializedName("max_submission")
                val maxSubmission: Int = 0,
                @SerializedName("max_product_appear")
                val maxProductAppear: Int = 0,
                @SerializedName("day_periode_time_appear")
                val dayPeriodTimeAppear: Int = 0,
                @SerializedName("categories")
                val categories: List<ProductCategories> = listOf(),
                @SerializedName("additional_info")
                val additionalInfo: AdditionalInfo = AdditionalInfo()
            )

            data class ProductCategories(
                @SerializedName("category_id")
                val categoryId: Long = 0,
                @SerializedName("category_name")
                val categoryName: String = ""
            )

            data class AdditionalInfo(
                @SerializedName("matched_product")
                val matchedProduct: Long = 0
            )
        }
    }
}
