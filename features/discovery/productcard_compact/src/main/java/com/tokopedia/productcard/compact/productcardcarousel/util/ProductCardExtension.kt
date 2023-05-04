package com.tokopedia.productcard.compact.productcardcarousel.util

import android.view.View
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.common.util.ViewUtil

object ProductCardExtension {
    private const val EXPECTED_PRODUCT_ON_SCREEN = 3

    fun View.setProductCarouselWidth() {
        val spaceWidth = ViewUtil.getDpFromDimen(
            context = context,
            id = R.dimen.product_card_compact_product_card_total_space_width
        )
        val defaultWidth = ViewUtil.getDpFromDimen(
            context = context,
            id = R.dimen.product_card_compact_product_card_default_width
        )
        val calculationWidth = (getScreenWidth() - spaceWidth) / EXPECTED_PRODUCT_ON_SCREEN
        val width = if (calculationWidth < defaultWidth) defaultWidth else calculationWidth
        layoutParams.width = width.toIntSafely()
    }
}
