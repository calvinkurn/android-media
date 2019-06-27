package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.google.gson.annotations.SerializedName

data class SettingSections(
        @SerializedName("list_settings")
        var listSettings: List<ParentSetting?> = emptyList(),
        @SerializedName("section")
        var section: String = ""
)