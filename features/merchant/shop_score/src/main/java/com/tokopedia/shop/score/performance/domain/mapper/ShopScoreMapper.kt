package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.BROADCAST_CHAT_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.BRONZE_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.COUNT_DAYS_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.DIAMOND_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.DISCOUNT_SHOP_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.DOWN_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.FREE_SHIPPING_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.GOLD_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_BRONZE_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_DIAMOND_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_GOLD_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_SILVER_PM
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
import com.tokopedia.shop.score.common.ShopScoreConstant.NO_GRADE_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_IDENTIFIER
import com.tokopedia.shop.score.common.ShopScoreConstant.PM_ACTIVE
import com.tokopedia.shop.score.common.ShopScoreConstant.PM_INACTIVE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_REVIEW_WITH_FOUR_STARS_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.READ_TIPS_MORE_INFO_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SET_OPERATIONAL_HOUR_SHOP_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_ADMIN_URL
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
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ZERO
import com.tokopedia.shop.score.common.ShopScoreConstant.SILVER_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.SPECIAL_RELEASE_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.STILL_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.UP_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.dayText
import com.tokopedia.shop.score.common.ShopScoreConstant.minuteText
import com.tokopedia.shop.score.common.ShopScoreConstant.percentText
import com.tokopedia.shop.score.common.ShopScoreConstant.targetText
import com.tokopedia.shop.score.performance.domain.model.*
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.user.session.UserSessionInterface
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ShopScoreMapper @Inject constructor(private val userSession: UserSessionInterface,
                                          @ApplicationContext val context: Context?) {

    fun mapToShopPerformanceDetail(identifierPerformanceDetail: String): ShopPerformanceDetailUiModel {
        val shopPerformanceDetailUiModel = ShopPerformanceDetailUiModel()
        when (identifierPerformanceDetail) {
            ORDER_SUCCESS_RATE_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_success_order
                    descTips = R.string.desc_tips_success_order
                    moreInformation = R.string.read_tips_more_info_performance_detail
                    urlLink = READ_TIPS_MORE_INFO_URL
                }
            }
            CHAT_DISCUSSION_REPLY_SPEED_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_reply_chat
                    descTips = R.string.desc_tips_speed_reply_chat
                    moreInformation = R.string.set_hour_operational_shop_performance_detail
                    urlLink = SET_OPERATIONAL_HOUR_SHOP_URL
                }
            }
            CHAT_DISCUSSION_SPEED_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_level_reply_chat
                    descTips = R.string.desc_tips_level_reply_chat
                }
            }
            SPEED_SENDING_ORDERS_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_speed_process_order
                    descTips = R.string.desc_tips_speed_process_order
                    moreInformation = R.string.read_tips_for_send_fast_order
                    urlLink = SPEED_SENDING_ORDERS_URL
                }
            }
            PRODUCT_REVIEW_WITH_FOUR_STARS_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_review_product
                    descTips = R.string.desc_tips_review_product
                }
            }
            TOTAL_BUYER_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_total_buyer
                    descTips = R.string.desc_tips_total_buyer
                }
            }
            OPEN_TOKOPEDIA_SELLER_KEY -> {
                with(shopPerformanceDetailUiModel) {
                    descCalculation = R.string.desc_calculation_open_seller_app
                    descTips = R.string.desc_tips_open_seller_app
                }
            }
        }
        return shopPerformanceDetailUiModel
    }

    fun mapToShopPerformanceVisitable(shopScoreWrapperResponse: ShopScoreWrapperResponse, shopInfoPeriodUiModel: ShopInfoPeriodUiModel): List<BaseShopPerformance> {
        val shopScoreVisitableList = mutableListOf<BaseShopPerformance>()
        val isEligiblePM = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isEligiblePm
        val shopScoreResult = shopScoreWrapperResponse.shopScoreLevelResponse?.result
        shopScoreVisitableList.apply {
            if (shopInfoPeriodUiModel.isNewSeller) {
                add(mapToTimerNewSellerUiModel(shopInfoPeriodUiModel.shopAge, shopInfoPeriodUiModel.isEndTenureNewSeller).first)
            }
            add(mapToHeaderShopPerformance(shopScoreWrapperResponse.shopScoreLevelResponse?.result, shopInfoPeriodUiModel.isNewSeller))
            add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreWrapperResponse.shopScoreTooltipResponse?.result))
            if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
                addAll(mapToItemDetailPerformanceUiModel(shopScoreResult.shopScoreDetail))
            }

//            if (shopScoreResult?.shopScore.orZero() >= SHOP_SCORE_EIGHTY) {
//                add(mapToItemRecommendationPMUiModel(shopScoreResult?.shopLevel.orZero()))
//            }
            add(mapToItemRecommendationPMUiModel())


            when {
                userSession.isShopOfficialStore || shopInfoPeriodUiModel.isNewSeller -> {
                    add(SectionFaqUiModel(mapToItemFaqUiModel()))
                }
                userSession.isGoldMerchant && !userSession.isShopOfficialStore -> {
                    add(mapToTransitionPeriodReliefUiModel())
                    if (shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse != null &&
                            shopScoreWrapperResponse.goldGetPMStatusResponse != null) {
                        add(mapToItemPotentialStatusPMUiModel(
                                shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse,
                                shopScoreWrapperResponse.goldGetPMStatusResponse
                        ))
                    }
                }
                else -> {
                    if (isEligiblePM == true) {
                        if (shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse != null) {
                            add(mapToItemCurrentStatusRMUiModel(shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse?.potentialPmGrade))
                        }
                    } else {
                        add(mapToCardPotentialBenefitNonEligible())
                    }
                }
            }
        }
        return shopScoreVisitableList
    }

    private fun mapToHeaderShopPerformance(shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?, isNewSeller: Boolean): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        with(headerShopPerformanceUiModel) {
            when (shopScoreLevelResponse?.shopScore) {
                in SHOP_SCORE_SIXTY..SHOP_SCORE_SIXTY_NINE -> {
                    when (shopScoreLevelResponse?.shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = context?.getString(R.string.title_keep_up_level_1)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_keep_up_level_1)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = context?.getString(R.string.title_keep_up_level_2)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_keep_up_level_2)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = context?.getString(R.string.title_keep_up_level_3)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_keep_up_level_3)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = context?.getString(R.string.title_keep_up_level_4)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_keep_up_level_4)
                                    ?: ""
                        }
                    }
                }
                in SHOP_SCORE_SEVENTY..SHOP_SCORE_SEVENTY_NINE -> {
                    when (shopScoreLevelResponse?.shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = context?.getString(R.string.title_good_level_1)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_good_level_1)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = context?.getString(R.string.title_good_level_2)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_good_level_2)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = context?.getString(R.string.title_good_level_3)
                                    ?: "-"
                            descHeaderShopService = context?.getString(R.string.desc_good_level_3)
                                    ?: "-"
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = context?.getString(R.string.title_good_level_4)
                                    ?: "-"
                            descHeaderShopService = context?.getString(R.string.desc_good_level_4)
                                    ?: "-"
                        }
                    }
                }
                in SHOP_SCORE_EIGHTY..SHOP_SCORE_EIGHTY_NINE -> {
                    when (shopScoreLevelResponse?.shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = context?.getString(R.string.title_great_level_1)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_great_level_1)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = context?.getString(R.string.title_great_level_2)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_great_level_2)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = context?.getString(R.string.title_great_level_3)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_great_level_3)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = context?.getString(R.string.title_great_level_4)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_great_level_4)
                                    ?: ""
                        }
                    }
                }
                in SHOP_SCORE_NINETY..SHOP_SCORE_ONE_HUNDRED -> {
                    when (shopScoreLevelResponse?.shopLevel) {
                        SHOP_SCORE_LEVEL_ONE -> {
                            titleHeaderShopService = context?.getString(R.string.title_perfect_level_1)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_perfect_level_1)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_TWO -> {
                            titleHeaderShopService = context?.getString(R.string.title_perfect_level_2)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_perfect_level_2)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_THREE -> {
                            titleHeaderShopService = context?.getString(R.string.title_perfect_level_3)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_perfect_level_3)
                                    ?: ""
                        }
                        SHOP_SCORE_LEVEL_FOUR -> {
                            titleHeaderShopService = context?.getString(R.string.title_perfect_level_4)
                                    ?: ""
                            descHeaderShopService = context?.getString(R.string.desc_perfect_level_4)
                                    ?: ""
                        }
                    }
                }
                in SHOP_SCORE_FIFTY..SHOP_SCORE_FIFTY_NINE -> {
                    titleHeaderShopService = context?.getString(R.string.title_performance_approaching)
                            ?: ""
                    descHeaderShopService = context?.getString(R.string.desc_performance_approaching)
                            ?: ""
                }
                in SHOP_SCORE_ZERO..SHOP_SCORE_FORTY_NINE -> {
                    titleHeaderShopService = context?.getString(R.string.title_performance_below)
                            ?: ""
                    descHeaderShopService = context?.getString(R.string.desc_performance_below)
                            ?: ""
                }
            }

            this.shopLevel = shopScoreLevelResponse?.shopLevel?.toString() ?: "-"
            this.shopScore = shopScoreLevelResponse?.shopScore?.toString() ?: "-"
            this.scorePenalty = shopScoreLevelResponse?.shopScoreDetail?.firstOrNull { it.identifier == PENALTY_IDENTIFIER }?.rawValue
            this.isNewSeller = isNewSeller
        }

        return headerShopPerformanceUiModel
    }

    //if ready staging
    fun mapToShoInfoLevelUiModel(shopLevel: Int): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()

        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemDetailPerformanceUiModel(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {
            val shopScoreLevelSize = shopScoreLevelList?.filter { it.identifier != PENALTY_IDENTIFIER }?.size.orZero()
            shopScoreLevelList?.filter { it.identifier != PENALTY_IDENTIFIER }?.mapIndexed { index, shopScoreDetail ->

                val targetDetailPerformanceText = when (shopScoreDetail.identifier) {
                    CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY -> "${shopScoreDetail.nextMinValue} $minuteText"
                    ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY, TOTAL_BUYER_KEY ->
                        "${shopScoreDetail.nextMinValue}$percentText"
                    OPEN_TOKOPEDIA_SELLER_KEY -> "${shopScoreDetail.nextMinValue} $dayText"
                    else -> ""
                }

                add(ItemDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = shopScoreDetail.rawValue.toString(),
                        colorValueDetailPerformance = shopScoreDetail.colorText,
                        targetDetailPerformance = targetDetailPerformanceText,
                        isDividerShow = index + 1 < shopScoreLevelSize,
                        identifierDetailPerformance = shopScoreDetail.identifier
                ))
            }
        }
    }

    private fun mapToCardPotentialBenefitNonEligible(): SectionPotentialPMBenefitUiModel {
        return SectionPotentialPMBenefitUiModel(potentialPMBenefitList = mapToItemPotentialBenefit())
    }

    private fun mapToSectionPeriodDetailPerformanceUiModel(shopScoreTooltipResponse: ShopLevelTooltipResponse.ShopLevel.Result?): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(period = shopScoreTooltipResponse?.period
                ?: "-", nextUpdate = shopScoreTooltipResponse?.nextUpdate ?: "-")
    }

    private fun mapToTransitionPeriodReliefUiModel(): TransitionPeriodReliefUiModel {
        return TransitionPeriodReliefUiModel(dateTransitionPeriodRelief = getShopScoreDate(context), iconTransitionPeriodRelief = IC_SELLER_ANNOUNCE)
    }

    private fun mapToItemRecommendationPMUiModel(level: Int = 3): SectionShopRecommendationUiModel {
        return SectionShopRecommendationUiModel(
                recommendationShopList = mutableListOf<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>().apply {
                    when {
                        //official store section
                        userSession.isShopOfficialStore -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ApplinkConstInternalSellerapp.CREATE_VOUCHER
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_DISCOUNT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_discount,
                                            descRecommendation = R.string.desc_recommendation_special_discount,
                                            appLinkRecommendation = DISCOUNT_SHOP_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = SPECIAL_RELEASE_URL
                                    ))
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = SPECIAL_RELEASE_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_DISCOUNT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_discount,
                                            descRecommendation = R.string.desc_recommendation_special_discount,
                                            appLinkRecommendation = DISCOUNT_SHOP_URL
                                    ))
                                }
                            }
                        }
                        //power merchant section
                        userSession.isGoldMerchant -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ApplinkConstInternalSellerapp.CREATE_VOUCHER
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = SPECIAL_RELEASE_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_ADMIN_FEATURE,
                                            titleRecommendation = R.string.title_recommendation_admin,
                                            descRecommendation = R.string.desc_recommendation_admin,
                                            appLinkRecommendation = SHOP_ADMIN_URL
                                    ))
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_FREE_SHIPPING_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_free_shipping,
                                            descRecommendation = R.string.desc_recommendation_free_shipping,
                                            appLinkRecommendation = FREE_SHIPPING_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_SPECIAL_RELEASE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_special_release,
                                            descRecommendation = R.string.desc_recommendation_special_release,
                                            appLinkRecommendation = SPECIAL_RELEASE_URL
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                }
                            }

                        }
                        //reguler merchant section
                        else -> {
                            when (level) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ApplinkConstInternalSellerapp.CREATE_VOUCHER
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ApplinkConstInternalSellerapp.CREATE_VOUCHER
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_VOUCHER_EXCLUSIVE_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_voucher_exclusive,
                                            descRecommendation = R.string.desc_recommendation_voucher_exclusive,
                                            appLinkRecommendation = ApplinkConstInternalSellerapp.CREATE_VOUCHER
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_TOP_ADS_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_top_ads,
                                            descRecommendation = R.string.desc_recommendation_top_ads,
                                            appLinkRecommendation = TOPADS_CREATE_ONBOARDING
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_BROADCAST_CHAT_FEATURE_URL,
                                            titleRecommendation = R.string.title_recommendation_broadcast_chat,
                                            descRecommendation = R.string.desc_recommendation_broadcast_chat,
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
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
                                            appLinkRecommendation = String.format("%s?url=%s", ApplinkConst.WEBVIEW, BROADCAST_CHAT_URL)
                                    ))
                                    add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                            iconRecommendationUrl = IC_ADMIN_FEATURE,
                                            titleRecommendation = R.string.title_recommendation_admin,
                                            descRecommendation = R.string.desc_recommendation_admin,
                                            appLinkRecommendation = SHOP_ADMIN_URL
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
                    titlePotentialPM = R.string.text_item_potential_pm_benefit_1
            ))

            add(SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                    iconPotentialPMUrl = IC_INCOME_PM_URL,
                    titlePotentialPM = R.string.text_item_potential_pm_benefit_2
            ))

            add(SectionPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                    iconPotentialPMUrl = IC_PM_VISITED_URL,
                    titlePotentialPM = R.string.text_item_potential_pm_benefit_3
            ))
        }
        return itemPotentialPMBenefitList
    }

    private fun mapToItemPotentialStatusPMUiModel(goldPMGradeBenefitInfoResponse: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo?,
                                                  goldGetPMStatusResponse: GoldGetPMStatusResponse.GoldGetPMOSStatus?): ItemStatusPMUiModel {
        val potentialPMGrade = goldPMGradeBenefitInfoResponse?.potentialPmGrade
        val currentPMGrade = goldPMGradeBenefitInfoResponse?.currentPmGrade
        val statusPowerMerchant = goldGetPMStatusResponse?.data?.powerMerchant?.status
        val nextDate = goldPMGradeBenefitInfoResponse?.nextMonthlyRefreshDate.orEmpty()
        val (statusPM, titlePM) = when {
            currentPMGrade?.gradeName?.mapToGradeNameToInt().orZero() > potentialPMGrade?.gradeName?.mapToGradeNameToInt().orZero() -> {
                Pair(UP_POTENTIAL_PM, context?.getString(R.string.performance_regarding_pm_label).orEmpty())
            }
            currentPMGrade?.gradeName?.mapToGradeNameToInt() == potentialPMGrade?.gradeName?.mapToGradeNameToInt() -> {
                Pair(STILL_POTENTIAL_PM, context?.getString(R.string.current_regarding_penalty_pm_label).orEmpty())
            }
            else -> {
                Pair(DOWN_POTENTIAL_PM, context?.getString(R.string.performance_regarding_pm_label, currentPMGrade?.lastUpdateDate.orEmpty()).orEmpty())
            }
        }

        val bgPowerMerchant = when (currentPMGrade?.gradeName?.toLowerCase(Locale.getDefault())) {
            GRADE_BRONZE_PM -> {
                R.drawable.bg_header_bronze
            }
            GRADE_SILVER_PM -> {
                R.drawable.bg_header_silver
            }
            GRADE_GOLD_PM -> {
                R.drawable.bg_header_gold
            }
            GRADE_DIAMOND_PM -> {
                R.drawable.bg_header_diamond
            }
            else -> null
        }
        val descStatus = when (statusPowerMerchant) {
            PM_ACTIVE -> {
                when (statusPM) {
                    UP_POTENTIAL_PM, STILL_POTENTIAL_PM -> {
                        context?.getString(R.string.desc_up_still_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty()
                    }
                    DOWN_POTENTIAL_PM -> {
                        context?.getString(R.string.desc_down_pm_status, potentialPMGrade?.gradeName).orEmpty()
                    }
                    else -> ""
                }
            }
            else -> {
                context?.getString(R.string.desc_inactive_pm_status, PM_INACTIVE_TEXT).orEmpty()
            }
        }
        return ItemStatusPMUiModel(statusPowerMerchant = currentPMGrade?.gradeName.orEmpty(),
                titlePowerMerchant = titlePM,
                bgPowerMerchant = bgPowerMerchant,
                badgePowerMerchant = currentPMGrade?.imageBadgeUrl.orEmpty(),
                updateDatePotential = nextDate,
                descPotentialPM = descStatus,
                isActivePM = true)
    }

    private fun String?.mapToGradeNameToInt(): Int {
        return when (this?.toLowerCase(Locale.getDefault())) {
            GRADE_BRONZE_PM -> BRONZE_SCORE
            GRADE_SILVER_PM -> SILVER_SCORE
            GRADE_GOLD_PM -> GOLD_SCORE
            GRADE_DIAMOND_PM -> DIAMOND_SCORE
            else -> NO_GRADE_SCORE
        }
    }

    private fun mapToItemCurrentStatusRMUiModel(potentialPmGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?): ItemStatusRMUiModel {
        //only regular merchant & eligible
        val updateDate = getShopScoreDate(context)
        val statusPM = potentialPmGrade?.gradeName.orEmpty()
        val badgePM = potentialPmGrade?.imageBadgeUrl.orEmpty()
        val backgroundPM = when (statusPM.toLowerCase(Locale.getDefault())) {
            GRADE_BRONZE_PM -> {
                R.drawable.bg_header_bronze
            }
            GRADE_SILVER_PM -> {
                R.drawable.bg_header_silver
            }
            GRADE_GOLD_PM -> {
                R.drawable.bg_header_gold
            }
            GRADE_DIAMOND_PM -> {
                R.drawable.bg_header_diamond
            }
            else -> null
        }
        return ItemStatusRMUiModel(updateDatePotential = updateDate, badgeGradePM = badgePM, bgGradePM = backgroundPM, statusGradePM = statusPM)
    }

    private fun mapToCardTooltipLevel(level: Int = 0): List<CardTooltipLevelUiModel> {
        return mutableListOf<CardTooltipLevelUiModel>().apply {
            add(CardTooltipLevelUiModel(R.string.title_level_1, R.string.desc_level_1, SHOP_SCORE_LEVEL_ONE == level))
            add(CardTooltipLevelUiModel(R.string.title_level_2, R.string.desc_level_2, SHOP_SCORE_LEVEL_TWO == level))
            add(CardTooltipLevelUiModel(R.string.title_level_3, R.string.desc_level_3, SHOP_SCORE_LEVEL_THREE == level))
            add(CardTooltipLevelUiModel(R.string.title_level_4, R.string.desc_level_4, SHOP_SCORE_LEVEL_FOUR == level))
        }
    }

    private fun mapToItemFaqUiModel(): List<ItemFaqUiModel> {
        return mutableListOf<ItemFaqUiModel>().apply {
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_performance).orEmpty(),
                    desc_first = context?.getString(R.string.desc_shop_score_performance).orEmpty(),
            ))
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_benefit).orEmpty(),
                    desc_first = context?.getString(R.string.desc_shop_score_benefit).orEmpty(),
            ))
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_benefit).orEmpty(),
                    desc_first = context?.getString(R.string.desc_shop_score_benefit).orEmpty(),
            ))
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_calculation).orEmpty(),
                    desc_first = context?.getString(R.string.desc_section_1_shop_score_calculation).orEmpty(),
                    desc_second = context?.getString(R.string.desc_section_2_shop_score_calculation).orEmpty(),
                    isCalculationScore = true,
                    cardLevelList = mapToCardTooltipLevel(),
                    parameterFaqList = mapToItemParameterFaq()
            ))
            if (!userSession.isShopOfficialStore) {
                add(ItemFaqUiModel(
                        title = context?.getString(R.string.title_changes_shop_score_during_transition).orEmpty(),
                        desc_first = context?.getString(R.string.desc_changes_shop_score_during_transition).orEmpty()
                ))
            }
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_calculate_shop_performance_if_parameter_value_missing).orEmpty(),
                    desc_first = context?.getString(R.string.desc_calculate_shop_performance_if_parameter_value_missing).orEmpty(),
            ))
            add(ItemFaqUiModel(
                    title = context?.getString(R.string.title_calculate_shop_performance_for_new_seller).orEmpty(),
                    desc_first = context?.getString(R.string.desc_calculate_shop_performance_for_new_seller).orEmpty(),
            ))
        }
    }

    private fun mapToItemParameterFaq(): List<ItemParameterFaqUiModel> {
        return mutableListOf<ItemParameterFaqUiModel>().apply {
            add(ItemParameterFaqUiModel(title = context?.getString(R.string.title_parameter_shop_score_1).orEmpty(),
                    desc = context?.getString(R.string.desc_parameter_shop_score_1).orEmpty(),
                    score = context?.getString(R.string.score_parameter_shop_score_1).orEmpty()
            ))
            add(ItemParameterFaqUiModel(title = context?.getString(R.string.title_parameter_shop_score_2).orEmpty(),
                    desc = context?.getString(R.string.desc_parameter_shop_score_2).orEmpty(),
                    score = context?.getString(R.string.score_parameter_shop_score_2).orEmpty()
            ))
            add(ItemParameterFaqUiModel(title = context?.getString(R.string.title_parameter_shop_score_3).orEmpty(),
                    desc = context?.getString(R.string.desc_parameter_shop_score_3).orEmpty(),
                    score = context?.getString(R.string.score_parameter_shop_score_3).orEmpty()
            ))
        }
    }

    private fun mapToTimerNewSellerUiModel(shopAge: Int = 0, isEndTenure: Boolean): Pair<ItemTimerNewSellerUiModel, Boolean> {
        val nextSellerDays = COUNT_DAYS_NEW_SELLER - shopAge

        val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays)
        return Pair(ItemTimerNewSellerUiModel(effectiveDate = effectiveDate,
                effectiveDateText = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER),
                isTenureDate = isEndTenure), nextSellerDays > 0)
    }

    private fun getNNextDaysTimeCalendar(nextDays: Int): Calendar {
        val date = Calendar.getInstance(Locale.getDefault())
        date.add(Calendar.DATE, nextDays + 1)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date
    }

    private fun format(timeMillis: Long, pattern: String, locale: Locale = Locale.getDefault()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }
}