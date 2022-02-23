package com.tokopedia.catalog.model.raw


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogComparisonProductsResponse(
    @Expose @SerializedName("catalogComparisonList")
    val catalogComparisonList: CatalogComparisonList?
) {
    data class CatalogComparisonList(
        @Expose @SerializedName("catalogComparisonList")
        var catalogComparisonList: List<CatalogComparison?>?,
        @Expose @SerializedName("header")
        val header: Header?
    ) {
        data class CatalogComparison(
            @Expose @SerializedName("brand")
            val brand: String?,
            @Expose @SerializedName("catalogImage")
            val catalogImage: List<CatalogImage?>?,
            @Expose @SerializedName("id")
            val id: String?,
            @Expose @SerializedName("marketPrice")
            val marketPrice: List<MarketPrice?>?,
            @Expose @SerializedName("name")
            val name: String?,
            @Expose @SerializedName("isActive")
            var isActive : Boolean? = true
        ) {
            data class CatalogImage(
                @Expose @SerializedName("imageUrl")
                val imageUrl: String?,
                @Expose @SerializedName("isPrimary")
                val isPrimary: Boolean?
            )

            data class MarketPrice(
                @Expose @SerializedName("date")
                val date: String?,
                @Expose @SerializedName("max")
                val max: Int?,
                @Expose @SerializedName("maxFmt")
                val maxFmt: String?,
                @Expose @SerializedName("min")
                val min: Int?,
                @Expose @SerializedName("minFmt")
                val minFmt: String?,
                @Expose @SerializedName("name")
                val name: String?
            )
        }

        data class Header(
            @Expose @SerializedName("code")
            val code: Int?,
            @Expose @SerializedName("message")
            val message: String?
        )
    }
}