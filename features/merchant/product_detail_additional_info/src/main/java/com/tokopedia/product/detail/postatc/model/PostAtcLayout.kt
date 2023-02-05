package com.tokopedia.product.detail.postatc.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.postatc.model.PostAtcComponentData as Data

data class PostAtcLayout(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("basicInfo")
    @Expose
    val basicInfo: BasicInfo = BasicInfo(),

    @SerializedName("components")
    @Expose
    val components: List<Component> = emptyList()
) {

    data class BasicInfo(
        @SerializedName("shopID")
        @Expose
        val shopId: String = "",

        @SerializedName("category")
        val category: Category = Category()
    )

    data class Category(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = ""
    )

    data class Component(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("data")
        @Expose
        val data: List<Data> = emptyList()
    )
}

data class PostAtcLayoutResponse(
    @SerializedName("pdpGetPostATCLayout")
    @Expose
    val postAtcLayout: PostAtcLayout = PostAtcLayout()
)
