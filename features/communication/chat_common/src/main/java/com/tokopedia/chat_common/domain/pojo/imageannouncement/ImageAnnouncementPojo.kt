package com.tokopedia.chat_common.domain.pojo.imageannouncement

import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 04/01/19
 */

class ImageAnnouncementPojo {

    @SerializedName("image_url")
    var imageUrl: String = ""

    @SerializedName("url")
    var url: String = ""

    @SerializedName("is_hide_banner")
    var isHideBanner: Boolean = false

    // Optional data below
    @SerializedName("campaign_label")
    var campaignLabel: String? = ""

    @SerializedName("wording_end_state")
    var endStateWording: String? = ""

    @SerializedName("is_campaign")
    var isCampaign: Boolean? = false

    @SerializedName("status_campaign")
    var statusCampaign: Int? = 1

    @SerializedName("start_date")
    var startDate: String? = ""

    @SerializedName("end_date")
    var endDate: String? = ""

    @SerializedName("broadcast_cta_url")
    var broadcastCtaUrl: String? = null

    @SerializedName("broadcast_cta_text")
    var broadcastCtaText: String? = null
}
