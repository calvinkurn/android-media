package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.gm.common.constant.*
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.COUNT_DAYS_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_INCOME_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_ORDER_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_PM_VISITED_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_SELLER_ANNOUNCE
import com.tokopedia.shop.score.common.ShopScoreConstant.ONE_HUNDRED_PERCENT
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_PERIOD_DATE
import com.tokopedia.shop.score.common.ShopScoreConstant.PENALTY_IDENTIFIER
import com.tokopedia.shop.score.common.ShopScoreConstant.PM_PRO_BENEFIT_URL_1
import com.tokopedia.shop.score.common.ShopScoreConstant.PM_PRO_BENEFIT_URL_2
import com.tokopedia.shop.score.common.ShopScoreConstant.PM_PRO_BENEFIT_URL_3
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
import com.tokopedia.shop.score.common.getLocale
import com.tokopedia.shop.score.performance.domain.model.*
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.user.session.UserSessionInterface
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong

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
                    descCalculation = R.string.desc_calculation_open_seller_app_within_90_days
                    descTips = R.string.desc_tips_open_seller_app
                }
            }
        }
        return shopPerformanceDetailUiModel
    }

    fun mapToShopPerformanceVisitable(shopScoreWrapperResponse: ShopScoreWrapperResponse, shopInfoPeriodUiModel: ShopInfoPeriodUiModel): List<BaseShopPerformance> {
        val shopScoreVisitableList = mutableListOf<BaseShopPerformance>()
        val powerMerchantResponse = shopScoreWrapperResponse.goldGetPMOStatusResponse?.powerMerchant
        val officialStoreResponse = shopScoreWrapperResponse.goldGetPMOStatusResponse?.officialStore
        val isEligiblePM = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isEligiblePm
        val isEligiblePMPro = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isEligiblePmPro
        val shopScoreResult = shopScoreWrapperResponse.shopScoreLevelResponse?.result
        val shopAge = if (shopScoreWrapperResponse.goldGetPMShopInfoResponse != null) {
            shopScoreWrapperResponse.goldGetPMShopInfoResponse?.shopAge
        } else {
            shopInfoPeriodUiModel.shopAge
        } ?: 0
        val isNewSeller = shopInfoPeriodUiModel.isNewSeller

        val isNewSellerProjection = shopAge in SHOP_AGE_SIXTY..NEW_SELLER_DAYS

        shopScoreVisitableList.apply {
            if (isNewSeller || shopAge < NEW_SELLER_DAYS) {
                val mapTimerNewSeller = mapToTimerNewSellerUiModel(shopAge, shopInfoPeriodUiModel.isEndTenureNewSeller)
                if (mapTimerNewSeller.second) {
                    add(mapTimerNewSeller.first)
                    if (shopAge >= SHOP_AGE_SIXTY) {
                        add(ItemLevelScoreProjectUiModel())
                    }
                }
            }

            add(mapToHeaderShopPerformance(shopScoreWrapperResponse.shopScoreLevelResponse?.result, shopAge))
            add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreResult, isNewSeller))
            if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
                addAll(mapToItemDetailPerformanceUiModel(shopScoreResult.shopScoreDetail, shopAge))
            }

            if (shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools?.isNotEmpty() == true) {
                add(mapToItemRecommendationPMUiModel(shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools))
            }

            when {
                officialStoreResponse?.status == OSStatus.ACTIVE || shopAge < SHOP_AGE_SIXTY -> {
                    add(SectionFaqUiModel(mapToItemFaqUiModel()))
                }
                powerMerchantResponse?.pmTier == PMTier.PRO -> {
                    add(ItemStatusPMProUiModel())
                }
                powerMerchantResponse?.pmTier == PMTier.REGULAR -> {
                    when {
                        powerMerchantResponse.status == PMStatusConst.INACTIVE -> {
                            if (shopAge >= SHOP_AGE_SIXTY) {
                                if (isNewSellerProjection) {
                                    add(mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel, isNewSellerProjection))
                                    return@apply
                                } else {
                                    if (isEligiblePM == true) {
                                        add(mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel, isNewSellerProjection))
                                        return@apply
                                    } else {
                                        add(mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel))
                                        return@apply
                                    }
                                }
                            }
                        }
                        (powerMerchantResponse.status == PMStatusConst.ACTIVE || powerMerchantResponse.status == PMStatusConst.IDLE)
                                && !userSession.isShopOfficialStore -> {
                            if (shopInfoPeriodUiModel.periodType == TRANSITION_PERIOD) {
                                if (shopAge >= NEW_SELLER_DAYS) {
                                    add(mapToTransitionPeriodReliefUiModel(shopInfoPeriodUiModel.periodEndDate))
                                }
                            }

                            if (shopAge >= SHOP_AGE_SIXTY) {
                                when (isEligiblePMPro) {
                                    true -> {
                                        add(mapToSectionPMPro(shopInfoPeriodUiModel))
                                        return@apply
                                    }
                                    else -> {
                                        add(mapToItemPMUiModel(isNewSellerProjection))
                                        return@apply
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return shopScoreVisitableList
    }

    private fun mapToHeaderShopPerformance(shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?, shopAge: Long): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        val shopScore = shopScoreLevelResponse?.shopScore ?: -1
        val shopLevel = shopScoreLevelResponse?.shopLevel ?: -1
        with(headerShopPerformanceUiModel) {
            when {
                shopAge < SHOP_AGE_SIXTY -> {
                    titleHeaderShopService = context?.getString(R.string.title_new_seller_level_0)
                        ?: ""
                    this.showCardNewSeller = true
                    val nextSellerDays = SHOP_AGE_SIXTY - shopAge
                    val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays.toInt())
                    val dateNewSellerProjection = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER)
                    descHeaderShopService = context?.getString(R.string.desc_new_seller_level_0, dateNewSellerProjection)
                        ?: ""
                }
                shopScore < 0 || shopLevel < 0 -> {
                    titleHeaderShopService = context?.getString(R.string.title_performance_below)
                            ?: ""
                    descHeaderShopService = context?.getString(R.string.desc_performance_below)
                            ?: ""
                }
                shopAge in SHOP_AGE_SIXTY..COUNT_DAYS_NEW_SELLER -> {
                    when {
                        shopScore < SHOP_SCORE_SIXTY -> {
                            titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_under_60)
                                    ?: ""
                        }
                        shopScore in SHOP_SCORE_SIXTY..SHOP_SCORE_SEVENTY_NINE -> {
                            titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_between_60_to_79)
                                    ?: ""
                        }
                        shopScore >= SHOP_SCORE_EIGHTY -> {
                            titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_more_80)
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
                            when (shopLevel) {
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
                            when (shopLevel) {
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
                            when (shopLevel) {
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
                }
            }
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

            this.scorePenalty = shopScoreLevelResponse?.shopScoreDetail?.find { it.identifier == PENALTY_IDENTIFIER }?.rawValue?.roundToLong().orZero()
        }
        return headerShopPerformanceUiModel
    }

    fun mapToShopInfoLevelUiModel(shopLevel: Long): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemDetailPerformanceUiModel(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?, shopAge: Long): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {

            val multipleFilterShopScore = listOf(CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY,
                    ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY,
                    TOTAL_BUYER_KEY, OPEN_TOKOPEDIA_SELLER_KEY)

            val shopScoreLevelFilter = shopScoreLevelList?.filter { it.identifier in multipleFilterShopScore }
            val shopScoreLevelSize = shopScoreLevelFilter?.size.orZero()
            val sortShopScoreLevelParam = sortItemDetailPerformanceFormatted(shopScoreLevelFilter)
            sortShopScoreLevelParam.forEachIndexed { index, shopScoreDetail ->

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
                                val minValueFormattedPercent = (shopScoreDetail.nextMinValue * ONE_HUNDRED_PERCENT)
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
                                    val rawValueFormatted = if (shopScoreDetail.rawValue.rem(1) == 0.0) {
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

                add(ItemDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = rawValueFormatted,
                        colorValueDetailPerformance = shopScoreDetail.colorText,
                        targetDetailPerformance = targetDetailPerformanceText,
                        isDividerHide = index + 1 == shopScoreLevelSize,
                        identifierDetailPerformance = shopScoreDetail.identifier,
                        parameterValueDetailPerformance = parameterItemDetailPerformance,
                        shopAge = shopAge
                ))
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

        val compareIdentifier = Comparator { item1: ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail,
                                             item2: ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail ->
            return@Comparator identifierFilter[item1.identifier].orZero() - identifierFilter[item2.identifier].orZero()
        }

        val copyItemDetail = mutableListOf<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>().apply {
            shopScoreLevelList?.let { addAll(it) }
        }
        copyItemDetail.sortWith(compareIdentifier)
        return copyItemDetail
    }

    private fun mapToItemPMUiModel(isNewSellerProjection: Boolean): ItemStatusPMUiModel {
        return ItemStatusPMUiModel(
                descPM = if (isNewSellerProjection)
                    context?.getString(R.string.desc_pm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.desc_content_pm_section).orEmpty(),
                isNewSellerProjection = isNewSellerProjection
        )
    }

    private fun mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): SectionPotentialPMBenefitUiModel {
        return SectionPotentialPMBenefitUiModel(potentialPMBenefitList = mapToItemPotentialBenefit(),
                transitionEndDate = DateFormatUtils.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT, shopInfoPeriodUiModel.periodEndDate))
    }

    private fun mapToSectionPeriodDetailPerformanceUiModel(shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?, isNewSeller: Boolean): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(period = shopScoreLevelResponse?.period
                ?: "-", nextUpdate = shopScoreLevelResponse?.nextUpdate
                ?: "-", isNewSeller = isNewSeller)
    }

    private fun mapToTransitionPeriodReliefUiModel(endDate: String): TransitionPeriodReliefUiModel {
        return TransitionPeriodReliefUiModel(dateTransitionPeriodRelief = DateFormatUtils.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT, endDate), iconTransitionPeriodRelief = IC_SELLER_ANNOUNCE)
    }

    private fun mapToItemRecommendationPMUiModel(recommendationTools: List<GetRecommendationToolsResponse.ValuePropositionGetRecommendationTools.RecommendationTool>?): SectionShopRecommendationUiModel {
        return SectionShopRecommendationUiModel(
                recommendationShopList = mutableListOf<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>().apply {
                    recommendationTools?.forEach {
                        add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                iconRecommendationUrl = it.imageUrl,
                                appLinkRecommendation = it.relatedLinkAppLink,
                                descRecommendation = it.text,
                                titleRecommendation = it.title,
                                identifier = it.identifier
                        ))
                    }
                })
    }

    private fun mapToSectionPMPro(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): SectionPotentialPMProUiModel {
        return SectionPotentialPMProUiModel(potentialPMProPMBenefitList = mapToItemPMProBenefit(), transitionEndDate = DateFormatUtils.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT, shopInfoPeriodUiModel.periodEndDate))
    }

    private fun mapToItemPMProBenefit(): List<SectionPotentialPMProUiModel.ItemPMProBenefitUIModel> {
        val itemPotentialPMBenefitList = mutableListOf<SectionPotentialPMProUiModel.ItemPMProBenefitUIModel>()
        itemPotentialPMBenefitList.apply {
            add(SectionPotentialPMProUiModel.ItemPMProBenefitUIModel(
                    iconPotentialPMProUrl = PM_PRO_BENEFIT_URL_1,
                    titlePotentialPMPro = R.string.title_item_benefit_1_pm_pro
            ))

            add(SectionPotentialPMProUiModel.ItemPMProBenefitUIModel(
                    iconPotentialPMProUrl = PM_PRO_BENEFIT_URL_2,
                    titlePotentialPMPro = R.string.title_item_benefit_2_pm_pro
            ))

            add(SectionPotentialPMProUiModel.ItemPMProBenefitUIModel(
                    iconPotentialPMProUrl = PM_PRO_BENEFIT_URL_3,
                    titlePotentialPMPro = R.string.title_item_benefit_3_pm_pro
            ))
        }
        return itemPotentialPMBenefitList
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

    private fun mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel: ShopInfoPeriodUiModel, isNewSellerProjection: Boolean)
            : ItemStatusRMUiModel {
        val updateDate = DateFormatUtils.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT, shopInfoPeriodUiModel.periodEndDate)
        return ItemStatusRMUiModel(updateDatePotential = updateDate,
                titleRMEligible =
                if (isNewSellerProjection)
                    context?.getString(R.string.title_header_rm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.title_header_rm_section).orEmpty(),
                descRMEligible = if (isNewSellerProjection)
                    context?.getString(R.string.desc_potential_rm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.desc_potential_eligible_power_merchant, updateDate).orEmpty()
        )
    }

    private fun mapToCardTooltipLevel(level: Long = 0): List<CardTooltipLevelUiModel> {
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
                    isShow = true
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

    private fun mapToTimerNewSellerUiModel(shopAge: Long = 0, isEndTenure: Boolean)
            : Pair<ItemTimerNewSellerUiModel, Boolean> {
        val nextSellerDays = COUNT_DAYS_NEW_SELLER - shopAge

        val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays.toInt())
        return Pair(ItemTimerNewSellerUiModel(effectiveDate = effectiveDate,
                effectiveDateText = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER),
                isTenureDate = isEndTenure,
                shopAge = shopAge
        ), nextSellerDays > 0)
    }

    private fun getNNextDaysTimeCalendar(nextDays: Int): Calendar {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.DATE, nextDays)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date
    }

    private fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    private fun getNumberFormatted(valueResponse: Double): String {
        return try {
            val number = valueResponse.toString().split(".").getOrNull(0) ?: ""
            val decimalNumber = valueResponse.toString().split(".").getOrNull(1)?.getOrNull(0) ?: ""
            "$number.$decimalNumber"
        } catch (e: IndexOutOfBoundsException) {
            String.format("%.1f", valueResponse)
        }
    }

    companion object {
        const val ORDER_SUCCESS_RATE_INDEX = 0
        const val CHAT_DISCUSSION_REPLY_SPEED_INDEX = 1
        const val SPEED_SENDING_ORDERS_INDEX = 2
        const val CHAT_DISCUSSION_SPEED_INDEX = 3
        const val PRODUCT_REVIEW_WITH_FOUR_STARS_INDEX = 4
        const val TOTAL_BUYER_INDEX = 5
        const val OPEN_TOKOPEDIA_SELLER_INDEX = 6
    }
}