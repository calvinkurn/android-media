package com.tokopedia.privacycenter.domain

import android.content.SharedPreferences
import com.tokopedia.abstraction.constant.TkpdCache
import javax.inject.Inject

class DarkModeUseCase @Inject constructor(
    private val preference: SharedPreferences
) {

    fun isDarkModeActivated(): Boolean {
        return preference.getBoolean(TkpdCache.Key.KEY_DARK_MODE, false)
    }

}
