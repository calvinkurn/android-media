package com.tokopedia.home_component.usecase.thematic

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo

data class ThematicModel(
    val isShown: Boolean = false,
    val colorMode: String = COLOR_DEFAULT,
    val heightPercentage: Int = 0,
    val backgroundImageURL: String = "",
    val foregroundImageURL: String = "",
) {
    companion object {
        const val COLOR_DEFAULT = "default"
        const val COLOR_DARK = "dark"
        const val COLOR_LIGHT = "light"

        fun fromResponse(data: ThematicResponse.Thematic) = ThematicModel(
            isShown = data.isShown,
            colorMode = data.colorMode,
            heightPercentage = data.heightPercentage,
            backgroundImageURL = data.backgroundImageURL,
            foregroundImageURL = data.foregroundImageURL
        )
    }

    fun getActualHeightPx(context: Context) =
        DeviceScreenInfo.getScreenHeight(context) * heightPercentage / 100
}
