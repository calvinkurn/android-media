package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil.Companion.COLOR_DEFAULT

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
}
