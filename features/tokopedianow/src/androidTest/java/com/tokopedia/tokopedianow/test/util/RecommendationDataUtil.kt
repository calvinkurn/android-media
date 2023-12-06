package com.tokopedia.tokopedianow.test.util

import com.tokopedia.recommendation_widget_common.data.RecommendationEntity.Recommendation
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity.Recommendation.LabelGroup

object RecommendationDataUtil {

    private const val LABEL_PRICE = "price"
    private const val LABEL_BEST_SELLER = "best_seller"
    private const val LABEL_GIMMICK = "gimmick"

    private const val LIGHT_GREEN = "lightGreen"
    private const val TEXT_DARK_ORANGE = "textDarkOrange"

    fun Recommendation.getPriceLabelGroup(): LabelGroup? {
        return labelGroups.firstOrNull { it.isPricePosition() && it.isLightGreenColor() }
    }

    fun Recommendation.getAssignedValueLabelGroup(): LabelGroup? {
        return if (isBestSellerLabelAvailable()) getBestSellerLabelGroup() else getNewProductLabelGroup()
    }

    private fun Recommendation.getNewProductLabelGroup(): LabelGroup? {
        return labelGroups.firstOrNull { it.isNewProductLabelPosition() && it.isTextDarkOrangeColor() }
    }

    private fun Recommendation.getBestSellerLabelGroup(): LabelGroup? {
        return labelGroups.firstOrNull { it.isBestSellerPosition() }
    }

    private fun Recommendation.isBestSellerLabelAvailable(): Boolean {
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
