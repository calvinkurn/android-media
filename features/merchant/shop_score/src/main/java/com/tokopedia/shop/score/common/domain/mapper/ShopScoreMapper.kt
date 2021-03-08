package com.tokopedia.shop.score.common.domain.mapper

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_ADMIN_FEATURE
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_BROADCAST_CHAT_FEATURE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_FREE_SHIPPING_FEATURE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_INCOME_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_ORDER_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_PM_VISITED_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_SELLER_ANNOUNCE
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_SPECIAL_DISCOUNT_FEATURE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_SPECIAL_RELEASE_FEATURE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_TOP_ADS_FEATURE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_VOUCHER_EXCLUSIVE_FEATURE_URL
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
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopScoreMapper @Inject constructor(private val userSession: UserSessionInterface) {

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
                }
            }
            SPEED_SENDING_ORDERS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_process_order
                    descTips = R.string.desc_tips_speed_process_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = SPEED_SENDING_ORDERS_URL
                }
            }
            PRODUCT_REVIEW_WITH_FOUR_STARS -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_review_product
                    descTips = R.string.desc_tips_review_product
                }
            }
            TOTAL_BUYER -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_total_buyer
                    descTips = R.string.desc_tips_total_buyer
                }
            }
            OPEN_TOKOPEDIA_SELLER -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_open_seller_app
                    descTips = R.string.desc_tips_open_seller_app
                }
            }
        }
        return shopPerformanceDetailUiModel
    }

    fun mapToShopPerformanceVisitableDummy(): List<BaseShopPerformance> {
        return mutableListOf<BaseShopPerformance>().apply {
            add(mapToHeaderShopPerformance())
            add(mapToSectionPeriodDetailPerformanceUiModel())
            add(mapToSectionPeriodDetailPerformanceUiModel())
            addAll(mapToItemDetailPerformanceUiModel())
            add(mapToItemRecommendationPMUiModel())
            if (userSession.isGoldMerchant) {
                add(mapToTransitionPeriodReliefUiModel())
            }
            add(mapToCardPotentialBenefit())
        }
    }

    fun mapToHeaderShopPerformance(shopLevel: Int = 3, shopScore: Int = 65): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        with(headerShopPerformanceUiModel) {
            when (shopScore) {
                in SHOP_SCORE_SIXTY..SHOP_SCORE_SIXTY_NINE -> {
                    when (shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = R.string.title_keep_up_level_1
                            descHeaderShopService = R.string.desc_keep_up_level_1
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = R.string.title_keep_up_level_2
                            descHeaderShopService = R.string.desc_keep_up_level_2
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            this.shopLevel = shopLevel
                            this.shopScore = shopScore
                            this.scorePenalty = -3
                            titleHeaderShopService = R.string.title_keep_up_level_3
                            descHeaderShopService = R.string.desc_keep_up_level_3
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = R.string.title_keep_up_level_4
                            descHeaderShopService = R.string.desc_keep_up_level_4
                        }
                    }
                }
                in SHOP_SCORE_SEVENTY..SHOP_SCORE_SEVENTY_NINE -> {
                    when (shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = R.string.title_good_level_1
                            descHeaderShopService = R.string.desc_good_level_1
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = R.string.title_good_level_2
                            descHeaderShopService = R.string.desc_good_level_2
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = R.string.title_good_level_3
                            descHeaderShopService = R.string.desc_good_level_3
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = R.string.title_good_level_4
                            descHeaderShopService = R.string.desc_good_level_4
                        }
                    }
                }
                in SHOP_SCORE_EIGHTY..SHOP_SCORE_EIGHTY_NINE -> {
                    when (shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = R.string.title_great_level_1
                            descHeaderShopService = R.string.desc_great_level_1
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = R.string.title_great_level_2
                            descHeaderShopService = R.string.desc_great_level_2
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = R.string.title_great_level_3
                            descHeaderShopService = R.string.desc_great_level_3
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = R.string.title_great_level_4
                            descHeaderShopService = R.string.desc_great_level_4
                        }
                    }
                }
                in SHOP_SCORE_NINETY..SHOP_SCORE_ONE_HUNDRED -> {
                    when (shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = R.string.title_perfect_level_1
                            descHeaderShopService = R.string.desc_perfect_level_1
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = R.string.title_perfect_level_2
                            descHeaderShopService = R.string.desc_perfect_level_2
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = R.string.title_perfect_level_3
                            descHeaderShopService = R.string.desc_perfect_level_3
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = R.string.title_perfect_level_4
                            descHeaderShopService = R.string.desc_perfect_level_4
                        }
                    }
                }
                in SHOP_SCORE_FIFTY..SHOP_SCORE_FIFTY_NINE -> {
                    titleHeaderShopService = R.string.title_performance_approaching
                    descHeaderShopService = R.string.desc_performance_approaching
                }
                in SHOP_SCORE_ZERO..SHOP_SCORE_FORTY_NINE -> {
                    titleHeaderShopService = R.string.title_performance_below
                    descHeaderShopService = R.string.desc_performance_below
                }
            }

            this.shopLevel = shopLevel
            this.shopScore = shopScore
        }

        return headerShopPerformanceUiModel
    }

    fun mapToShoInfoLevelUiModel(level: Int): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        //temporary dummy
        shopInfoLevelUiModel.shopIncome = "Rp4.000.000"
        shopInfoLevelUiModel.productSold = "5 produk"
        shopInfoLevelUiModel.periodDate = "1 APR - 1 OKT 2021"
        shopInfoLevelUiModel.nextUpdate = "15 Nov 2021"

        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(level)
        return shopInfoLevelUiModel
    }

    fun mapToItemDetailPerformanceUiModel(): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Tingkat kesuksesan pesanan",
                    valueDetailPerformance = "85%",
                    colorValueDetailPerformance = "#D6001C",
                    targetDetailPerformance = "90%"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Kecepatan membalas chat dan diskusi",
                    valueDetailPerformance = "300 menit",
                    colorValueDetailPerformance = "#D6001C",
                    targetDetailPerformance = "30 menit"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Kecepatan mengirim pesanan",
                    valueDetailPerformance = "65 menit",
                    colorValueDetailPerformance = "#FA591D",
                    targetDetailPerformance = "30 menit"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Tingkat membalas chat dan diskusi",
                    valueDetailPerformance = "100%",
                    colorValueDetailPerformance = "#31353B",
                    targetDetailPerformance = "100%"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Ulasan produk dengan bintang 4+",
                    valueDetailPerformance = "100%",
                    colorValueDetailPerformance = "#31353B",
                    targetDetailPerformance = "90%"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Jumlah pembeli",
                    valueDetailPerformance = "100%",
                    colorValueDetailPerformance = "#31353B",
                    targetDetailPerformance = "100%"
            ))
            add(ItemDetailPerformanceUiModel(
                    titleDetailPerformance = "Membuka Tokopedia Seller dalam 90 hari terakhir ",
                    valueDetailPerformance = "100%",
                    colorValueDetailPerformance = "#31353B",
                    targetDetailPerformance = "100%"
            ))
        }
    }

    fun mapToCardPotentialBenefit(): SectionPotentialPMBenefitUiModel {
        return SectionPotentialPMBenefitUiModel(potentialPMBenefitList = mapToItemPotentialBenefit())
    }

    fun mapToSectionPeriodDetailPerformanceUiModel(): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(period = "1 Jan - 31 Maret 2021", "7 Juni 2021")
    }

    fun mapToTransitionPeriodReliefUiModel(): TransitionPeriodReliefUiModel {
        return TransitionPeriodReliefUiModel(dateTransitionPeriodRelief = "5 Mei 201", iconTransitionPeriodRelief = IC_SELLER_ANNOUNCE)
    }

    fun mapToItemRecommendationPMUiModel(level: Int = 3): SectionShopRecommendationUiModel {
        return SectionShopRecommendationUiModel(
                recommendationShopList = mutableListOf<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>().apply {
                    when {
                        userSession.isShopOfficialStore -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_DISCOUNT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_discount,
                                            descRecommendation = R.string.desc_recommendation_special_discount,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_DISCOUNT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_discount,
                                            descRecommendation = R.string.desc_recommendation_special_discount,
                                            appLinkRecommendation = ""
                                    ))
                                }
                            }

                        }
                        userSession.isGoldMerchant -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_ADMIN_FEATURE,
                                            titleRecommendation = R.string.title_recommendation_admin,
                                            descRecommendation = R.string.desc_recommendation_admin,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                            }

                        }
                        !userSession.isGoldMerchant -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = ""
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_ADMIN_FEATURE,
                                            titleRecommendation = R.string.title_recommendation_admin,
                                            descRecommendation = R.string.desc_recommendation_admin,
                                            appLinkRecommendation = ""
                                    ))
                                }
                            }
                        }
                    }
                })
    }

    private fun mapToItemPotentialBenefit(): List<SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel> {
        val itemPotentialPMBenefitList = mutableListOf<SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel>()
        itemPotentialPMBenefitList.apply {
            add(SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                    iconPotentialPMUrl = IC_ORDER_PM_URL,
                    titlePotentialPMU = R.string.text_item_potential_pm_benefit_1
            ))

            add(SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                    iconPotentialPMUrl = IC_INCOME_PM_URL,
                    titlePotentialPMU = R.string.text_item_potential_pm_benefit_2
            ))

            add(SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                    iconPotentialPMUrl = IC_PM_VISITED_URL,
                    titlePotentialPMU = R.string.text_item_potential_pm_benefit_3
            ))
        }
        return itemPotentialPMBenefitList
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