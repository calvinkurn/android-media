package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackLabelAndImage(
    @SerializedName("labelTitle")
    @Expose
    val labelTitle: String = "",
    @SerializedName("labelSubtitle")
    @Expose
    val labelSubtitle: String = "",
    @SerializedName("imageURL")
    @Expose
    val imageURL: String = "",
    @SerializedName("applink")
    @Expose
    val appLink: String = ""
)