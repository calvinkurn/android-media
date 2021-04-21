package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.constant.TRANSITION_PERIOD
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScoreConstant.BRONZE_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_REPLY_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.CHAT_DISCUSSION_SPEED_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.COUNT_DAYS_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.DIAMOND_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.DOWN_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GOLD_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_BRONZE_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_DIAMOND_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_GOLD_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.GRADE_SILVER_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_INCOME_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_ORDER_PM_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_PM_VISITED_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.IC_SELLER_ANNOUNCE
import com.tokopedia.shop.score.common.ShopScoreConstant.NO_GRADE_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.OPEN_TOKOPEDIA_SELLER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.ORDER_SUCCESS_RATE_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTERN_DATE_NEW_SELLER
import com.tokopedia.shop.score.common.ShopScoreConstant.PATTER_DATE_TEXT
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
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ONE_HUNDRED
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SEVENTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_SIXTY_NINE
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_SCORE_ZERO
import com.tokopedia.shop.score.common.ShopScoreConstant.SILVER_SCORE
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.SPEED_SENDING_ORDERS_URL
import com.tokopedia.shop.score.common.ShopScoreConstant.STILL_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.SUPPOSED_INACTIVE_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.TOTAL_BUYER_KEY
import com.tokopedia.shop.score.common.ShopScoreConstant.UP_POTENTIAL_PM
import com.tokopedia.shop.score.common.ShopScoreConstant.dayText
import com.tokopedia.shop.score.common.ShopScoreConstant.minuteText
import com.tokopedia.shop.score.common.ShopScoreConstant.percentText
import com.tokopedia.shop.score.common.formatDate
import com.tokopedia.shop.score.common.getLocale
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
        val shopAge = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.shopAge.orZero()
        val isNewSeller = shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isNewSeller ?: false
        shopScoreVisitableList.apply {
            if (isNewSeller) {
                val mapTimerNewSeller = mapToTimerNewSellerUiModel(shopAge, shopInfoPeriodUiModel.isEndTenureNewSeller)
                if (mapTimerNewSeller.second) {
                    add(mapTimerNewSeller.first)
                    add(ItemLevelScoreProjectUiModel())
                }
            }

            add(mapToHeaderShopPerformance(shopScoreWrapperResponse.shopScoreLevelResponse?.result, shopAge))
            add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreWrapperResponse.shopScoreTooltipResponse?.result))
            if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
                addAll(mapToItemDetailPerformanceUiModel(shopScoreResult.shopScoreDetail))
            }

            if (shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools?.isNotEmpty() == true) {
                add(mapToItemRecommendationPMUiModel(shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools))
            }

            when {
                userSession.isShopOfficialStore || shopAge < SHOP_AGE_SIXTY -> {
                    add(SectionFaqUiModel(mapToItemFaqUiModel()))
                }
                userSession.isGoldMerchant && !userSession.isShopOfficialStore -> {
                    if (shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse != null &&
                            shopScoreWrapperResponse.goldGetPMStatusResponse != null) {
                        if (shopAge >= SHOP_AGE_SIXTY) {
                            if (shopInfoPeriodUiModel.periodType == TRANSITION_PERIOD) {
                                add(mapToTransitionPeriodReliefUiModel(shopInfoPeriodUiModel.periodEndDate))
                            }
                            add(mapToItemPotentialStatusPMUiModel(
                                    shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse,
                                    shopScoreWrapperResponse.goldGetPMStatusResponse,
                                    shopScoreWrapperResponse.goldGetPMShopInfoResponse,
                                    shopInfoPeriodUiModel,
                                    shopScoreWrapperResponse.reputationShopResponse,
                                    shopScoreResult?.shopScore.orZero()
                            ))
                        }
                    }
                }
                else -> {
                    if (isEligiblePM == true) {
                        if (shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse != null) {
                            add(mapToItemCurrentStatusRMUiModel(shopScoreWrapperResponse.goldPMGradeBenefitInfoResponse?.potentialPmGrade, shopInfoPeriodUiModel))
                        }
                    } else {
                        add(mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel))
                    }
                }
            }
        }
        return shopScoreVisitableList
    }

    private fun mapToHeaderShopPerformance(shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?, shopAge: Int): HeaderShopPerformanceUiModel {
        val headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        with(headerShopPerformanceUiModel) {
            when {
                shopAge < SHOP_AGE_SIXTY -> {
                    titleHeaderShopService = context?.getString(R.string.title_new_seller_level_0)
                            ?: ""
                    this.showCardNewSeller = true
                    val nextSellerDays = SHOP_AGE_SIXTY - shopAge
                    val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays)
                    val dateNewSellerProjection = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER)
                    descHeaderShopService = context?.getString(R.string.desc_new_seller_level_0, dateNewSellerProjection)
                            ?: ""
                }
                else -> {
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
                }
            }
            this.shopLevel = if (shopScoreLevelResponse?.shopLevel?.isZero() == true) "-" else shopScoreLevelResponse?.shopLevel?.toString().orEmpty()
            this.shopScore = if (shopScoreLevelResponse?.shopScore?.isZero() == true) "-" else shopScoreLevelResponse?.shopScore?.toString().orEmpty()
            this.scorePenalty = shopScoreLevelResponse?.shopScoreDetail?.find { it.identifier == PENALTY_IDENTIFIER }?.rawValue
        }
        return headerShopPerformanceUiModel
    }

    fun mapToShopInfoLevelUiModel(shopLevel: Int): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemDetailPerformanceUiModel(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {

            val multipleFilterShopScore = listOf(CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY,
                    ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY,
                    TOTAL_BUYER_KEY, OPEN_TOKOPEDIA_SELLER_KEY)

            val shopScoreLevelFilter = shopScoreLevelList?.filter { it.identifier in multipleFilterShopScore }
            val shopScoreLevelSize = shopScoreLevelFilter?.size.orZero()
            val sortShopScoreLevelParam = sortItemDetailPerformanceFormatted(shopScoreLevelFilter)
            sortShopScoreLevelParam.forEachIndexed { index, shopScoreDetail ->
                val (targetDetailPerformanceText, parameterItemDetailPerformance) = when (shopScoreDetail.identifier) {
                    CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY -> Pair("${shopScoreDetail.nextMinValue} $minuteText", minuteText)
                    ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY, TOTAL_BUYER_KEY ->
                        Pair("${shopScoreDetail.nextMinValue}$percentText", percentText)
                    OPEN_TOKOPEDIA_SELLER_KEY -> Pair("${shopScoreDetail.nextMinValue} $dayText", dayText)
                    else -> Pair("", "")
                }

                add(ItemDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = shopScoreDetail.rawValue.toString(),
                        colorValueDetailPerformance = shopScoreDetail.colorText,
                        targetDetailPerformance = targetDetailPerformanceText,
                        isDividerHide = index + 1 == shopScoreLevelSize,
                        identifierDetailPerformance = shopScoreDetail.identifier,
                        parameterValueDetailPerformance = parameterItemDetailPerformance
                ))
            }
        }
    }

    private fun sortItemDetailPerformanceFormatted(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?): List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail> {
        val identifierFilter = hashMapOf(
                ORDER_SUCCESS_RATE_KEY to 0,
                CHAT_DISCUSSION_REPLY_SPEED_KEY to 1,
                SPEED_SENDING_ORDERS_KEY to 2,
                CHAT_DISCUSSION_SPEED_KEY to 3,
                PRODUCT_REVIEW_WITH_FOUR_STARS_KEY to 4,
                TOTAL_BUYER_KEY to 5,
                OPEN_TOKOPEDIA_SELLER_KEY to 6
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

    private fun mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): SectionPotentialPMBenefitUiModel {
        return SectionPotentialPMBenefitUiModel(potentialPMBenefitList = mapToItemPotentialBenefit(), transitionEndDate = shopInfoPeriodUiModel.periodEndDate.formatDate(PATTER_DATE_TEXT))
    }

    private fun mapToSectionPeriodDetailPerformanceUiModel(shopScoreTooltipResponse: ShopLevelTooltipResponse.ShopLevel.Result?): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(period = shopScoreTooltipResponse?.period
                ?: "-", nextUpdate = shopScoreTooltipResponse?.nextUpdate ?: "-")
    }

    private fun mapToTransitionPeriodReliefUiModel(endDate: String): TransitionPeriodReliefUiModel {
        return TransitionPeriodReliefUiModel(dateTransitionPeriodRelief = endDate.formatDate(PATTER_DATE_TEXT), iconTransitionPeriodRelief = IC_SELLER_ANNOUNCE)
    }

    private fun mapToItemRecommendationPMUiModel(recommendationTools: List<GetRecommendationToolsResponse.ValuePropositionGetRecommendationTools.RecommendationTool>?): SectionShopRecommendationUiModel {
        return SectionShopRecommendationUiModel(
                recommendationShopList = mutableListOf<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>().apply {
                    recommendationTools?.forEach {
                        add(SectionShopRecommendationUiModel.ItemShopRecommendationUiModel(
                                iconRecommendationUrl = it.imageUrl,
                                appLinkRecommendation = it.relatedLinkAppLink,
                                descRecommendation = it.text,
                                titleRecommendation = it.title
                        ))
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
                                                  goldGetPMStatusResponse: GoldGetPMStatusResponse.GoldGetPMOSStatus?,
                                                  goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
                                                  shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
                                                  reputationShopResponse: List<ReputationShopResponse.ReputationShop>?,
                                                  shopScore: Int
    ): ItemStatusPMUiModel {
        val potentialPMGrade = goldPMGradeBenefitInfoResponse?.potentialPmGrade
        val currentPMGrade = goldPMGradeBenefitInfoResponse?.currentPmGrade
        val statusPowerMerchant = goldGetPMStatusResponse?.data?.powerMerchant?.status.orEmpty()
        val nextDate = goldPMGradeBenefitInfoResponse?.nextMonthlyRefreshDate.orEmpty()
        val titlePM =
                when (goldGetPMShopInfoResponse?.isNewSeller) {
                    true -> {
                        getTitlePMNewSellerSection(shopInfoPeriodUiModel)
                    }
                    else ->
                        when (shopInfoPeriodUiModel.periodType) {
                            ShopScoreConstant.TRANSITION_PERIOD -> {
                                getTitlePMExistingSellerTransitionPeriod(shopInfoPeriodUiModel, currentPMGrade)
                            }
                            ShopScoreConstant.END_PERIOD -> {
                                when (shopInfoPeriodUiModel.numberMonth) {
                                    ShopScoreConstant.ONE_MONTH, ShopScoreConstant.TWO_MONTH -> {
                                        getTitlePMExistingSellerFirstMonthEndPeriod(shopScore, goldGetPMShopInfoResponse, currentPMGrade)
                                    }
                                    ShopScoreConstant.THREE_MONTH -> {
                                        getTitlePMExistingSellerThreeMonthEndPeriod(shopScore, goldGetPMShopInfoResponse, currentPMGrade)
                                    }
                                    else -> ""
                                }
                            }
                            else -> {
                                ""
                            }
                        }
                }

        val statusPM = getStatusPowerMerchant(currentPMGrade, potentialPMGrade, goldGetPMShopInfoResponse)

        val bgPowerMerchant = getBackgroundPowerMerchant(shopInfoPeriodUiModel.isNewSeller, currentPMGrade)

        val (descStatus, isShowCardBg) = when (goldGetPMShopInfoResponse?.isNewSeller) {
            true -> {
                getStatusAndIsShowCardBgNewSellerTransition(statusPM, potentialPMGrade, shopInfoPeriodUiModel, reputationShopResponse)
            }
            else -> {
                when (shopInfoPeriodUiModel.periodType) {
                    ShopScoreConstant.TRANSITION_PERIOD -> {
                        when (shopInfoPeriodUiModel.numberMonth) {
                            ShopScoreConstant.ONE_MONTH, ShopScoreConstant.TWO_MONTH -> {
                                getStatusFirstTimeTransition(statusPM, potentialPMGrade)
                            }
                            ShopScoreConstant.THREE_MONTH -> {
                                getStatusEndTimeTransition(statusPM, potentialPMGrade)
                            }
                            else -> {
                                Pair("", false)
                            }
                        }
                    }
                    ShopScoreConstant.END_PERIOD -> {
                        when (shopInfoPeriodUiModel.numberMonth) {
                            ShopScoreConstant.ONE_MONTH, ShopScoreConstant.TWO_MONTH -> {
                                getStatusFirstTimeEndPeriod(statusPM, potentialPMGrade, goldGetPMShopInfoResponse, shopScore, statusPowerMerchant)
                            }
                            ShopScoreConstant.THREE_MONTH -> {
                                getStatusEndTimeEndPeriod(statusPM, goldGetPMShopInfoResponse, potentialPMGrade, shopScore)
                            }
                            else -> Pair("", false)
                        }
                    }
                    else -> Pair("", false)
                }
            }
        }

        return ItemStatusPMUiModel(
                statusPowerMerchant = currentPMGrade?.gradeName ?: "",
                titlePowerMerchant = titlePM,
                bgPowerMerchant = bgPowerMerchant,
                badgePowerMerchant = currentPMGrade?.imageBadgeUrl.orEmpty(),
                updateDatePotential = nextDate,
                descPotentialPM = descStatus,
                isActivePM = true,
                isCardBgColor = isShowCardBg,
                isInActivePM = statusPM == ShopScoreConstant.PM_IDLE
        )
    }

    private fun getStatusAndIsShowCardBgNewSellerTransition(statusPM: String,
                                                            potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?,
                                                            shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
                                                            reputationShopResponse: List<ReputationShopResponse.ReputationShop>?
    ): Pair<String, Boolean> {
        return when (statusPM) {
            SUPPOSED_INACTIVE_TEXT -> {
                when (shopInfoPeriodUiModel.numberMonth) {
                    ShopScoreConstant.THREE_MONTH -> {
                        Pair(context?.getString(R.string.desc_inactive_new_seller_end_month_status, SUPPOSED_INACTIVE_TEXT).orEmpty(), true)
                    }
                    else -> Pair(context?.getString(R.string.desc_inactive_new_seller_status, SUPPOSED_INACTIVE_TEXT).orEmpty(), true)
                }
            }
            else -> {
                val thresholdReputation = 5
                val hasScore = reputationShopResponse?.firstOrNull()?.score.toIntOrZero() < thresholdReputation
                if (hasScore) {
                    Pair(context?.getString(R.string.desc_new_seller_with_reputation_pm_status, potentialPMGrade?.gradeName).orEmpty(), false)
                } else {
                    Pair(context?.getString(R.string.desc_new_seller_no_reputation_pm_status).orEmpty(), false)
                }
            }
        }
    }

    private fun getStatusFirstTimeTransition(statusPM: String,
                                             potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?): Pair<String, Boolean> {
        return when (statusPM) {
            UP_POTENTIAL_PM, STILL_POTENTIAL_PM -> {
                Pair(context?.getString(R.string.desc_up_still_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
            }
            DOWN_POTENTIAL_PM -> {
                Pair(context?.getString(R.string.desc_down_pm_status, potentialPMGrade?.gradeName).orEmpty(), false)
            }
            SUPPOSED_INACTIVE_TEXT -> {
                Pair(context?.getString(R.string.desc_inactive_pm_status, SUPPOSED_INACTIVE_TEXT).orEmpty(), false)
            }
            else -> Pair("", false)
        }
    }

    private fun getStatusEndTimeTransition(statusPM: String,
                                           potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?): Pair<String, Boolean> {
        return when (statusPM) {
            UP_POTENTIAL_PM, STILL_POTENTIAL_PM -> {
                Pair(context?.getString(R.string.desc_up_still_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
            }
            DOWN_POTENTIAL_PM -> {
                Pair(context?.getString(R.string.desc_down_end_transition_warning_score_pm_status, potentialPMGrade?.gradeName).orEmpty(), true)
            }
            SUPPOSED_INACTIVE_TEXT -> {
                Pair(context?.getString(R.string.desc_inactive_end_transition_pm_status, SUPPOSED_INACTIVE_TEXT).orEmpty(), true)
            }
            else -> Pair("", false)
        }
    }

    private fun getStatusFirstTimeEndPeriod(statusPM: String,
                                            potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?,
                                            goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
                                            shopScore: Int,
                                            statusPowerMerchant: String
    ): Pair<String, Boolean> {
        return when (statusPM) {
            UP_POTENTIAL_PM, STILL_POTENTIAL_PM -> {
                when (shopScore) {
                    in goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()..SHOP_SCORE_SIXTY_NINE -> {
                        Pair(context?.getString(R.string.desc_up_still_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                    else -> {
                        Pair(context?.getString(R.string.desc_up_still_but_potential_down_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                }
            }
            DOWN_POTENTIAL_PM -> {
                when (shopScore) {
                    in goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()..SHOP_SCORE_SIXTY_NINE -> {
                        Pair(context?.getString(R.string.desc_down_end_transition_under_score_pm_status, potentialPMGrade?.gradeName).orEmpty(), true)
                    }
                    else -> {
                        Pair(context?.getString(R.string.desc_down_pm_end_game_status, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                }
            }
            else -> {
                when (statusPowerMerchant) {
                    ShopScoreConstant.PM_IDLE -> {
                        Pair(context?.getString(R.string.desc_inactive_pm_status, SUPPOSED_INACTIVE_TEXT).orEmpty(), false)
                    }
                    else -> Pair("", false)
                }
            }
        }
    }

    private fun getStatusEndTimeEndPeriod(statusPM: String,
                                          goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
                                          potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?,
                                          shopScore: Int
    ): Pair<String, Boolean> {
        return when (statusPM) {
            UP_POTENTIAL_PM, STILL_POTENTIAL_PM -> {
                when (shopScore) {
                    in goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()..SHOP_SCORE_SIXTY_NINE -> {
                        Pair(context?.getString(R.string.desc_up_still_but_potential_down_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                    else -> {
                        Pair(context?.getString(R.string.desc_up_still_pm_status, statusPM, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                }
            }
            DOWN_POTENTIAL_PM -> {
                when (shopScore) {
                    in goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()..SHOP_SCORE_SIXTY_NINE -> {
                        Pair(context?.getString(R.string.desc_down_end_transition_under_score_pm_status, potentialPMGrade?.gradeName).orEmpty(), true)
                    }
                    else -> {
                        Pair(context?.getString(R.string.desc_down_end_transition_warning_score_pm_status, potentialPMGrade?.gradeName).orEmpty(), false)
                    }
                }
            }
            else -> Pair("", false)
        }
    }

    private fun getStatusPowerMerchant(currentPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.CurrentPmGrade?,
                                       potentialPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?,
                                       goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?): String {
        return when {
            currentPMGrade?.gradeName?.mapToGradeNameToInt().orZero() > potentialPMGrade?.gradeName?.mapToGradeNameToInt().orZero() -> {
                UP_POTENTIAL_PM
            }
            currentPMGrade?.gradeName?.mapToGradeNameToInt() == potentialPMGrade?.gradeName?.mapToGradeNameToInt() -> {
                STILL_POTENTIAL_PM
            }
            currentPMGrade?.gradeName?.mapToGradeNameToInt().orZero() < potentialPMGrade?.gradeName?.mapToGradeNameToInt().orZero() -> {
                DOWN_POTENTIAL_PM
            }
            goldGetPMShopInfoResponse?.shopScoreSum.orZero() < goldGetPMShopInfoResponse?.shopScoreThreshold.orZero() -> {
                SUPPOSED_INACTIVE_TEXT
            }
            else -> ""
        }
    }

    private fun getBackgroundPowerMerchant(isNewSeller: Boolean, currentPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.CurrentPmGrade?): Int? {
        return when (isNewSeller) {
            true -> {
                R.drawable.bg_header_new_seller
            }
            else -> {
                when (currentPMGrade?.gradeName?.toLowerCase(Locale.getDefault())) {
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
            }
        }
    }

    private fun getTitlePMExistingSellerFirstMonthEndPeriod(shopScore: Int,
                                                            goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
                                                            currentPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.CurrentPmGrade?
    ): String {
        return when (shopScore) {
            in goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()..SHOP_SCORE_SIXTY_NINE -> {
                context?.getString(R.string.regarding_performance_reputation_pm_label).orEmpty()
            }
            else -> {
                if (goldGetPMShopInfoResponse?.shopScoreSum.orZero() < goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()) {
                    context?.getString(R.string.score_performance_pm_less_than_sixty_label).orEmpty()
                } else {
                    context?.getString(R.string.performance_regarding_pm_label, currentPMGrade?.lastUpdateDate.orEmpty()).orEmpty()
                }
            }
        }
    }

    private fun getTitlePMExistingSellerThreeMonthEndPeriod(shopScore: Int,
                                                            goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
                                                            currentPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.CurrentPmGrade?
    ): String {
        return when (shopScore) {
            in SHOP_SCORE_SIXTY..SHOP_SCORE_SIXTY_NINE -> {
                context?.getString(R.string.regarding_performance_reputation_pm_label).orEmpty()
            }
            else -> {
                if (goldGetPMShopInfoResponse?.shopScoreSum.orZero() < goldGetPMShopInfoResponse?.shopScoreThreshold.orZero()) {
                    context?.getString(R.string.score_performance_pm_less_than_sixty_label).orEmpty()
                } else {
                    context?.getString(R.string.performance_regarding_pm_label, currentPMGrade?.lastUpdateDate.orEmpty()).orEmpty()
                }
            }
        }
    }

    private fun getTitlePMExistingSellerTransitionPeriod(shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
                                                         currentPMGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.CurrentPmGrade?
    ): String {
        return when (shopInfoPeriodUiModel.numberMonth) {
            ShopScoreConstant.ONE_MONTH -> {
                context?.getString(R.string.current_regarding_penalty_pm_label).orEmpty()
            }
            ShopScoreConstant.TWO_MONTH, ShopScoreConstant.THREE_MONTH -> {
                context?.getString(R.string.performance_regarding_pm_label, currentPMGrade?.lastUpdateDate).orEmpty()
            }
            else -> ""
        }
    }

    private fun getTitlePMNewSellerSection(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): String {
        return when (shopInfoPeriodUiModel.periodType) {
            ShopScoreConstant.TRANSITION_PERIOD -> {
                context?.getString(R.string.score_performance_pm_new_seller_label).orEmpty()
            }
            else -> ""
        }
    }

    private fun String?.mapToGradeNameToInt()
            : Int {
        return when (this?.toLowerCase(Locale.getDefault())) {
            GRADE_BRONZE_PM -> BRONZE_SCORE
            GRADE_SILVER_PM -> SILVER_SCORE
            GRADE_GOLD_PM -> GOLD_SCORE
            GRADE_DIAMOND_PM -> DIAMOND_SCORE
            else -> NO_GRADE_SCORE
        }
    }

    private fun mapToItemCurrentStatusRMUiModel(potentialPmGrade: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo.PotentialPmGrade?, shopInfoPeriodUiModel: ShopInfoPeriodUiModel)
            : ItemStatusRMUiModel {
        //only regular merchant & eligible
        val updateDate = shopInfoPeriodUiModel.periodEndDate.formatDate(PATTER_DATE_TEXT)
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
                    isShow = true
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

    private fun mapToTimerNewSellerUiModel(shopAge: Int = 0, isEndTenure: Boolean)
            : Pair<ItemTimerNewSellerUiModel, Boolean> {
        val nextSellerDays = COUNT_DAYS_NEW_SELLER - shopAge

        val effectiveDate = getNNextDaysTimeCalendar(nextSellerDays)
        return Pair(ItemTimerNewSellerUiModel(effectiveDate = effectiveDate,
                effectiveDateText = format(effectiveDate.timeInMillis, PATTERN_DATE_NEW_SELLER),
                isTenureDate = isEndTenure,
                shopAge = shopAge
        ), nextSellerDays > 0)
    }

    private fun getNNextDaysTimeCalendar(nextDays: Int): Calendar {
        val date = Calendar.getInstance(getLocale())
        date.add(Calendar.DATE, nextDays + 1)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        return date
    }

    private fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }
}