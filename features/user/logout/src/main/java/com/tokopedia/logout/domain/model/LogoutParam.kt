package com.tokopedia.logout.domain.model

import com.google.gson.annotations.SerializedName

data class LogoutParam (
    @SerializedName("save_session")
    val saveSession: String = ""
)
