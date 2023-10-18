package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class GetCategoryListResponse(
    @Expose
    @SerializedName("TokonowCategoryTree")
    val response: CategoryListResponse = CategoryListResponse()
) {
    data class CategoryListResponse(
        @Expose
        @SerializedName("header")
        val header: Header = Header(),
        @Expose
        @SerializedName("data")
        val data: List<CategoryResponse> = listOf()
    ) {
        data class CategoryResponse(
            @Expose
            @SerializedName("id")
            val id: String = "",
            @Expose
            @SerializedName("name")
            val name: String = "",
            @Expose
            @SerializedName("url")
            val url: String = "",
            @Expose
            @SerializedName("isAdult")
            val isAdult: Int = 0,
            @Expose
            @SerializedName("applinks")
            val appLinks: String = "",
            @Expose
            @SerializedName("imageUrl")
            val imageUrl: String? = null,
            @Expose
            @SerializedName("parentID")
            val parentId: String? = null,
            @Expose
            @SerializedName("color")
            val color: String = "",
            @Expose
            @SerializedName("child")
            val childList: List<CategoryResponse>? = null
        )
    }
}
