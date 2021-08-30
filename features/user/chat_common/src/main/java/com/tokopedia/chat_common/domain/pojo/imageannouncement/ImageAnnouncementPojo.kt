package com.tokopedia.chat_common.domain.pojo.imageannouncement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 04/01/19
 */

class ImageAnnouncementPojo {

    @SerializedName("image_url")
    @Expose
    var imageUrl: String = ""
    @SerializedName("url")
    @Expose
    var url: String = ""
    @SerializedName("is_hide_banner")
    @Expose
    var isHideBanner: Boolean = false

    @SerializedName("wording_end_state")
    @Expose
    var wording: String = ""
    @SerializedName("is_campaign")
    @Expose
    var isCampaign: Boolean = false
    @SerializedName("status_campaign")
    @Expose
    var statusCampaign: Int = 1
    @SerializedName("start_date")
    @Expose
    var startDate: String = ""
    @SerializedName("end_date")
    @Expose
    var endDate: String = ""
}
