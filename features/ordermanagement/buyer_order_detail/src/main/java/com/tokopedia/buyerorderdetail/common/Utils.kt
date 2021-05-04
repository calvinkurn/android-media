package com.tokopedia.buyerorderdetail.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerType

object Utils {

    private const val STRING_TICKER_TYPE_ANNOUNCEMENT = "announcement"
    private const val STRING_TICKER_TYPE_ERROR = "error"
    private const val STRING_TICKER_TYPE_INFO = "info"
    private const val STRING_TICKER_TYPE_WARNING = "warning"

    fun getColoredIndicator(context: Context, colorHex: String): Drawable? {
        val color = if (colorHex.length > 1) Color.parseColor(colorHex)
        else MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        val drawable = MethodChecker.getDrawable(context, R.drawable.ic_buyer_order_status_indicator)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    fun copyText(context: Context, label: String, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun mapTickerType(typeString: String): Int {
        return when (typeString) {
            STRING_TICKER_TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            STRING_TICKER_TYPE_ERROR -> Ticker.TYPE_ERROR
            STRING_TICKER_TYPE_INFO -> Ticker.TYPE_INFORMATION
            STRING_TICKER_TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_INFORMATION
        }
    }
}