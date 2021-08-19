package com.tokopedia.buyerorderdetail.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.math.BigDecimal
import java.math.RoundingMode

object Utils {
    private const val STRING_BUTTON_TYPE_ALTERNATE = "alternate"
    private const val STRING_BUTTON_TYPE_MAIN = "main"
    private const val STRING_BUTTON_TYPE_TRANSACTION = "transaction"

    private const val STRING_BUTTON_VARIANT_FILLED = "filled"
    private const val STRING_BUTTON_VARIANT_GHOST = "ghost"
    private const val STRING_BUTTON_VARIANT_TEXT_ONLY = "text_only"

    private const val CURRENCY_RUPIAH = "Rp"

    fun parseColorHex(context: Context, colorHex: String, defaultColor: Int): Int {
        return try {
            Color.parseColor(colorHex)
        } catch (e: Exception) {
            MethodChecker.getColor(context, defaultColor)
        }
    }

    fun getColoredIndicator(context: Context, colorHex: String): Drawable? {
        val color = parseColorHex(context, colorHex, com.tokopedia.unifyprinciples.R.color.Unify_N0)
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
            BuyerOrderDetailTickerType.ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            BuyerOrderDetailTickerType.ERROR -> Ticker.TYPE_ERROR
            BuyerOrderDetailTickerType.INFO -> Ticker.TYPE_INFORMATION
            BuyerOrderDetailTickerType.WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_INFORMATION
        }
    }

    fun mapButtonType(typeString: String): Int {
        return when (typeString) {
            STRING_BUTTON_TYPE_ALTERNATE -> UnifyButton.Type.ALTERNATE
            STRING_BUTTON_TYPE_MAIN -> UnifyButton.Type.MAIN
            STRING_BUTTON_TYPE_TRANSACTION -> UnifyButton.Type.TRANSACTION
            else -> UnifyButton.Type.MAIN
        }
    }

    fun mapButtonVariant(variantString: String): Int {
        return when (variantString) {
            STRING_BUTTON_VARIANT_FILLED -> UnifyButton.Variant.FILLED
            STRING_BUTTON_VARIANT_GHOST -> UnifyButton.Variant.GHOST
            STRING_BUTTON_VARIANT_TEXT_ONLY -> UnifyButton.Variant.TEXT_ONLY
            else -> UnifyButton.Variant.FILLED
        }
    }

    fun composeItalicNote(productNote: String): SpannableString {
        return SpannableString(productNote).apply {
            setSpan(StyleSpan(Typeface.ITALIC), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    fun Double.toCurrencyFormatted(): String {
        val value =  BigDecimal(this).apply {
            setScale(0, RoundingMode.HALF_UP)
        }
        val values = CurrencyFormatHelper.convertToRupiah(value.toString())
        return "$CURRENCY_RUPIAH$values"
    }
}