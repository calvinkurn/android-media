package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedsFavoriteCta(
    @SerializedName("title_en")
    @Expose val titleEn: String = "",

    @SerializedName("title_id")
    @Expose val titleId: String = "",

    @SerializedName("subtitle_en")
    @Expose val subtitleEn: String = "",

    @SerializedName("subtitle_id")
    @Expose val subtitleId: String = ""
)
