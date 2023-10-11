package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil.COLOR_DARK
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil.COLOR_DEFAULT
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil.COLOR_LIGHT

/**
 * Created by frenzel
 */
data class HomeThematicModel(
    val isShown: Boolean = false,
    val colorMode: String = COLOR_DEFAULT,
    val heightPercentage: Int = 0,
    val backgroundImageURL: String = "" ,
    val foregroundImageURL: String = "",
) {
    fun getActualHeightPx(context: Context) = DeviceScreenInfo.getScreenHeight(context) * heightPercentage / 100

    fun shouldOverrideColor() = isShown && (colorMode == COLOR_DARK || colorMode == COLOR_LIGHT)

    companion object {
        const val PAYLOAD_CHANGE_TEXT_COLOR = "payloadChangeTextColor"
    }
}
