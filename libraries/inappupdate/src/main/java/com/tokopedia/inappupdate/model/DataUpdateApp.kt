package com.tokopedia.inappupdate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataUpdateApp {
    @SerializedName("latest_version_force_update")
    @Expose
    var latestVersionForceUpdate = 0

    @SerializedName("latest_version_optional_update")
    @Expose
    var latestVersionOptionalUpdate = 0

    @SerializedName("link")
    @Expose
    var link: String = ""

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("message")
    @Expose
    var message: String = ""

    @SerializedName("is_force_enabled")
    @Expose
    var isIsForceEnabled = false

    @SerializedName("is_optional_enabled")
    @Expose
    var isIsOptionalEnabled = false

    @SerializedName("inapp_update_enabled")
    @Expose
    val isInappUpdateEnabled = false
}
