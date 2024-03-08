package com.tokopedia.productcard.reimagine

import android.view.View
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.productcard.R as productcardR

internal class CompatPaddingUtils(
    private val productCardView: View,
    private val useCompatPadding: Boolean,
    private val productCardModel: ProductCardModel,
) {

    fun updatePadding() {
        if (!useCompatPadding) return

        val padding = productCardView.context?.getPixel(
            productcardR.dimen.product_card_reimagine_use_compat_padding_size
        ) ?: 0

        val startPadding = if (productCardModel.hasRibbon()) 0 else padding
        productCardView.setPadding(startPadding, padding, padding, padding)
    }
}
