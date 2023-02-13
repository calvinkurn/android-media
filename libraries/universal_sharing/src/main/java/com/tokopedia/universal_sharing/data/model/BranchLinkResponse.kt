package com.tokopedia.universal_sharing.data.model

import com.google.gson.annotations.SerializedName

data class BranchLinkResponse(
        @SerializedName("\$android_deeplink_path")
        val android_deeplink: String
)