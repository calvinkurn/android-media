package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenAsset(
        @SerializedName("backgroundImgURL")
        val backgroundImgURL: String? = null,

        @SerializedName("glowImgURL")
        val glowImgURL: String? = null,

        @SerializedName("glowShadowImgURL")
        val glowShadowImgURL: String? = null,

        @SerializedName("imageURL")
        val imageURL: String? = null,

        @SerializedName("rewardImgURL")
        val rewardImgURL: String? = null,

        @SerializedName("imageV2URLs")
        val imageV2URLs: List<String>? = null,

        @SerializedName("seamlessImgURL")
        val seamlessImgURL: String? = null,

        @Expose(serialize = false, deserialize = false)
        val version: String = "1"
)