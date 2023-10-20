package com.scp.auth.registerpushnotif.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RegisterPushNotificationParamsModel(
    @SerializedName("publicKey")
    var publicKey: String = "",
    @SerializedName("signature")
    var signature: String = "",
    @SerializedName("datetime")
    var datetime: String = ""
): GqlParam
