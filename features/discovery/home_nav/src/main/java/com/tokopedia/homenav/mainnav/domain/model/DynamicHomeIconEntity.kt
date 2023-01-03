package com.tokopedia.homenav.mainnav.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 21/10/20.
 */

data class DynamicHomeIconEntity(
        @SerializedName("dynamicHomeIcon", alternate = ["getHomeCategoryV2"])
        val dynamicHomeIcon: DynamicHomeIcon
){
    data class DynamicHomeIcon(
            @SerializedName("categoryGroup", alternate = ["categories"])
            val categoryGroup: List<Category> = listOf()
    )

    data class Category(
            @SuppressLint("Invalid Data Type")
            @SerializedName("id")
            val id: Int = -1,
            @SerializedName("title")
            val name: String = "",
            @SerializedName("imageUrl")
            val imageUrl: String = "",
            @SerializedName("applink")
            val applink: String = "",
            @SerializedName("url")
            val url: String = "",
            @SerializedName ("categoryRows")
            val categoryRows: List<CategoryRow> = listOf()
    )

    data class CategoryRow(
            @SuppressLint("Invalid Data Type")
            @SerializedName("id")
            val id: Int = -1,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("imageUrl")
            val imageUrl: String = "",
            @SerializedName("applinks")
            val applink: String = "",
            @SerializedName("url")
            val url: String = ""
    )
}
