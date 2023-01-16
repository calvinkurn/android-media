package com.tokopedia.catalog_library.model.raw

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogListResponse(
    @SerializedName("catalogGetList")
    @Expose
    val catalogGetList: CatalogGetList = CatalogGetList()
) {
    data class CatalogGetList(
        @SerializedName("header")
        @Expose
        val header: CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader =
            CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),

        @SerializedName("catalogs")
        @Expose
        val catalogsProduct: ArrayList<CatalogsProduct> = arrayListOf(),

        @SerializedName("category_name")
        @Expose
        val categoryName: String = ""
    ) {
        data class CatalogsProduct(
            @SerializedName("id")
            @Expose
            val id: String? = "",
            @SerializedName("name")
            @Expose
            val name: String? = "",
            @SerializedName("categoryID")
            @Expose
            val categoryID: String? = "",
            @SerializedName("brand_id")
            @Expose
            val brandId: String? = "",
            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String? = "",
            @SerializedName("mobileUrl")
            @Expose
            val mobileUrl: String? = "",
            @SerializedName("applink")
            @Expose
            val applink: String? = "",
            @SerializedName("url")
            @Expose
            val url: String? = "",
            @SerializedName("brand")
            @Expose
            val brand: String? = "",
            @SerializedName("marketPrice")
            @Expose
            val marketPrice: CatalogListMarketPrice? = null,
            @SerializedName("rank")
            @Expose
            var rank: Int = 0
        ) {
            data class CatalogListMarketPrice(
                @SerializedName("min")
                @Expose
                val min: Long? = 0,
                @SerializedName("max")
                @Expose
                val max: Long? = 0,
                @SerializedName("minFmt")
                @Expose
                val minFmt: String? = "",
                @SerializedName("maxFmt")
                @Expose
                val maxFmt: String? = ""
            )
        }
    }
}
