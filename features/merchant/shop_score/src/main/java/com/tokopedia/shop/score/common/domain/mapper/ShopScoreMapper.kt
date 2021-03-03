package com.tokopedia.shop.score.common.domain.mapper

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_REVIEW_WITH_FOUR_STARS
import com.tokopedia.shop.score.common.ShopScoreConstant.READ_TIPS_MORE_INFO_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SET_OPERATIONAL_HOUR_SHOP_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_EIGHTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_EIGHTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_FIFTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_FIFTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_FORTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_FOUR
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_ONE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_THREE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_LEVEL_TWO
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_NINETY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ONE_HUNDRED
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_TOTAL_LEVEL
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ZERO
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER
import com.tokopedia.shop.score.performance.presentation.model.CardTooltipLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopInfoLevelUiModel
import com.tokopedia.shop.score.performance.presentation.model.ShopPerformanceDetailUiModel

object ShopScoreMapper {

    fun mapToShopPerformanceDetail(titlePerformanceDetail: String): ShopPerformanceDetailUiModel {
        val shopPerformanceDetailUiModel = ShopPerformanceDetailUiModel()
        when (titlePerformanceDetail) {
            ORDER_SUCCESS_RATE -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_success_order
                    descTips = R.string.desc_tips_success_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
                }
            }
            CHAT_DISCUSSION_REPLY_SPEED -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_reply_chat
                    descTips = R.string.desc_tips_speed_reply_chat
                    moreInformation = R.string.set_hour_operational_shop_performance_detail
                    urlLink = SET_OPERATIONAL_HOUR_SHOP_URL
                }
            }
            CHAT_DISCUSSION_SPEED -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_level_reply_chat
                    descTips = R.string.desc_tips_level_reply_chat
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
                }
            }
            SPEED_SENDING_ORDERS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_process_order
                    descTips = R.string.desc_tips_speed_process_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
                }
            }
            PRODUCT_REVIEW_WITH_FOUR_STARS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_review_product
                    descTips = R.string.desc_tips_review_product
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
                }
            }
            TOTAL_BUYER -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_total_buyer
                    descTips = R.string.desc_tips_total_buyer
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
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

    fun mapToHeaderShopPerformance(shopLevel: Int, shopScore: Int): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        when (shopScore) {
            in SHOP_SCORE_SIXTY..SHOP_SCORE_SIXTY_NINE -> {
                when (shopLevel) {
                    SHOP_SCORE_LEVEL_ONE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_keep_up_level_1
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_keep_up_level_1
                    }
                    SHOP_SCORE_LEVEL_TWO -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_keep_up_level_2
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_keep_up_level_2
                    }
                    SHOP_SCORE_LEVEL_THREE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_keep_up_level_3
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_keep_up_level_3
                    }
                    SHOP_SCORE_LEVEL_FOUR -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_keep_up_level_4
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_keep_up_level_4
                    }
                }
            }
            in SHOP_SCORE_SEVENTY..SHOP_SCORE_SEVENTY_NINE -> {
                when (shopLevel) {
                    SHOP_SCORE_LEVEL_ONE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_good_level_1
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_good_level_1
                    }
                    SHOP_SCORE_LEVEL_TWO -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_good_level_2
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_good_level_2
                    }
                    SHOP_SCORE_LEVEL_THREE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_good_level_3
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_good_level_3
                    }
                    SHOP_SCORE_LEVEL_FOUR -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_good_level_4
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_good_level_4
                    }
                }
            }
            in SHOP_SCORE_EIGHTY..SHOP_SCORE_EIGHTY_NINE -> {
                when (shopLevel) {
                    SHOP_SCORE_LEVEL_ONE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_great_level_1
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_great_level_1
                    }
                    SHOP_SCORE_LEVEL_TWO -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_great_level_2
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_great_level_2
                    }
                    SHOP_SCORE_LEVEL_THREE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_great_level_3
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_great_level_3
                    }
                    SHOP_SCORE_LEVEL_FOUR -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_great_level_4
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_great_level_4
                    }
                }
            }
            in SHOP_SCORE_NINETY..SHOP_SCORE_ONE_HUNDRED -> {
                when (shopLevel) {
                    SHOP_SCORE_LEVEL_ONE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_perfect_level_1
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_perfect_level_1
                    }
                    SHOP_SCORE_LEVEL_TWO -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_perfect_level_2
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_perfect_level_2
                    }
                    SHOP_SCORE_LEVEL_THREE -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_perfect_level_3
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_perfect_level_3
                    }
                    SHOP_SCORE_LEVEL_FOUR -> {
                        headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_perfect_level_4
                        headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_perfect_level_4
                    }
                }
            }
            in SHOP_SCORE_FIFTY..SHOP_SCORE_FIFTY_NINE -> {
                headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_performance_approaching
                headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_performance_approaching
            }
            in SHOP_SCORE_ZERO..SHOP_SCORE_FORTY_NINE -> {
                headerShopPerformanceUiModel.titleHeaderShopService = R.string.title_performance_below
                headerShopPerformanceUiModel.descHeaderShopService = R.string.desc_performance_below
            }
        }

        headerShopPerformanceUiModel.shopLevel = shopLevel
        headerShopPerformanceUiModel.shopScore = shopScore

        return headerShopPerformanceUiModel
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