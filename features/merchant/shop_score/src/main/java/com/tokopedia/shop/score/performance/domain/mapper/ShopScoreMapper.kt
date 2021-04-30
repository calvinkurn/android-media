package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gm.common.constant.NEW_SELLER_DAYS
import com.tokopedia.gm.common.constant.TRANSITION_PERIOD
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.isZero
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
                    descCalculation = R.string.desc_calculation_open_seller_app_within_90_days
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
        val shopAge = if (shopScoreWrapperResponse.goldGetPMShopInfoResponse != null) {
            shopScoreWrapperResponse.goldGetPMShopInfoResponse?.shopAge
        } else {
            shopInfoPeriodUiModel.shopAge
        } ?: 0
        val isNewSeller = if (shopScoreWrapperResponse.goldGetPMShopInfoResponse != null) {
            shopScoreWrapperResponse.goldGetPMShopInfoResponse?.isNewSeller
        } else {
            shopInfoPeriodUiModel.isNewSeller
        } ?: false
        shopScoreVisitableList.apply {
            if (isNewSeller || shopAge < SHOP_AGE_SIXTY) {
                val mapTimerNewSeller = mapToTimerNewSellerUiModel(shopAge, shopInfoPeriodUiModel.isEndTenureNewSeller)
                if (mapTimerNewSeller.second) {
                    add(mapTimerNewSeller.first)
                    add(ItemLevelScoreProjectUiModel())
                }
            }

            add(mapToHeaderShopPerformance(shopScoreWrapperResponse.shopScoreLevelResponse?.result, shopAge))
            add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreWrapperResponse.shopScoreTooltipResponse?.result))
            if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
                addAll(mapToItemDetailPerformanceUiModel(shopScoreResult.shopScoreDetail, shopAge))
            }

            if (shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools?.isNotEmpty() == true) {
                add(mapToItemRecommendationPMUiModel(shopScoreWrapperResponse.getRecommendationToolsResponse?.recommendationTools))
            }

            when {
                userSession.isShopOfficialStore || shopAge < SHOP_AGE_SIXTY -> {
                    add(SectionFaqUiModel(mapToItemFaqUiModel()))
                }
                userSession.isGoldMerchant && !userSession.isShopOfficialStore -> {
                    if (shopAge >= SHOP_AGE_SIXTY) {
                        if (shopInfoPeriodUiModel.periodType == TRANSITION_PERIOD) {
                            add(mapToTransitionPeriodReliefUiModel(shopInfoPeriodUiModel.periodEndDate))
                        }
                        add(mapToItemPMUiModel(shopAge))
                    }
                }
                else -> {
                    if (shopAge >= SHOP_AGE_SIXTY) {
                        val isNewSellerPM = shopAge in SHOP_AGE_SIXTY..NEW_SELLER_DAYS
                        if (isNewSellerPM) {
                            add(mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel, shopAge, isNewSellerPM))
                        } else {
                            if (isEligiblePM == true) {
                                add(mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel, shopAge, isNewSellerPM))
                            } else {
                                add(mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel))
                            }
                        }
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
                shopAge in SHOP_AGE_SIXTY..COUNT_DAYS_NEW_SELLER -> {
                    shopScoreLevelResponse?.let {
                        when {
                            it.shopScore < SHOP_SCORE_SIXTY -> {
                                titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_under_60)
                                        ?: ""
                            }
                            it.shopScore in SHOP_SCORE_SIXTY..SHOP_SCORE_SEVENTY_NINE -> {
                                titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_between_60_to_79)
                                        ?: ""
                            }
                            it.shopScore >= SHOP_SCORE_EIGHTY -> {
                                titleHeaderShopService = context?.getString(R.string.title_tenure_new_seller_score_more_80)
                                        ?: ""
                            }
                        }
                        descHeaderShopService = context?.getString(R.string.desc_tenure_new_seller)
                                ?: ""
                    }
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
            this.shopLevel =
                    if (shopAge < SHOP_AGE_SIXTY) {
                        "-"
                    } else {
                        if (shopScoreLevelResponse?.shopLevel?.isZero() == true) "-" else shopScoreLevelResponse?.shopLevel?.toString().orEmpty()
                    }
            this.shopScore =
                    if (shopAge < SHOP_AGE_SIXTY) {
                        "-"
                    } else {
                        if (shopScoreLevelResponse?.shopScore?.isZero() == true) "-" else shopScoreLevelResponse?.shopScore?.toString().orEmpty()
                    }
            this.scorePenalty = shopScoreLevelResponse?.shopScoreDetail?.find { it.identifier == PENALTY_IDENTIFIER }?.rawValue
        }
        return headerShopPerformanceUiModel
    }

    fun mapToShopInfoLevelUiModel(shopLevel: Int): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemDetailPerformanceUiModel(shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?, shopAge: Int): List<ItemDetailPerformanceUiModel> {
        return mutableListOf<ItemDetailPerformanceUiModel>().apply {

            val multipleFilterShopScore = listOf(CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY,
                    ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY,
                    TOTAL_BUYER_KEY, OPEN_TOKOPEDIA_SELLER_KEY)

            val shopScoreLevelFilter = shopScoreLevelList?.filter { it.identifier in multipleFilterShopScore }
            val shopScoreLevelSize = shopScoreLevelFilter?.size.orZero()
            val sortShopScoreLevelParam = sortItemDetailPerformanceFormatted(shopScoreLevelFilter)
            sortShopScoreLevelParam.forEachIndexed { index, shopScoreDetail ->
                val (targetDetailPerformanceText, parameterItemDetailPerformance) =
                        when (shopScoreDetail.identifier) {
                            CHAT_DISCUSSION_REPLY_SPEED_KEY, SPEED_SENDING_ORDERS_KEY -> Pair("${shopScoreDetail.nextMinValue} $minuteText", minuteText)
                            ORDER_SUCCESS_RATE_KEY, CHAT_DISCUSSION_SPEED_KEY, PRODUCT_REVIEW_WITH_FOUR_STARS_KEY ->
                                Pair("${shopScoreDetail.nextMinValue * ONE_HUNDRED_PERCENT}$percentText", percentText)
                            TOTAL_BUYER_KEY -> Pair("${shopScoreDetail.nextMinValue} $peopleText", peopleText)
                            OPEN_TOKOPEDIA_SELLER_KEY -> Pair("${shopScoreDetail.nextMinValue} $dayText", dayText)
                            else -> Pair("-", "")
                        }

                add(ItemDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = if (shopAge < SHOP_AGE_SIXTY) "-" else shopScoreDetail.rawValue.toString(),
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

    private fun mapToItemPMUiModel(shopAge: Int): ItemStatusPMUiModel {
        val isNewSeller = shopAge in SHOP_AGE_SIXTY..NEW_SELLER_DAYS
        return ItemStatusPMUiModel(isNewSeller = isNewSeller,
                descPM = if (isNewSeller)
                    context?.getString(R.string.desc_pm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.desc_content_pm_section).orEmpty())
    }

    private fun mapToCardPotentialBenefitNonEligible(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): SectionPotentialPMBenefitUiModel {
        return SectionPotentialPMBenefitUiModel(potentialPMBenefitList = mapToItemPotentialBenefit(),
                transitionEndDate = shopInfoPeriodUiModel.periodEndDate.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT))
    }

    private fun mapToSectionPeriodDetailPerformanceUiModel(shopScoreTooltipResponse: ShopLevelTooltipResponse.ShopLevel.Result?): PeriodDetailPerformanceUiModel {
        return PeriodDetailPerformanceUiModel(period = shopScoreTooltipResponse?.period
                ?: "-", nextUpdate = shopScoreTooltipResponse?.nextUpdate ?: "-")
    }

    private fun mapToTransitionPeriodReliefUiModel(endDate: String): TransitionPeriodReliefUiModel {
        return TransitionPeriodReliefUiModel(dateTransitionPeriodRelief = endDate.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT), iconTransitionPeriodRelief = IC_SELLER_ANNOUNCE)
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

    private fun mapToSectionPMPro(shopInfoPeriodUiModel: ShopInfoPeriodUiModel): SectionPotentialPMProUiModel {
        return SectionPotentialPMProUiModel(potentialPMProPMBenefitList = mapToItemPMProBenefit(), transitionEndDate = shopInfoPeriodUiModel.periodEndDate.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT))
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

    private fun mapToItemCurrentStatusRMUiModel(shopInfoPeriodUiModel: ShopInfoPeriodUiModel, shopAge: Int, isNewSeller)
            : ItemStatusRMUiModel {
        val updateDate = shopInfoPeriodUiModel.periodEndDate.formatDate(PATTERN_PERIOD_DATE, PATTERN_DATE_TEXT)
        return ItemStatusRMUiModel(updateDatePotential = updateDate,
                titleRMEligible =
                if (isNewSeller)
                    context?.getString(R.string.title_header_rm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.title_header_rm_section).orEmpty(),
                descRMEligible = if (isNewSeller)
                    context?.getString(R.string.desc_potential_rm_section_new_seller).orEmpty()
                else
                    context?.getString(R.string.desc_potential_eligible_power_merchant, updateDate).orEmpty()
        )
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