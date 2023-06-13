package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val KEY_TEMPLATE_ID = "template_id"
private const val KEY_NAME = "name"
private const val KEY_CPM_IMAGE = "image"
private const val KEY_BADGES = "badges"
private const val KEY_PROMOTED_TEXT = "promoted_text"
private const val KEY_DESCRIPTION = "description"
private const val KEY_URI = "uri"
private const val KEY_SHOP = "shop"
private const val KEY_CTA_TEXT = "button_text"
private const val KEY_LAYOUT = "layout"
private const val KEY_POSITION = "position"
private const val KEY_WIDGET_TITLE = "widget_title"
private const val KEY_WIDGET_IMAGE_URL = "widget_image_url"
private const val FLASH_SALE_CAMPAIGN_DETAIL = "flash_sale_campaign_detail"

@Parcelize
data class Cpm(
    @SerializedName(KEY_TEMPLATE_ID)
    var templateId: Int = 0,

    @SerializedName(KEY_NAME)
    var name: String = "",

    @SerializedName(KEY_CPM_IMAGE)
    var cpmImage: CpmImage = CpmImage(),

    @SerializedName(KEY_BADGES)
    var badges: MutableList<Badge> = ArrayList(),

    @SerializedName(KEY_PROMOTED_TEXT)
    var promotedText: String = "",

    @SerializedName(KEY_URI)
    var uri: String = "",

    @SerializedName(KEY_DESCRIPTION)
    var decription: String = "",

    @SerializedName(KEY_SHOP)
    var cpmShop: CpmShop = CpmShop(),

    @SerializedName(KEY_CTA_TEXT)
    var cta: String = "",

    @SerializedName(KEY_LAYOUT)
    var layout: Int = 0,

    @SerializedName(KEY_POSITION)
    var position: Int = 0,

    @SerializedName(KEY_WIDGET_TITLE)
    var widgetTitle: String = "",

    @SerializedName(KEY_WIDGET_IMAGE_URL)
    var widgetImageUrl: String = "",

    @SerializedName(FLASH_SALE_CAMPAIGN_DETAIL)
    var flashSaleCampaignDetail: FlashSaleCampaignDetail = FlashSaleCampaignDetail()
) : Parcelable
