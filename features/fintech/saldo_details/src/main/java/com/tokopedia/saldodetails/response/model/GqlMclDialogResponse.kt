package com.tokopedia.saldodetails.response.model

import com.google.gson.annotations.SerializedName

data class GqlMclDialogResponse(
        @SerializedName("dialog_title")
        var dialogTitle: String? = null,

        @SerializedName("dialog_body")
        var dialogBody: String? = null,

        @SerializedName("dialog_button_text")
        var dialogButtonText: String? = null,

        @SerializedName("dialog_button_link")
        var dialogButtonLink: String? = null
)