package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateEducationCategoryResponse(
    @SerializedName("categoryTree")
    val categoryTree: CategoryTree? = null
) {

    data class CategoryTree(

        @SerializedName("data")
        val data: CategoryTreeData? = null
    ) {

        data class CategoryTreeData(

            @SerializedName("categories")
            val categories: List<CategoriesItem?>? = null,

            @SerializedName("status")
            val status: String? = null
        ) {

            data class Icon(

                @SerializedName("url")
                val url: String? = null
            )

            data class CategoriesItem(

                @SerializedName("children")
                val children: List<ChildrenItem?>? = null,

                @SerializedName("icon")
                val icon: Icon? = null,

                @SerializedName("description")
                val description: String? = null,

                @SerializedName("id")
                val id: Long? = null,

                @SerializedName("title")
                val title: String? = null,

                @SerializedName("url")
                val url: String? = null
            ) {

                data class ChildrenItem(

                    @SerializedName("icon")
                    val icon: Icon? = null,

                    @SerializedName("description")
                    val description: String? = null,

                    @SerializedName("id")
                    val id: Long? = null,

                    @SerializedName("title")
                    var title: String? = null,

                    @SerializedName("url")
                    val url: String? = null,

                    var isSelected: Boolean = false
                )
            }
        }
    }
}
