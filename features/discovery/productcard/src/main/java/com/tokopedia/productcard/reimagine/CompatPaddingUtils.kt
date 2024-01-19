package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.setPadding
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.productcard.R as productcardR

internal class CompatPaddingUtils(
    private val context: Context?,
    private val productCardView: View,
    private val attrs: AttributeSet?,
) {

    fun updateMargin() {
        if (!useCompatPadding()) return

        productCardView.setPadding(
            context.getPixel(
                productcardR.dimen.product_card_reimagine_use_compat_padding_size
            )
        )
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
}
