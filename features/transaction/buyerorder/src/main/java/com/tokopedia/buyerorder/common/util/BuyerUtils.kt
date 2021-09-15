package com.tokopedia.buyerorder.common.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.tokopedia.unifycomponents.ticker.Ticker
import java.util.regex.Pattern

object BuyerUtils {
    val VIBRATE_DURATION = 150

    @JvmStatic
    fun copyTextToClipBoard(label: String, textVoucherCode: String, context: Context) {
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, textVoucherCode)
        clipboard.setPrimaryClip(clip)
    }
    @JvmStatic
    fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(VIBRATE_DURATION.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(VIBRATE_DURATION.toLong())
        }
    }

    @JvmStatic
    fun getTickerType(typeStr: String): Int {
        return when (typeStr) {
            BuyerConsts.TICKER_TYPE_ERROR -> {
                Ticker.TYPE_ERROR
            }
            BuyerConsts.TICKER_TYPE_INFORMATION -> {
                Ticker.TYPE_INFORMATION
            }
            BuyerConsts.TICKER_TYPE_WARNING -> {
                Ticker.TYPE_WARNING
            }
            else -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
        }
    }

    @JvmStatic
    fun isUridownloadable(uri: String, isDownloadable: Boolean): Boolean {
        val pattern = Pattern.compile("^.+\\.([pP][dD][fF])$")
        val matcher = pattern.matcher(uri)
        return matcher.find() || isDownloadable
    }

    @JvmStatic
    fun isValidUrl(invoiceUrl: String?): Boolean {
        val pattern = Pattern.compile("^(https|HTTPS):\\/\\/")
        val matcher = pattern.matcher(invoiceUrl)
        return matcher.find()
    }
}
