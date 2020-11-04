package com.tokopedia.entertainment.pdp.data.redeem.redeemable


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EventRedeem(
    @SerializedName("data")
    @Expose
    val data: Data = Data(),
    @SerializedName("status")
    @Expose
    val status: String = ""
)

data class Data(
        @SerializedName("action")
        @Expose
        val action: List<Action> = emptyList(),
        @SerializedName("product")
        @Expose
        val product: Product = Product(),
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("schedule")
        @Expose
        val schedule: Schedule = Schedule(),
        @SerializedName("user")
        @Expose
        val user: User = User()
)

data class Product(
        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("created_at")
        @Expose
        val createdAt: String = "",
        @SerializedName("display_name")
        @Expose
        val displayName: String = "",
        @SerializedName("facility_group_id")
        @Expose
        val facilityGroupId: Int = 0,
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("image_app")
        @Expose
        val imageApp: String = "",
        @SerializedName("image_web")
        @Expose
        val imageWeb: String = "",
        @SerializedName("long_rich_desc")
        @Expose
        val longRichDesc: String = "",
        @SerializedName("max_end_date")
        @Expose
        val maxEndDate: Int = 0,
        @SerializedName("min_start_date")
        @Expose
        val minStartDate: Int = 0,
        @SerializedName("mrp")
        @Expose
        val mrp: Int = 0,
        @SerializedName("provider_id")
        @Expose
        val providerId: Int = 0,
        @SerializedName("provider_product_id")
        @Expose
        val providerProductId: String = "",
        @SerializedName("sales_price")
        @Expose
        val salesPrice: Int = 0,
        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",
        @SerializedName("thumbnail_app")
        @Expose
        val thumbnailApp: String = "",
        @SerializedName("thumbnail_web")
        @Expose
        val thumbnailWeb: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("tnc")
        @Expose
        val tnc: String = "",
        @SerializedName("updated_at")
        @Expose
        val updatedAt: String = "",
        @SerializedName("url")
        @Expose
        val url: String = ""
)

data class Schedule(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("show_data")
        @Expose
        val showData: String = ""
)

data class User(
        @SerializedName("name")
        @Expose
        val name: String = ""
)

data class Action(
        @SerializedName("border_color")
        @Expose
        val borderColor: String = "",
        @SerializedName("button_type")
        @Expose
        val buttonType: String = "",
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("text_color")
        @Expose
        val textColor: String = "",
        @SerializedName("ui_control")
        @Expose
        val uiControl: String = "",
        @SerializedName("value")
        @Expose
        val value: String = "",
        @SerializedName("weight")
        @Expose
        val weight: Int = 0,
        @SerializedName("url_params")
        @Expose
        val urlParams: UrlParams = UrlParams()

)

data class UrlParams(
        @SerializedName("app_url")
        @Expose
        val appUrl: String = "",
        @SerializedName("method")
        @Expose
        val method: String = ""
)