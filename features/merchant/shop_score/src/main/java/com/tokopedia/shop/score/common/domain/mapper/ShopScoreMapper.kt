package com.tokopedia.shop.score.common.domain.mapper

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_REVIEW_WITH_FOUR_STARS
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_ONE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_THREE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_TWO
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_TOTAL_LEVEL
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER
import com.tokopedia.shop.score.performance.presentation.model.CardTooltipLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel

object ShopScoreMapper {

    fun mapToShopPerformanceDetail(titlePerformanceDetail: String): ShopPerformanceDetailUiModel {
        val shopPerformanceDetailUiModel = ShopPerformanceDetailUiModel()
        when(titlePerformanceDetail) {
            ORDER_SUCCESS_RATE -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_success_order
                    descTips = R.string.desc_tips_success_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = ""
                }
            }
            CHAT_DISCUSSION_REPLY_SPEED -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_reply_chat
                    descTips = R.string.desc_tips_speed_reply_chat
                    moreInformation = R.string.set_hour_operational_shop_performance_detail
                    urlLink = ""
                }
            }
            CHAT_DISCUSSION_SPEED -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_level_reply_chat
                    descTips = R.string.desc_tips_level_reply_chat
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = ""
                }
            }
            SPEED_SENDING_ORDERS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_process_order
                    descTips = R.string.desc_tips_speed_process_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = ""
                }
            }
            PRODUCT_REVIEW_WITH_FOUR_STARS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_review_product
                    descTips = R.string.desc_tips_review_product
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = ""
                }
            }
            TOTAL_BUYER -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_total_buyer
                    descTips = R.string.desc_tips_total_buyer
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = ""
                }
            }
            OPEN_TOKOPEDIA_SELLER -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_open_seller_app
                    descTips = R.string.desc_tips_open_seller_app
                    moreInformation = R.string.set_time_selling_shop_performance_detail
                    urlLink = ""
                }
            }
        }
        return shopPerformanceDetailUiModel
    }

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