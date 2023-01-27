package com.tokopedia.sellerpersona.common

import android.content.Context
import com.tokopedia.kotlin.extensions.view.getResColor

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

object Utils {

    fun getHexColor(context: Context, resColor: Int): String {
        val hexColor = Integer.toHexString(context.getResColor(resColor))
        val startIndex = 2
        return if (hexColor.length > startIndex) {
            "#" + hexColor.substring(startIndex)
        } else {
            "#$hexColor"
        }
    }
}