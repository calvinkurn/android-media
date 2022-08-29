package com.tokopedia.vouchercreation.shop.create.view.enums

import android.content.Context
import com.tokopedia.kotlin.extensions.view.toBlankOrString

object CurrencyScale {
    internal const val THOUSAND = 1000
    internal const val MILLION = 1000000
    internal const val BILLION = 1000000000
}

fun getScaledValuePair(context: Context, value: Int): Pair<String, String> {
    if (value >= CurrencyScale.THOUSAND) {
        return if (value >= CurrencyScale.MILLION) {
            if (value >= CurrencyScale.BILLION) {
                Pair("", "")
            } else {
                Pair((value/ CurrencyScale.MILLION).toString(), context.getString(ValueScaleType.MILLION.stringRes).toBlankOrString())
            }
        } else {
            val scaleText = context.getString(ValueScaleType.THOUSAND.stringRes).toBlankOrString()
            Pair((value/ CurrencyScale.THOUSAND).toString(), scaleText)
        }
    } else {
        return if (value > 0) {
            Pair(value.toString(), "")
        } else {
            Pair("", "")
        }
    }
}
