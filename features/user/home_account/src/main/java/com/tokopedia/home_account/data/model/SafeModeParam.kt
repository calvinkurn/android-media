package com.tokopedia.home_account.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SafeModeParam(
    @SerializedName("safeMode")
    var safeMode: Boolean
): GqlParam
