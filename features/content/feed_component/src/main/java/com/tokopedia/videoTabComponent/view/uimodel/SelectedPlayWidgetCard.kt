package com.tokopedia.videoTabComponent.view.uimodel

/**
 * Created By : Jonathan Darwin on June 08, 2022
 */
data class SelectedPlayWidgetCard(
    val channelId: String,
    val position: Int,
) {
    companion object {
        val Empty: SelectedPlayWidgetCard
            get() = SelectedPlayWidgetCard(
                channelId = "",
                position = -1,
            )
    }
}