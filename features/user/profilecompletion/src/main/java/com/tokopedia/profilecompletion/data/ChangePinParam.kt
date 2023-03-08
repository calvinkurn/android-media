package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ChangePinParam (
    @SerializedName("pin")
    val pin: String = "",

    @SerializedName("pinConfirm")
    val pinConfirm: String = "",

    @SerializedName("pinOld")
    val pinOld: String = ""
) : GqlParam

