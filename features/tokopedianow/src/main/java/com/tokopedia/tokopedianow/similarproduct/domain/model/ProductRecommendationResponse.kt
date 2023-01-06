package com.tokopedia.tokopedianow.similarproduct.domain.model

import com.google.gson.annotations.SerializedName

data class ProductRecommendationResponse(

	@field:SerializedName("productRecommendationWidgetSingle")
	val productRecommendationWidgetSingle: ProductRecommendationWidgetSingle? = null
) {
    data class ProductRecommendationWidgetSingle(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("meta")
        val meta: Meta? = null
    ){

        data class Meta(

            @field:SerializedName("size")
            val size: Int? = null,

            @field:SerializedName("failSize")
            val failSize: Int? = null,

            @field:SerializedName("recommendation")
            val recommendation: String? = null,

            @field:SerializedName("experimentVersion")
            val experimentVersion: String? = null,

            @field:SerializedName("processTime")
            val processTime: String? = null
        )

        data class Data(

            @field:SerializedName("widgetUrl")
            val widgetUrl: String? = null,

            @field:SerializedName("recommendation")
            val recommendation: List<RecommendationItem?>? = null,

            @field:SerializedName("seeMoreUrlLink")
            val seeMoreUrlLink: String? = null,

            @field:SerializedName("source")
            val source: String? = null,

            @field:SerializedName("title")
            val title: String? = null,

            @field:SerializedName("foreignTitle")
            val foreignTitle: String? = null,

            @field:SerializedName("pageName")
            val pageName: String? = null,

            @field:SerializedName("tID")
            val tID: String? = null,

            @field:SerializedName("seeMoreAppLink")
            val seeMoreAppLink: String? = null
        ){

            data class RecommendationItem(

                @field:SerializedName("clickUrl")
                val clickUrl: String? = null,

                @field:SerializedName("trackerImageUrl")
                val trackerImageUrl: String? = null,

                @field:SerializedName("shop")
                val shop: Shop? = null,

                @field:SerializedName("relatedProductAppLink")
                val relatedProductAppLink: String? = null,

                @field:SerializedName("departmentId")
                val departmentId: Int? = null,

                @field:SerializedName("rating")
                val rating: Int? = null,

                @field:SerializedName("appUrl")
                val appUrl: String? = null,

                @field:SerializedName("isTopads")
                val isTopads: Boolean? = null,

                @field:SerializedName("url")
                val url: String? = null,

                @field:SerializedName("labels")
                val labels: List<Any?>? = null,

                @field:SerializedName("badges")
                val badges: List<BadgesItem?>? = null,

                @field:SerializedName("countReview")
                val countReview: Int? = null,

                @field:SerializedName("wishlistUrl")
                val wishlistUrl: String? = null,

                @field:SerializedName("price")
                val price: String? = null,

                @field:SerializedName("slashedPrice")
                val slashedPrice: String? = null,

                @field:SerializedName("discountPercentage")
                val discountPercentage: Float = 0F,

                @field:SerializedName("minOrder")
                val minOrder: Int = 0,

                @field:SerializedName("maxOrder")
                val maxOrder: Int = 0,

                @field:SerializedName("imageUrl")
                val imageUrl: String? = null,

                @field:SerializedName("name")
                val name: String? = null,

                @field:SerializedName("categoryBreadcrumbs")
                val categoryBreadcrumbs: String? = null,

                @field:SerializedName("id")
                val id: Long? = null,

                @field:SerializedName("relatedProductUrlLink")
                val relatedProductUrlLink: String? = null,

                @field:SerializedName("recommendationType")
                val recommendationType: String? = null,

                @field:SerializedName("stock")
                val stock: Int? = null,

                @field:SerializedName("wholesalePrice")
                val wholesalePrice: List<WholesalePriceItem?>? = null,

                @field:SerializedName("priceInt")
                val priceInt: Int? = null
            ){

                data class BadgesItem(

                    @field:SerializedName("imageUrl")
                    val imageUrl: String? = null,

                    @field:SerializedName("title")
                    val title: String? = null
                )

                data class WholesalePriceItem(
                    @SerializedName("price")
                    val price: Int,
                    @SerializedName("quantityMax")
                    val quantityMax: Int,
                    @SerializedName("quantityMin")
                    val quantityMin: Int,
                    @SerializedName("priceString")
                    val priceString: String
                )

                data class LabelsItem(

                    @field:SerializedName("color")
                    val color: String? = null,

                    @field:SerializedName("title")
                    val title: String? = null
                )

                data class Shop(

                    @field:SerializedName("city")
                    val city: String? = null,

                    @field:SerializedName("name")
                    val name: String? = null,

                    @field:SerializedName("id")
                    val id: Int? = null
                )

            }
        }
    }
}
