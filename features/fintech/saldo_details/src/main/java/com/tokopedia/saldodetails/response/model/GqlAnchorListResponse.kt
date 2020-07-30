package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

class GqlAnchorListResponse(
        @SerializedName("title")
        var label: String? = null,

        @SerializedName("text_color")
        var color: String? = null,

        @SerializedName("show_dialog")
        var isShowDialog: Boolean = false,

        @SerializedName("dialog")
        var dialogInfo: GqlMclDialogResponse? = null
        ,
        @SerializedName("link")
        var link: String? = null
)
