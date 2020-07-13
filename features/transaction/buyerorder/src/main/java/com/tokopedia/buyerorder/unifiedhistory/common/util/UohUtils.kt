package com.tokopedia.buyerorder.unifiedhistory.common.util

import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created by fwidjaja on 10/07/20.
 */
object UohUtils {
    fun getTickerType(typeStr: String): Int {
        return when (typeStr) {
            UohConsts.TICKER_TYPE_ANNOUNCEMENT -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
            UohConsts.TICKER_TYPE_ERROR -> {
                Ticker.TYPE_ERROR
            }
            UohConsts.TICKER_TYPE_INFORMATION -> {
                Ticker.TYPE_INFORMATION
            }
            UohConsts.TICKER_TYPE_WARNING -> {
                Ticker.TYPE_WARNING
            }
            else -> {
                Ticker.TYPE_ANNOUNCEMENT
            }
        }
    }

    fun getButtonType(typeBtn: String): Int {
        return when (typeBtn) {
            UohConsts.BUTTON_TYPE_ALTERNATE -> {
                UnifyButton.Type.ALTERNATE
            }
            UohConsts.BUTTON_TYPE_MAIN -> {
                UnifyButton.Type.MAIN
            }
            else -> {
                UnifyButton.Type.TRANSACTION
            }
        }
    }

    fun getButtonVariant(variantBtn: String): Int {
        return when (variantBtn) {
            UohConsts.BUTTON_VARIANT_FILLED -> {
                UnifyButton.Variant.FILLED
            }
            UohConsts.BUTTON_VARIANT_GHOST -> {
                UnifyButton.Variant.GHOST
            }
            else -> {
                UnifyButton.Variant.TEXT_ONLY
            }
        }
    }
}