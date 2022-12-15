package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedBanner(
    @SerializedName("img_url")
    @Expose val imgUrl: String = "",

    @SerializedName("click_url")
    @Expose val clickUrl: String = "",

    @SerializedName("click_applink")
    @Expose val clickApplink: String = "",

    @SerializedName("width_to_height_ratio")
    @Expose val widthToHeightRatio: Float = 0f
)
