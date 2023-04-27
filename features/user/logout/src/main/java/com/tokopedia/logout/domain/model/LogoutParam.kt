package com.tokopedia.logout.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class LogoutParam (
    @SerializedName("save_session")
    val saveSession: String = ""
): GqlParam
