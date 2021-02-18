package com.tokopedia.shop.score.common

import com.tokopedia.shop.score.R

object ShopScoreUtils {

    fun getLevelBarWhite(level: Int): Int {
        return when(level) {
            ShopScoreConstant.SHOP_SCORE_LEVEL_ONE -> R.drawable.ic_one_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_TWO -> R.drawable.ic_two_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_THREE -> R.drawable.ic_three_level_white
            ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR -> R.drawable.ic_four_level_white
            else -> R.drawable.ic_no_level
        }
    }
}