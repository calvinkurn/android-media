package com.tokopedia.entertainment.pdp.data.redeem.redeemable


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EventRedeem(
    @SerializedName("data")
    val data: Data = Data(),
    @SerializedName("status")
    val status: String = ""
)

data class Data(
    @SerializedName("action")
    val action: List<Action> = emptyList(),
    @SerializedName("product")
    val product: Product = Product(),
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("schedule")
    val schedule: Schedule = Schedule(),
    @SerializedName("user")
    val user: User = User(),
    @SerializedName("redemptions")
    val redemptions: List<Participant>? = emptyList(),
    @SerializedName("redemption_status")
    val redemptionStatus: Int = 0,
)

data class Product(
    @SerializedName("category_id")
    val categoryId: String = "0",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("display_name")
    val displayName: String = "",
    @SerializedName("facility_group_id")
    val facilityGroupId: String = "0",
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("image_app")
    val imageApp: String = "",
    @SerializedName("image_web")
    val imageWeb: String = "",
    @SerializedName("long_rich_desc")
    val longRichDesc: String = "",
    @SerializedName("max_end_date")
    val maxEndDate: Int = 0,
    @SerializedName("min_start_date")
    val minStartDate: Int = 0,
    @SerializedName("mrp")
    val mrp: Int = 0,
    @SerializedName("provider_id")
    val providerId: String = "0",
    @SerializedName("provider_product_id")
    val providerProductId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("sales_price")
    val salesPrice: Long = 0,
    @SerializedName("seo_url")
    val seoUrl: String = "",
    @SerializedName("thumbnail_app")
    val thumbnailApp: String = "",
    @SerializedName("thumbnail_web")
    val thumbnailWeb: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("tnc")
    val tnc: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = "",
    @SerializedName("url")
    val url: String = ""
)

data class Schedule(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("show_data")
    val showData: String = ""
)

data class User(
    @SerializedName("name")
    val name: String = ""
)

data class Action(
    @SerializedName("border_color")
    val borderColor: String = "",
    @SerializedName("button_type")
    val buttonType: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("text_color")
    val textColor: String = "",
    @SerializedName("ui_control")
    val uiControl: String = "",
    @SerializedName("value")
    val value: String = "",
    @SerializedName("weight")
    val weight: Int = 0,
    @SerializedName("url_params")
    val urlParams: UrlParams = UrlParams()

)

data class UrlParams(
    @SerializedName("app_url")
    val appUrl: String = "",
    @SerializedName("method")
    val method: String = ""
)

data class Participant(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("day")
    val day: Int = 0,
    @SerializedName("redemption_time")
    val redemptionTime: Int = 0,
    @SerializedName("participant_details")
    val participantDetails: List<ParticipantDetail> = emptyList(),
    var checked: Boolean = false
)

data class ParticipantDetail(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("value")
    val value: String = ""
)
