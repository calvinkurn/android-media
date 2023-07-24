package com.tokopedia.product.detail.postatc.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.postatc.data.model.PostAtcComponentData as Data

data class PostAtcLayout(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("basicInfo")
    @Expose
    val basicInfo: BasicInfo = BasicInfo(),

    @SerializedName("postATCInfo")
    @Expose
    val postAtcInfo: ProductPostAtcInfo = ProductPostAtcInfo(),

    @SerializedName("components")
    @Expose
    val components: List<Component> = emptyList()
) {

    data class BasicInfo(
        @SerializedName("shopID")
        @Expose
        val shopId: String = "",

        @SerializedName("category")
        val category: Category = Category(),

        @SerializedName("price")
        val price: Double = 0.0,

        @SerializedName("originalPrice")
        val originalPrice: Double = 0.0,

        @SerializedName("condition")
        val condition: String = ""
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

    data class ProductPostAtcInfo(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subTitle")
        @Expose
        val subtitle: String = "",
        @SerializedName("image")
        @Expose
        val image: String = "",
        @SerializedName("button")
        @Expose
        val button: Button = Button()
    ) {
        data class Button(
            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("cartID")
            @Expose
            val cartId: String = ""
        )
    }
}

data class PostAtcLayoutResponse(
    @SerializedName("pdpGetPostATCLayout")
    @Expose
    val postAtcLayout: PostAtcLayout = PostAtcLayout()
)
