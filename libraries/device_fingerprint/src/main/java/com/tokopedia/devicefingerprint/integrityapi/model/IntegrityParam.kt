package com.tokopedia.devicefingerprint.integrityapi.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class IntegrityParam(
    @SerializedName("payload")
    var payload: String,
    @SerializedName("error")
    var error: String,
    @SerializedName("error_code")
    var errorCode: String,
    @SerializedName("event")
    var event: String
): GqlParam