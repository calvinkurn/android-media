package com.tokopedia.gm.common.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.view.getResColor

/**
 * Created By @ilhamsuaib on 23/03/21
 */

object PMCommonUtils {

    fun getHexColor(context: Context, resColor: Int): String {
        val hexColor = Integer.toHexString(context.getResColor(resColor))
        val startIndex = 2
        return if (hexColor.length > startIndex) {
            "#" + hexColor.substring(startIndex)
        } else {
            "#$hexColor"
        }
    }

    fun getShopScoreFmt(shopScore: Int): String {
        return when {
            shopScore <= -1 -> "-"
            else -> shopScore.toString()
        }
    }

    fun getShopLevelFmt(shopLevel: Int): String {
        return getShopScoreFmt(shopLevel)
    }
}