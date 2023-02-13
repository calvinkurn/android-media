package com.tokopedia.affiliate.model.response
import com.google.gson.annotations.SerializedName

data class AffiliatePerformanceItemTypeListData(

    @field:SerializedName("getItemTypeList")
    val getItemTypeList: GetItemTypeList
)

data class Error(

    @field:SerializedName("ErrorType")
    val errorType: Int,

    @field:SerializedName("Message")
    val message: String,

    @field:SerializedName("CtaLink")
    val ctaLink: CtaLink,

    @field:SerializedName("CtaText")
    val ctaText: String
)

data class ItemTypesItem(

    @field:SerializedName("Order")
    val order: Int,

    @field:SerializedName("PageType")
    val pageType: String,

    @field:SerializedName("Name")
    val name: String,

    var isSelected: Boolean = false
)

data class GetItemTypeList(

    @field:SerializedName("Data")
    val data: Data
)

data class Data(

    @field:SerializedName("CtaLink")
    val ctaLink: CtaLink,

    @field:SerializedName("Error")
    val error: Error,

    @field:SerializedName("ItemTypes")
    val itemTypes: List<ItemTypesItem>,

    @field:SerializedName("CtaText")
    val ctaText: String
)

data class CtaLink(

    @field:SerializedName("IosURL")
    val iosURL: String,

    @field:SerializedName("DesktopURL")
    val desktopURL: String,

    @field:SerializedName("MobileURL")
    val mobileURL: String,

    @field:SerializedName("AndroidURL")
    val androidURL: String
)