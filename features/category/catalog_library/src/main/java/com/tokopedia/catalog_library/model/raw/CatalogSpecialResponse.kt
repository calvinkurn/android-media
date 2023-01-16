package com.tokopedia.catalog_library.model.raw

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogSpecialResponse(
    @SerializedName("catalogCategorySpecial")
    @Expose
    val catalogCategorySpecial: CatalogCategorySpecial = CatalogCategorySpecial()
) {
    data class CatalogCategorySpecial(
        @SerializedName("header")
        @Expose
        val header: CatalogSpecialHeader = CatalogSpecialHeader(),

        @SerializedName("data")
        @Expose
        val catalogSpecialDataList: ArrayList<CatalogSpecialData>? = arrayListOf()
    ) {
        data class CatalogSpecialHeader(
            @SerializedName("code")
            @Expose
            val code: Int? = 0,

            @SerializedName("message")
            @Expose
            val message: String? = ""
        )
        data class CatalogSpecialData(
            @SerializedName("id")
            @Expose
            val id: Long? = 0,
            @SerializedName("name")
            @Expose
            val name: String? = "",
            @SerializedName("iconUrl")
            @Expose
            val iconUrl: String? = "",
            @SerializedName("categoryUrl")
            @Expose
            val categoryUrl: String? = "",
            @SerializedName("categoryIdentifier")
            @Expose
            val categoryIdentifier: String? = ""
        )
    }
}
