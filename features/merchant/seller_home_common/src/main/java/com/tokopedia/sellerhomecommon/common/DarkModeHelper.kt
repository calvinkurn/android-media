package com.tokopedia.sellerhomecommon.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.utils.view.DarkModeUtil
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 12/05/23.
 */

class DarkModeHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getUnifyHexColor(hexColor: String): String {
        return DarkModeUtil.getUnifyHexColor(context, hexColor)
    }

    fun makeHtmlDarkModeSupport(text: String): String {
        return DarkModeUtil.getHtmlTextDarkModeSupport(context, text)
    }
}