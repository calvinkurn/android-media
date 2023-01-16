package com.tokopedia.catalog_library.model.raw

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogLibraryResponse(
    @SerializedName("categoryListCatalogLibraryPage")
    @Expose
    val categoryList: CategoryListLibraryPage = CategoryListLibraryPage()
) {
    data class CategoryListLibraryPage(
        @SerializedName("header")
        @Expose
        val header: CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader = CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialHeader(),

        @SerializedName("CategoryList")
        @Expose
        val categoryDataList: ArrayList<CategoryData>? = arrayListOf()
    ) {
        data class CategoryData(
            @SerializedName("RootCategoryId")
            @Expose
            val rootCategoryId: String? = "",

            @SerializedName("RootCategoryName")
            @Expose
            val rootCategoryName: String? = "",

            @SerializedName("ChildCategoryList")
            @Expose
            val childCategoryList: ArrayList<ChildCategoryList>? = arrayListOf()
        ) {
            data class ChildCategoryList(
                @SerializedName("CategoryID")
                @Expose
                val categoryId: String? = "",

                @SerializedName("CategoryName")
                @Expose
                val categoryName: String? = "",

                @SerializedName("CategoryURL")
                @Expose
                val categoryUrl: String? = "",

                @SerializedName("CategoryIconURL")
                @Expose
                val categoryIconUrl: String? = "",

                @SerializedName("CategoryIdentifier")
                @Expose
                val categoryIdentifier: String? = ""
            )
        }
    }
}
