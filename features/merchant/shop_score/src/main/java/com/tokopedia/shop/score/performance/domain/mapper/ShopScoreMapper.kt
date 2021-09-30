package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.gm.common.utils.GoldMerchantUtil
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.*
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.COUNT_DAYS_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.ONE_HUNDRED_PERCENT
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_IDENTIFIER
import com.tokopedia.shop.score.common.ShopScoreConstant.PRODUCT_REVIEW_WITH_FOUR_STARS_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.READ_TIPS_MORE_INFO_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SET_OPERATIONAL_HOUR_SHOP_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_AGE_SIXTY
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
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_NULL
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ONE_HUNDRED
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ZERO
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.dayText
import com.tokopedia.shop.score.common.ShopScoreConstant.minuteText
import com.tokopedia.shop.score.common.ShopScoreConstant.peopleText
import com.tokopedia.shop.score.common.ShopScoreConstant.percentText
import com.tokopedia.shop.score.performance.domain.model.*
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.user.session.UserSessionInterface
import java.text.ParseException
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong

class ShopScoreMapper @Inject constructor(
    private val userSession: UserSessionInterface,
    @ApplicationContext val context: Context?,
    private val shopScorePrefManager: ShopScorePrefManager
) {

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
                    descCalculation = R.string.desc_calculation_open_seller_app_within_90_days
                    descTips = R.string.desc_tips_open_seller_app
                }
            }
        }
        return shopPerformanceDetailUiModel
    }

    fun mapToShopPerformanceVisitable(
        shopScoreWrapperResponse: ShopScoreWrapperResponse,
        shopInfoPeriodUiModel: ShopInfoPeriodUiModel
    ): List<BaseShopPerformance> {
        val shopScoreVisitableList = mutableListOf<BaseShopPerformance>()
        val powerMerchantResponse = shopScoreWrapperResponse.goldGetPMOStatusResponse?.powerMerchant
        val officialStoreResponse = shopScoreWrapperResponse.goldGetPMOStatusResponse?.officialStore
        val isEligiblePM = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isEligiblePm
        val isEligiblePMPro = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isEligiblePmPro
        val shopScoreResult = shopScoreWrapperResponse.shopScoreLevelResponse?.result
        val powerMerchantData = shopScoreWrapperResponse.goldGetPMOStatusResponse
        val shopAge = if (shopScoreWrapperResponse.goldGetPMShopInfoResponse != null) {
            shopScoreWrapperResponse.goldGetPMShopInfoResponse?.shopAge
        } else {
            shopInfoPeriodUiModel.shopAge
        } ?: 0
        val isNewSeller = shopInfoPeriodUiModel.isNewSeller

        val isNewSellerProjection = shopAge in SHOP_AGE_SIXTY..NEW_SELLER_DAYS

        val isOfficialStore = if (officialStoreResponse?.status.isNullOrBlank()) {
            userSession.isShopOfficialStore
        } else {
            officialStoreResponse?.status == OSStatus.ACTIVE
        }

        val shopScore = shopScoreResult?.shopScore ?: SHOP_SCORE_NULL

        shopScoreVisitableList.apply {
            if (isNewSeller || shopAge < NEW_SELLER_DAYS) {
                val mapTimerNewSeller =
                    mapToTimerNewSellerUiModel(
                        shopAge,
                        shopInfoPeriodUiModel.isEndTenureNewSeller, shopScore.toInt()
                    )
                if (mapTimerNewSeller.second) {
                    add(mapTimerNewSeller.first)
                    if (shopAge < SHOP_AGE_NINETY_SIX && shopScore > 0) {
                        add(ItemLevelScoreProjectUiModel())
                    }
                }
            }

            add(
                mapToHeaderShopPerformance(
                    shopScoreWrapperResponse.shopScoreLevelResponse?.result,
                    powerMerchantData,
                    isOfficialStore,
                    shopAge,
                    shopInfoPeriodUiModel.dateShopCreated
                )
            )
            add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreResult, isNewSeller))
            if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
                addAll(
                    mapToItemDetailPerformanceUiModel(
                        shopScoreResult.shopScoreDetail, shopAge,
                        shopScore
                    )
                )
            }

            if (isShowProtectedParameter(shopAge.toInt())) {
                add(getProtectedParameterSection(shopScoreResult?.shopScoreDetail, shopAge.toInt()))
            }

            val recommendationTools =
                shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools
            if (recommendationTools?.isNotEmpty() == true) {
                add(
                    mapToItemRecommendationPMUiModel
                        (shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools)
                )
            }

            when {
                isOfficialStore || shopScore < 0 -> {
                    add(SectionFaqUiModel(mapToItemFaqUiModel(isNewSeller, isOfficialStore)))
                    return@apply
                }
                //PM PRO Section
                powerMerchantResponse?.pmTier == PMTier.PRO -> {
                    if (powerMerchantResponse.status == PMStatusConst.ACTIVE) {
                        add(ItemStatusPMProUiModel())
                        return@apply
                    } else if (powerMerchantResponse.status == PMStatusConst.IDLE) {
                        add(mapToCardPotentialBenefitNonEligible())
                        return@apply
                    }
                }
                powerMerchantResponse?.pmTier == PMTier.REGULAR -> {
                    when {
                        //RM Section logic
                        powerMerchantResponse.status == PMStatusConst.INACTIVE -> {
                            if (isNewSeller) {
                                add(
                                    mapToItemCurrentStatusRMUiModel(isNewSeller)
                                )
                                return@apply
                            } else {
                                when {
                                    isEligiblePMPro == true -> {
                                        add(mapToSectionRMEligibleToPMPro())
                                        return@apply
                                    }
                                    isEligiblePM == true -> {
                                        add(
                                            mapToItemCurrentStatusRMUiModel(
                                                isNewSellerProjection
                                            )
                                        )
                                        return@apply
                                    }
                                    else -> {
                                        add(mapToCardPotentialBenefitNonEligible())
                                        return@apply
                                    }
                                }
                            }
                        }
                        //PM Section logic
                        (powerMerchantResponse.status == PMStatusConst.ACTIVE)
                                && !isOfficialStore -> {
                            when (isEligiblePMPro) {
                                true -> {
                                    add(mapToSectionPMEligibleToPMPro())
                                    return@apply
                                }
                                else -> {
                                    add(mapToItemPMUiModel())
                                    return@apply
                                }
                            }
                        }
                        (powerMerchantResponse.status == PMStatusConst.IDLE &&
                                !isOfficialStore) -> {
                            add(mapToCardPotentialBenefitNonEligible())
                            return@apply
                        }
                    }
                }
            }
        }
        return shopScoreVisitableList
    }

    private fun mapToHeaderShopPerformance(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        powerMerchantResponse: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        isOfficialStore: Boolean,
        shopAge: Long,
        dateShopCreated: String
    ): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        val shopScore = shopScoreLevelResponse?.shopScore ?: -1
        val shopLevel = shopScoreLevelResponse?.shopLevel ?: -1
        with(headerShopPerformanceUiModel) {
            when {
                shopScore < 0 || shopAge in ONE_NUMBER..TWO_NUMBER -> {
                    titleHeaderShopService = context?.getString(R.string.title_new_seller_level_0)
                        ?: ""
                    this.showCardNewSeller = true
                    descHeaderShopService = context?.getString(R.string.desc_new_seller_level_0)
                        ?: ""
                }
                shopAge in THREE_NUMBER..SHOP_SCORE_EIGHTY_NINE -> {
                    when {
                        shopScore < SHOP_SCORE_SIXTY -> {
                            titleHeaderShopService =
                                context?.getString(R.string.title_tenure_new_seller_score_under_60)
                                    ?: ""
                        }
                        shopScore in SHOP_SCORE_SIXTY..SHOP_SCORE_SEVENTY_NINE -> {
                            titleHeaderShopService =
                                context?.getString(R.string.title_tenure_new_seller_score_between_60_to_79)
                                    ?: ""
                        }
                        shopScore >= SHOP_SCORE_EIGHTY -> {
                            titleHeaderShopService =
                                context?.getString(R.string.title_tenure_new_seller_score_more_80)
                                    ?: ""
                        }
                    }
                    descHeaderShopService = context?.getString(R.string.desc_tenure_new_seller)
                        ?: ""
                }
                else -> {
                    when (shopScore) {
                        in SHOP_SCORE_SIXTY..SHOP_SCORE_SIXTY_NINE -> {
                            when (shopLevel) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_keep_up_level_1)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_keep_up_level_1)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_keep_up_level_2)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_keep_up_level_2)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_keep_up_level_3)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_keep_up_level_3)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_keep_up_level_4)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_keep_up_level_4)
                                            ?: ""
                                }
                            }
                        }
                        in SHOP_SCORE_SEVENTY..SHOP_SCORE_SEVENTY_NINE -> {
                            when (shopLevel) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_good_level_1)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_good_level_1)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_good_level_2)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_good_level_2)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_good_level_3)
                                            ?: "-"
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_good_level_3)
                                            ?: "-"
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_good_level_4)
                                            ?: "-"
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_good_level_4)
                                            ?: "-"
                                }
                            }
                        }
                        in SHOP_SCORE_EIGHTY..SHOP_SCORE_EIGHTY_NINE -> {
                            when (shopLevel) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_great_level_1)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_great_level_1)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_great_level_2)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_great_level_2)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_great_level_3)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_great_level_3)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_great_level_4)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_great_level_4)
                                            ?: ""
                                }
                            }
                        }
                        in SHOP_SCORE_NINETY..SHOP_SCORE_ONE_HUNDRED -> {
                            when (shopLevel) {
                                SHOP_SCORE_LEVEL_ONE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_perfect_level_1)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_perfect_level_1)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_TWO -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_perfect_level_2)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_perfect_level_2)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_THREE -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_perfect_level_3)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_perfect_level_3)
                                            ?: ""
                                }
                                SHOP_SCORE_LEVEL_FOUR -> {
                                    titleHeaderShopService =
                                        context?.getString(R.string.title_perfect_level_4)
                                            ?: ""
                                    descHeaderShopService =
                                        context?.getString(R.string.desc_perfect_level_4)
                                            ?: ""
                                }
                            }
                        }
                        in SHOP_SCORE_FIFTY..SHOP_SCORE_FIFTY_NINE -> {
                            titleHeaderShopService =
                                context?.getString(R.string.title_performance_approaching)
                                    ?: ""
                            descHeaderShopService =
                                context?.getString(R.string.desc_performance_approaching)
                                    ?: ""
                        }
                        in SHOP_SCORE_ZERO..SHOP_SCORE_FORTY_NINE -> {
                            titleHeaderShopService =
                                context?.getString(R.string.title_performance_below)
                                    ?: ""
                            descHeaderShopService =
                                context?.getString(R.string.desc_performance_below)
                                    ?: ""
                        }
                    }
                }
            }
            this.powerMerchantData = powerMerchantResponse
            this.shopAge = shopAge
            this.shopLevel =
                if (shopLevel < 0) {
                    "-"
                } else {
                    shopLevel.toString()
                }

            this.shopScore =
                if (shopScore < 0) {
                    "-"
                } else {
                    shopScore.toString()
                }

            this.scorePenalty =
                shopScoreLevelResponse?.shopScoreDetail?.find {
                    it.identifier == PENALTY_IDENTIFIER
                }?.rawValue?.roundToLong().orZero()

            this.isShowPopupEndTenure = getIsShowPopupEndTenure(shopAge, dateShopCreated)
        }
        return headerShopPerformanceUiModel
    }

    fun mapToShopInfoLevelUiModel(shopLevel: Long): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemDetailPerformanceUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long,
        shopScore: Long
    ): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {

            val multipleFilterShopScore = listOf(
                CHAT_DISCUSSION_REPLY_SPEED_KEY,
                SPEED_SENDING_ORDERS_KEY,
                ORDER_SUCCESS_RATE_KEY,
                CHAT_DISCUSSION_SPEED_KEY,
                PRODUCT_REVIEW_WITH_FOUR_STARS_KEY,
                TOTAL_BUYER_KEY,
                OPEN_TOKOPEDIA_SELLER_KEY
            )

            val isShowProtectedParameter = isShowProtectedParameter(shopAge.toInt())

            val shopScoreLevelFilter =
                shopScoreLevelList?.filter { it.identifier in multipleFilterShopScore }
            val shopScoreLevelSize = shopScoreLevelFilter?.size.orZero()
            val sortShopScoreLevelParam = sortItemDetailPerformanceFormatted(shopScoreLevelFilter)
            val filterShopScoreLevelParam = if (isShowProtectedParameter) {
                sortShopScoreLevelParam.filterNot {
                    it.identifier == TOTAL_BUYER_KEY ||
                            it.identifier == OPEN_TOKOPEDIA_SELLER_KEY
                }
            } else {
                sortShopScoreLevelParam
            }
            filterShopScoreLevelParam.forEachIndexed { index, shopScoreDetail ->

                val roundNextMinValue = shopScoreDetail.nextMinValue

                val (targetDetailPerformanceText, parameterItemDetailPerformance) =
                    when (shopScoreDetail.identifier) {
                        CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY -> {
                            val rawValueFormatted =
                                if (roundNextMinValue < 0.0) {
                                    "-"
                                } else {
                                    if (roundNextMinValue.rem(1) == 0.0) {
                                        roundNextMinValue.roundToLong().toString()
                                    } else {
                                        getNumberFormatted(roundNextMinValue)
                                    }
                                }
                            Pair("$rawValueFormatted $minuteText", minuteText)
                        }
                        ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY -> {
                            val minValueFormattedPercent =
                                (shopScoreDetail.nextMinValue * ONE_HUNDRED_PERCENT)
                            val minValueFormatted = if (minValueFormattedPercent < 0.0) {
                                "-"
                            } else {
                                if (minValueFormattedPercent.rem(1) == 0.0) {
                                    minValueFormattedPercent.roundToLong().toString()
                                } else {
                                    getNumberFormatted(minValueFormattedPercent)
                                }
                            }
                            Pair("$minValueFormatted$percentText", percentText)
                        }
                        TOTAL_BUYER_KEY -> {
                            val peopleCount = if (roundNextMinValue < 0.0) {
                                "-"
                            } else {
                                roundNextMinValue.roundToLong()
                            }
                            Pair("$peopleCount $peopleText", peopleText)
                        }
                        OPEN_TOKOPEDIA_SELLER_KEY -> {
                            val openSellerAppCount = if (roundNextMinValue < 0.0) {
                                "-"
                            } else {
                                roundNextMinValue.roundToLong()
                            }
                            Pair("$openSellerAppCount $dayText", dayText)
                        }
                        else -> Pair("-", "")
                    }

                val rawValueFormatted =
                    if (shopScoreDetail.rawValue < 0.0) {
                        "-"
                    } else {
                        when (shopScoreDetail.identifier) {
                            ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY -> {
                                val rawValuePercent = shopScoreDetail.rawValue * ONE_HUNDRED_PERCENT
                                val rawValueFormatted = if (rawValuePercent.rem(1) == 0.0) {
                                    rawValuePercent.roundToLong().toString()
                                } else {
                                    getNumberFormatted(rawValuePercent)
                                }
                                rawValueFormatted
                            }
                            CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY -> {
                                val rawValueFormatted =
                                    if (shopScoreDetail.rawValue.rem(1) == 0.0) {
                                        shopScoreDetail.rawValue.roundToLong().toString()
                                    } else {
                                        getNumberFormatted(shopScoreDetail.rawValue)
                                    }
                                rawValueFormatted
                            }
                            TOTAL_BUYER_KEY, OPEN_TOKOPEDIA_SELLER_KEY -> {
                                shopScoreDetail.rawValue.roundToLong().toString()
                            }
                            else -> {
                                shopScoreDetail.rawValue.toString()
                            }
                        }
                    }

                add(
                    ItemDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = rawValueFormatted,
                        colorValueDetailPerformance = shopScoreDetail.colorText,
                        targetDetailPerformance = targetDetailPerformanceText,
                        isDividerHide = if (isShowProtectedParameter) false
                        else index + 1 == shopScoreLevelSize,
                        identifierDetailPerformance = shopScoreDetail.identifier,
                        parameterValueDetailPerformance = parameterItemDetailPerformance,
                        shopAge = shopAge,
                        shopScore = shopScore
                    )
                )
            }
        }
    }

    private fun sortItemDetailPerformanceFormatted(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?): List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail> {
        val identifierFilter = hashMapOf(
            ORDER_SUCCESS_RATE_KEY to ORDER_SUCCESS_RATE_INDEX,
            CHAT_DISCUSSION_REPLY_SPEED_KEY to CHAT_DISCUSSION_REPLY_SPEED_INDEX,
            SPEED_SENDING_ORDERS_KEY to SPEED_SENDING_ORDERS_INDEX,
            CHAT_DISCUSSION_SPEED_KEY to CHAT_DISCUSSION_SPEED_INDEX,
            PRODUCT_REVIEW_WITH_FOUR_STARS_KEY to PRODUCT_REVIEW_WITH_FOUR_STARS_INDEX,
            TOTAL_BUYER_KEY to TOTAL_BUYER_INDEX,
            OPEN_TOKOPEDIA_SELLER_KEY to OPEN_TOKOPEDIA_SELLER_INDEX
        )

        val compareIdentifier =
            Comparator { item1: ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail,
                         item2: ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail ->
                return@Comparator identifierFilter[item1.identifier].orZero() - identifierFilter[item2.identifier].orZero()
            }

        val copyItemDetail =
            mutableListOf<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>().apply {
                shopScoreLevelList?.let { addAll(it) }
            }
        copyItemDetail.sortWith(compareIdentifier)
        return copyItemDetail
    }

    private fun mapToItemPMUiModel(): ItemStatusPMUiModel {
        return ItemStatusPMUiModel(
            descPM = context?.getString(R.string.desc_content_pm_not_eligible_pm_pro).orEmpty()
        )
    }

    private fun mapToCardPotentialBenefitNonEligible(): SectionRMPotentialPMBenefitUiModel {
        return SectionRMPotentialPMBenefitUiModel(
            potentialPMBenefitList = mapToItemPotentialBenefit()
        )
    }

    private fun mapToSectionPeriodDetailPerformanceUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        isNewSeller: Boolean
    ): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(
            period = shopScoreLevelResponse?.period
                ?: "-", nextUpdate = shopScoreLevelResponse?.nextUpdate
                ?: "-", isNewSeller = isNewSeller
        )
    }

    private fun mapToItemRecommendationPMUiModel(
        recommendationTools: List<GetRecommendationToolsResponse
        .ValuePropositionGetRecommendationTools.RecommendationTool>?
    )
            : SectionShopRecommendationUiModel {
        return SectionShopRecommendationUiModel(
            recommendationTools?.map {
                SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                    iconRecommendationUrl = it.imageUrl,
                    appLinkRecommendation = it.relatedLinkAppLink,
                    descRecommendation = it.text,
                    titleRecommendation = it.title,
                    identifier = it.identifier
                )
            } ?: emptyList())
    }

    private fun mapToSectionRMEligibleToPMPro(): SectionRMPotentialPMProUiModel {
        val potentialPMProPMBenefitList =
            mapToItemPMProBenefit() as? List<SectionRMPotentialPMProUiModel.ItemPMProBenefitUIModel>
        return SectionRMPotentialPMProUiModel(
            potentialPMProPMBenefitList = potentialPMProPMBenefitList
        )
    }

    private fun mapToSectionPMEligibleToPMPro(): SectionPMPotentialPMProUiModel {
        val potentialPMProPMBenefitList =
            mapToItemPMProBenefit() as? List<SectionPMPotentialPMProUiModel.ItemPMProBenefitUIModel>

        return SectionPMPotentialPMProUiModel(
            potentialPMProPMBenefitList = potentialPMProPMBenefitList
        )
    }

    private fun mapToItemPMProBenefit(): List<ItemParentBenefitUiModel> {
        return listOf(
            ItemParentBenefitUiModel(
                iconUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_1,
                titleResources = R.string.title_item_benefit_1_pm_pro
            ),
            ItemParentBenefitUiModel(
                iconUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_2,
                titleResources = R.string.title_item_benefit_2_pm_pro
            ),
            ItemParentBenefitUiModel(
                iconUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_3,
                titleResources = R.string.title_item_benefit_3_pm_pro
            )
        )
    }

    private fun mapToItemPotentialBenefit():
            List<SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel> {
        return listOf(
            SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                iconPotentialPMUrl = ShopScoreConstant.IC_PM_PRO_BADGE_BENEFIT_URL,
                titlePotentialPM = R.string.title_item_rm_section_pm_benefit_1
            ),
            SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                iconPotentialPMUrl = ShopScoreConstant.IC_FREE_SHIPPING_BENEFIT_URL,
                titlePotentialPM = R.string.title_item_rm_section_pm_benefit_2
            ),
            SectionRMPotentialPMBenefitUiModel.ItemPotentialPMBenefitUIModel(
                iconPotentialPMUrl = ShopScoreConstant.IC_PROMOTION_BENEFIT_URL,
                titlePotentialPM = R.string.title_item_rm_section_pm_benefit_3
            )
        )
    }

    private fun mapToItemCurrentStatusRMUiModel(isNewSellerProjection: Boolean)
            : ItemStatusRMUiModel {
        return ItemStatusRMUiModel(
            titleRMEligible =
            if (isNewSellerProjection)
                context?.getString(R.string.title_header_rm_section_new_seller).orEmpty()
            else
                context?.getString(R.string.title_header_rm_section).orEmpty(),
            descRMEligible = if (isNewSellerProjection)
                context?.getString(R.string.desc_potential_rm_section_new_seller).orEmpty()
            else
                context?.getString(R.string.desc_potential_eligible_power_merchant).orEmpty()
        )
    }

    private fun mapToCardTooltipLevel(level: Long = 0): List<CardTooltipLevelUiModel> {
        return listOf(
            CardTooltipLevelUiModel(
                R.string.title_level_1,
                R.string.desc_level_1,
                SHOP_SCORE_LEVEL_ONE == level
            ),
            CardTooltipLevelUiModel(
                R.string.title_level_2,
                R.string.desc_level_2,
                SHOP_SCORE_LEVEL_TWO == level
            ),
            CardTooltipLevelUiModel(
                R.string.title_level_3,
                R.string.desc_level_3,
                SHOP_SCORE_LEVEL_THREE == level
            ),
            CardTooltipLevelUiModel(
                R.string.title_level_4,
                R.string.desc_level_4,
                SHOP_SCORE_LEVEL_FOUR == level
            )
        )
    }

    private fun mapToItemFaqUiModel(
        isNewSeller: Boolean,
        isOfficialStore: Boolean
    ): List<ItemFaqUiModel> {
        return mutableListOf<ItemFaqUiModel>().apply {
            add(
                ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_performance).orEmpty(),
                    desc_first = context?.getString(R.string.desc_shop_score_performance).orEmpty(),
                    isShow = true
                )
            )
            add(
                ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_benefit).orEmpty(),
                    desc_first = context?.getString(R.string.desc_shop_score_benefit).orEmpty(),
                )
            )
            add(
                ItemFaqUiModel(
                    title = context?.getString(R.string.title_shop_score_calculation).orEmpty(),
                    desc_first = context?.getString(R.string.desc_section_1_shop_score_calculation)
                        .orEmpty(),
                    desc_second = context?.getString(R.string.desc_section_2_shop_score_calculation)
                        .orEmpty(),
                    isCalculationScore = true,
                    cardLevelList = mapToCardTooltipLevel(),
                    parameterFaqList = mapToItemParameterFaq()
                )
            )
            add(
                ItemFaqUiModel(
                    title = context?.getString(R.string.title_calculate_shop_performance_if_parameter_value_missing)
                        .orEmpty(),
                    desc_first = context?.getString(R.string.desc_calculate_shop_performance_if_parameter_value_missing)
                        .orEmpty(),
                )
            )
            if (isNewSeller) {
                add(
                    ItemFaqUiModel(
                        title = context?.getString(R.string.title_calculate_shop_performance_for_new_seller)
                            .orEmpty(),
                        desc_first = context?.getString(R.string.desc_calculate_shop_performance_for_new_seller)
                            .orEmpty(),
                    )
                )
            }
            if (isOfficialStore) {
                add(
                    ItemFaqUiModel(
                        title = context?.getString(R.string.title_shop_score_affect_os)
                            .orEmpty(),
                        desc_first = context?.getString(R.string.desc_shop_score_affect_os)
                            .orEmpty(),
                    )
                )
            }
            add(
                ItemFaqUiModel(
                    title = context?.getString(R.string.title_time_adjustment_what_relief_for_new_seller)
                        .orEmpty(),
                    desc_first = context?.getString(R.string.desc_time_adjustment_what_relief_for_new_seller)
                        .orEmpty(),
                )
            )
        }
    }

    private fun mapToItemParameterFaq(): List<ItemParameterFaqUiModel> {
        return listOf(
            ItemParameterFaqUiModel(
                title = context?.getString(R.string.title_parameter_shop_score_1).orEmpty(),
                desc = context?.getString(R.string.desc_parameter_shop_score_1).orEmpty(),
                score = context?.getString(R.string.score_parameter_shop_score_1).orEmpty()
            ),
            ItemParameterFaqUiModel(
                title = context?.getString(R.string.title_parameter_shop_score_2).orEmpty(),
                desc = context?.getString(R.string.desc_parameter_shop_score_2).orEmpty(),
                score = context?.getString(R.string.score_parameter_shop_score_2).orEmpty()
            ),
            ItemParameterFaqUiModel(
                title = context?.getString(R.string.title_parameter_shop_score_3).orEmpty(),
                desc = context?.getString(R.string.desc_parameter_shop_score_3).orEmpty(),
                score = context?.getString(R.string.score_parameter_shop_score_3).orEmpty()
            )
        )
    }

    private fun mapToTimerNewSellerUiModel(shopAge: Long = 0, isEndTenure: Boolean, shopScore: Int)
            : Pair<ItemTimerNewSellerUiModel, Boolean> {
        val nextSellerDays = COUNT_DAYS_NEW_SELLER - shopAge

        val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays.toInt())
        return Pair(
            ItemTimerNewSellerUiModel(
                effectiveDate = effectiveDate,
                effectiveDateText = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER),
                isTenureDate = isEndTenure,
                shopAge = shopAge,
                shopScore = shopScore
            ), nextSellerDays > 0
        )
    }

    private fun getProtectedParameterSection(
        shopScoreLevelList:
        List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Int
    ): ProtectedParameterSectionUiModel {
        val totalBuyer =
            shopScoreLevelList?.find { it.identifier == TOTAL_BUYER_KEY }?.title.orEmpty()
        val openTokopediaSeller =
            shopScoreLevelList?.find { it.identifier == OPEN_TOKOPEDIA_SELLER_KEY }?.title.orEmpty()
        return ProtectedParameterSectionUiModel(
            itemProtectedParameterList = listOf(
                ItemProtectedParameterUiModel(totalBuyer),
                ItemProtectedParameterUiModel(openTokopediaSeller)
            ),
            protectedParameterDate = getProtectedParameterDaysDate(shopAge)
        )
    }

    private fun isShowProtectedParameter(shopAge: Int): Boolean {
        return shopAge in SHOP_AGE_THREE..SHOP_AGE_FIFTY_NINE
    }

    private fun getProtectedParameterDaysDate(shopAge: Int): String {
        return try {
            val date = Calendar.getInstance(getLocale())
            val diffDays = (SHOP_AGE_FIFTY_NINE - shopAge)
            val firstMondayDays = GoldMerchantUtil.getNNextDaysBasedOnFirstMonday(diffDays, true)
            val totalTargetDays = diffDays + firstMondayDays
            date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) + totalTargetDays)
            format(date.timeInMillis, PATTERN_DATE_TEXT)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    private fun getNNextDaysTimeCalendar(nextDays: Int): Calendar {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.DATE, nextDays)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date
    }

    private fun getNumberFormatted(valueResponse: Double): String {
        return try {
            val number = valueResponse.toString().split(".").getOrNull(0) ?: ""
            val decimalNumber =
                valueResponse.toString().split(".").getOrNull(1)
                    ?.getOrNull(0) ?: ""
            "$number.$decimalNumber"
        } catch (e: IndexOutOfBoundsException) {
            String.format("%.1f", valueResponse)
        }
    }


    private fun getIsShowPopupEndTenure(shopAge: Long, dateShopCreated: String): Boolean {
        return if (shopScorePrefManager.getIsShowPopupEndTenure()) {
            val calendar = Calendar.getInstance(getLocale())
            if (shopAge in SHOP_AGE_NINETY..SHOP_AGE_NINETY_SIX &&
                !GoldMerchantUtil.getIsExistingSellerPastMonday(dateShopCreated, shopAge)
            ) {
                calendar.getIsRangeCurrentWeekFromMonday()
            } else if (shopAge > SHOP_AGE_NINETY_SIX &&
                shopAge <= (SHOP_AGE_NINETY_SIX + calendar.getNNextDaysPopupEndTenure())
            ) {
                calendar.getIsRangeCurrentWeekFromMonday()
            } else false
        } else false
    }

    private fun Calendar.getIsRangeCurrentWeekFromMonday(): Boolean {
        return if (this.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY) {
            this.get(Calendar.DAY_OF_WEEK) in Calendar.MONDAY..Calendar.SATURDAY
        } else {
            false
        }
    }

    private fun Calendar.getNNextDaysPopupEndTenure(): Int {
        return when (this.get(Calendar.DAY_OF_WEEK)) {
            Calendar.TUESDAY -> ONE_NUMBER
            Calendar.WEDNESDAY -> TWO_NUMBER
            Calendar.THURSDAY -> THREE_NUMBER
            Calendar.FRIDAY -> FOUR_NUMBER
            Calendar.SATURDAY -> FIVE_NUMBER
            else -> ZERO_NUMBER
        }
    }

    companion object {
        const val SHOP_AGE_NINETY = 90
        const val SHOP_AGE_NINETY_SIX = 96
        const val SHOP_AGE_FIFTY_NINE = 59
        const val SHOP_AGE_THREE = 3
        const val ORDER_SUCCESS_RATE_INDEX = 0
        const val CHAT_DISCUSSION_REPLY_SPEED_INDEX = 1
        const val SPEED_SENDING_ORDERS_INDEX = 2
        const val CHAT_DISCUSSION_SPEED_INDEX = 3
        const val PRODUCT_REVIEW_WITH_FOUR_STARS_INDEX = 4
        const val TOTAL_BUYER_INDEX = 5
        const val OPEN_TOKOPEDIA_SELLER_INDEX = 6
    }
}