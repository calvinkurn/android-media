package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlBoxInfoResponse(

        @SerializedName("box_title")
        var boxTitle: String? = null,

        @SerializedName("box_desc")
        var boxDesc: String? = null,

        @SerializedName("box_bg_color")
        var boxBgColor: String? = null,

        @SerializedName("link_text")
        var linkText: String? = null,

        @SerializedName("link_url")
        var linkUrl: String? = null,

        @SerializedName("link_text_color")
        var linkTextColor: String? = null
)
