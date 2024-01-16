package com.tokopedia.tokopedianow.test.util

import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.LabelGroup

object ProductCarouselDataUtil {

    private const val LABEL_PRICE = "price"
    private const val LABEL_BEST_SELLER = "best_seller"
    private const val LABEL_GIMMICK = "gimmick"

    private const val LIGHT_GREEN = "lightGreen"
    private const val TEXT_DARK_ORANGE = "textDarkOrange"

    fun Grid.getPriceLabelGroup(): LabelGroup? {
        return labelGroup.firstOrNull { it.isPricePosition() && it.isLightGreenColor() }
    }

    fun Grid.getAssignedValueLabelGroup(): LabelGroup? {
        return if (isBestSellerLabelAvailable()) getBestSellerLabelGroup() else getNewProductLabelGroup()
    }

    private fun Grid.getNewProductLabelGroup(): LabelGroup? {
        return labelGroup.firstOrNull { it.isNewProductLabelPosition() && it.isTextDarkOrangeColor() }
    }

    private fun Grid.getBestSellerLabelGroup(): LabelGroup? {
        return labelGroup.firstOrNull { it.isBestSellerPosition() }
    }

    private fun Grid.isBestSellerLabelAvailable(): Boolean {
        return getBestSellerLabelGroup() != null
    }

    private fun LabelGroup.isNewProductLabelPosition(): Boolean {
        return position == LABEL_GIMMICK
    }

    private fun LabelGroup.isBestSellerPosition(): Boolean {
        return position == LABEL_BEST_SELLER
    }

    private fun LabelGroup.isPricePosition(): Boolean {
        return position == LABEL_PRICE
    }

    private fun LabelGroup.isLightGreenColor(): Boolean {
        return type == LIGHT_GREEN
    }

    private fun LabelGroup.isTextDarkOrangeColor(): Boolean {
        return type == TEXT_DARK_ORANGE
    }
}
