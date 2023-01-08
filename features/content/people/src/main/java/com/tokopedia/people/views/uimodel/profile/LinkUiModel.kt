package com.tokopedia.people.views.uimodel.profile

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
data class LinkUiModel(
    val webLink: String,
    val appLink: String,
) {
    companion object {
        val Empty: LinkUiModel
            get() = LinkUiModel(
                webLink = "",
                appLink = "",
            )
    }
}
