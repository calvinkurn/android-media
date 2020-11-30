package com.tokopedia.oneclickcheckout.common.view.model.preference

data class PreferenceListResponseModel(
        var success: Int = 0,
        var profiles: List<ProfilesItemModel> = ArrayList(),
        var messages: List<String> = emptyList(),
        var maxProfile: Int = 0,
        var ticker: String? = null,
        var enableOccRevamp: Boolean = false
)

