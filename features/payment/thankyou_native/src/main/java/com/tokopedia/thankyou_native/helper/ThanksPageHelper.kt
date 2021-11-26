package com.tokopedia.thankyou_native.helper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.tokopedia.topads.sdk.utils.*

object ThanksPageHelper {


    private val COPY_BOARD_LABEL = "Tokopedia"

    fun copyTOClipBoard(context: Context, dataStr: String) {
        try {
            val extraSpaceRegexStr = "\\s+".toRegex()
            val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                    as ClipboardManager
            val clip = ClipData.newPlainText(
                COPY_BOARD_LABEL,
                dataStr.replace(extraSpaceRegexStr, "")
            )
            clipboard.setPrimaryClip(clip)
        } catch (e: Exception) {
        }
    }

    fun getHeadlineAdsParam(topadsHeadLinePage: Int, userId: String, src: String): String {
        return UrlParamHelper.generateUrlParamString(
            mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to topadsHeadLinePage,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to src,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userId,
            )
        )
    }

}