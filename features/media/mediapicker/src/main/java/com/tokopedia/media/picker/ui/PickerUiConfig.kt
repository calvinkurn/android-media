package com.tokopedia.media.picker.ui

import android.net.Uri
import com.tokopedia.applink.ApplinkConst.MediaPicker

object PickerUiConfig {

    var startPageIndex = 0
        private set

    fun getStartPageIndex(data: Uri) {
        val value = data.getQueryParameter(MediaPicker.PARAM_LANDING_PAGE)?: "0"
        startPageIndex = value.toInt()
    }

}