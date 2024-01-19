package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.productcard.R as productcardR

internal class CompatPaddingUtils(
    private val context: Context?,
    private val layoutParams: ViewGroup.LayoutParams?,
    private val attrs: AttributeSet?,
) {

    fun updateMargin() {
        if (layoutParams !is MarginLayoutParams || !useCompatPadding()) return

        layoutParams.run {
            val additionalMarginPx = context.getPixel(
                productcardR.dimen.product_card_reimagine_use_compat_padding_size
            )
            marginStart += additionalMarginPx
            leftMargin += additionalMarginPx

            topMargin += additionalMarginPx

            marginEnd += additionalMarginPx
            rightMargin += additionalMarginPx

            bottomMargin += additionalMarginPx
        }
    }

    private fun useCompatPadding(): Boolean {
        val typedArray = context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return false

        return try {
            typedArray.getBoolean(R.styleable.ProductCardView_useCompatPadding, false)
        } catch(_: Throwable) {
            false
        } finally {
            typedArray.recycle()
        }
    }

    companion object {
        private const val ADDITIONAL_MARGIN_DP = 4
    }
}
