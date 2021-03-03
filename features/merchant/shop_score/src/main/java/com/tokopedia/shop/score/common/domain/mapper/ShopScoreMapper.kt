package com.tokopedia.shop.score.common.domain.mapper

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_ONE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_THREE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_TWO
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_TOTAL_LEVEL
import com.tokopedia.shop.score.performance.presentation.model.CardTooltipLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel

object ShopScoreMapper {

    fun mapToShoInfoLevelUiModel(level: Int): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(level)
        return shopInfoLevelUiModel
    }

    private fun mapToCardTooltipLevel(level: Int): List<CardTooltipLevelUiModel> {
        val cardTooltipLevelUiModelList = mutableListOf<CardTooltipLevelUiModel>()

        for (i in 1..SHOP_SCORE_TOTAL_LEVEL) {
            when (i) {
                SHOP_SCORE_LEVEL_ONE -> {
                    cardTooltipLevelUiModelList.add(CardTooltipLevelUiModel(R.string.title_level_1, R.string.desc_level_1, i == level))
                }
                SHOP_SCORE_LEVEL_TWO -> {
                    cardTooltipLevelUiModelList.add(CardTooltipLevelUiModel(R.string.title_level_2, R.string.desc_level_2, i == level))
                }
                SHOP_SCORE_LEVEL_THREE -> {
                    cardTooltipLevelUiModelList.add(CardTooltipLevelUiModel(R.string.title_level_3, R.string.desc_level_3, i == level))
                }
                SHOP_SCORE_LEVEL_FOUR -> {
                    cardTooltipLevelUiModelList.add(CardTooltipLevelUiModel(R.string.title_level_4, R.string.desc_level_4, i == level))
                }
            }
        }

        return cardTooltipLevelUiModelList
    }
}