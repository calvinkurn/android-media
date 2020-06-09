package com.tokopedia.one.click.checkout.common.domain.model.preference

data class PreferenceListResponseModel (
        var success: Int? = null,
        var profiles: ArrayList<ProfilesItemModel>? = null,
        var messages: List<Any?>? = null,
        var maxProfile: Int = 0
)

