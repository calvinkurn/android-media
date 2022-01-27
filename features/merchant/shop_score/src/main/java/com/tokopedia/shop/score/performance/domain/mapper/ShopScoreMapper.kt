package com.tokopedia.shop.score.performance.domain.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceScreenInfo
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
import com.tokopedia.shop.score.performance.presentation.model.tablet.BaseParameterDetail
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemDetailPerformanceTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.PeriodDetailTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel
import com.tokopedia.user.session.UserSessionInterface
import java.text.ParseException
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToLong

open class ShopScoreMapper @Inject constructor(
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
            when {
                isNewSeller || shopAge < NEW_SELLER_DAYS -> {
                    val isEndTenureNewSeller = GoldMerchantUtil.isTenureNewSeller(
                        shopAge
                    )
                    val mapTimerNewSeller =
                        mapToTimerNewSellerUiModel(
                            shopAge,
                            isEndTenureNewSeller, shopScore.toInt()
                        )
                    if (mapTimerNewSeller.second) {
                        add(mapTimerNewSeller.first)
                        if (shopAge < SHOP_AGE_NINETY_SIX && shopScore > 0) {
                            add(ItemLevelScoreProjectUiModel())
                        }
                    }
                }
                isReactivatedSellerBeforeFirstMonday(shopScore, shopAge) -> {
                    if (shopScorePrefManager.getIsNeedShowTickerReactivated()) {
                        add(TickerReactivatedUiModel())
                    }
                }
                isReactivatedSellerAfterComeback(shopScore, shopScoreResult?.shopScoreDetail) -> {
                    add(ItemReactivatedComebackUiModel())
                }
            }

            try {
                if (context != null) {
                    if (DeviceScreenInfo.isTablet(context)) {
                        addHeaderDetailParameterTabletMode(
                            shopScoreWrapperResponse,
                            shopInfoPeriodUiModel,
                            powerMerchantData,
                            shopScore,
                            shopAge,
                            isNewSeller
                        )
                    } else {
                        addDetailParameterMobileMode(
                            shopScoreWrapperResponse,
                            shopInfoPeriodUiModel,
                            powerMerchantData,
                            shopScoreResult,
                            shopScore,
                            shopAge,
                            isNewSeller
                        )
                    }
                } else {
                    addDetailParameterMobileMode(
                        shopScoreWrapperResponse,
                        shopInfoPeriodUiModel,
                        powerMerchantData,
                        shopScoreResult,
                        shopScore,
                        shopAge,
                        isNewSeller
                    )
                }
            } catch (e: Exception) {
                addDetailParameterMobileMode(
                    shopScoreWrapperResponse,
                    shopInfoPeriodUiModel,
                    powerMerchantData,
                    shopScoreResult,
                    shopScore,
                    shopAge,
                    isNewSeller
                )
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
                    add(
                        SectionFaqUiModel(
                            mapToItemFaqUiModel(
                                isNewSeller,
                                isOfficialStore,
                                powerMerchantResponse,
                                shopScore,
                                shopAge,
                                shopScoreResult?.shopScoreDetail
                            )
                        )
                    )
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

    private fun MutableList<BaseShopPerformance>.addHeaderDetailParameterTabletMode(
        shopScoreWrapperResponse: ShopScoreWrapperResponse,
        shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
        powerMerchantData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        shopScore: Long,
        shopAge: Long,
        isNewSeller: Boolean
    ) {
        val detailParameterList = mutableListOf<BaseParameterDetail>()
        val shopScoreDetail =
            shopScoreWrapperResponse.shopScoreLevelResponse?.result?.shopScoreDetail

        val periodDetailList = mapToSectionPeriodDetailPerformanceTabletUiModel(
            shopScoreWrapperResponse.shopScoreLevelResponse?.result,
            isNewSeller
        )

        detailParameterList.add(periodDetailList)

        val itemDetailPerformanceUiModel =
            mapToItemDetailPerformanceTabletUiModel(
                shopScoreDetail,
                shopAge,
                shopScore,
                shopInfoPeriodUiModel.dateShopCreated
            )

        if (!itemDetailPerformanceUiModel.isNullOrEmpty()) {
            detailParameterList.addAll(itemDetailPerformanceUiModel)
        }

        if (isShowProtectedParameterNewSeller(
                shopAge.toInt(),
                shopInfoPeriodUiModel.dateShopCreated
            ) ||
            isReactivatedSellerAfterComeback(
                shopScore, shopScoreDetail
            )
        ) {
            val protectedParameterSectionTablet =
                mapToItemProtectedParameterTabletUiModel(
                    shopScoreDetail,
                    shopAge
                )
            detailParameterList.add(protectedParameterSectionTablet)
        }

        val itemHeaderParameterDetailUiModel = ItemHeaderParameterDetailUiModel(
            headerShopPerformanceUiModel = mapToHeaderShopPerformance(
                shopScoreWrapperResponse.shopScoreLevelResponse?.result,
                powerMerchantData,
                shopAge,
                shopInfoPeriodUiModel.dateShopCreated
            ),
            detailParameterList = detailParameterList
        )
        add(itemHeaderParameterDetailUiModel)
    }

    private fun MutableList<BaseShopPerformance>.addDetailParameterMobileMode(
        shopScoreWrapperResponse: ShopScoreWrapperResponse,
        shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
        powerMerchantData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        shopScoreResult: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        shopScore: Long,
        shopAge: Long,
        isNewSeller: Boolean
    ) {
        addHeaderShopPerformance(
            shopScoreWrapperResponse,
            shopInfoPeriodUiModel,
            powerMerchantData,
            shopAge,
        )

        add(mapToSectionPeriodDetailPerformanceUiModel(shopScoreResult, isNewSeller))

        if (shopScoreResult?.shopScoreDetail?.isNotEmpty() == true) {
            val itemDetailPerformanceUiModel =
                mapToItemDetailPerformanceUiModel(
                    shopScoreResult.shopScoreDetail,
                    shopAge,
                    shopScore,
                    shopInfoPeriodUiModel.dateShopCreated
                )
            addAll(itemDetailPerformanceUiModel)
        }

        if (isShowProtectedParameterNewSeller(
                shopAge.toInt(),
                shopInfoPeriodUiModel.dateShopCreated
            ) ||
            isReactivatedSellerAfterComeback(
                shopScore, shopScoreResult?.shopScoreDetail
            )
        ) {
            val protectedParameterSection =
                mapToItemProtectedParameterUiModel(
                    shopScoreResult?.shopScoreDetail,
                    shopAge
                )
            add(protectedParameterSection)
        }
    }

    private fun MutableList<BaseShopPerformance>.addHeaderShopPerformance(
        shopScoreWrapperResponse: ShopScoreWrapperResponse,
        shopInfoPeriodUiModel: ShopInfoPeriodUiModel,
        powerMerchantData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        shopAge: Long
    ) {
        add(
            mapToHeaderShopPerformance(
                shopScoreWrapperResponse.shopScoreLevelResponse?.result,
                powerMerchantData,
                shopAge,
                shopInfoPeriodUiModel.dateShopCreated
            )
        )
    }

    fun mapToHeaderShopPerformance(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        powerMerchantResponse: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        shopAge: Long,
        dateShopCreated: String
    ): HeaderShopPerformanceUiModel {
        var headerShopPerformanceUiModel = HeaderShopPerformanceUiModel()
        val shopScore = shopScoreLevelResponse?.shopScore ?: SHOP_SCORE_NULL
        val shopLevel = shopScoreLevelResponse?.shopLevel ?: SHOP_SCORE_NULL
        with(headerShopPerformanceUiModel) {
            when {
                isNewSellerBeforeFirstMonday(shopScore, shopAge) -> {
                    titleHeaderShopService = R.string.title_new_seller_level_0
                    this.showCard = true
                    descHeaderShopService = R.string.desc_new_seller_level_0
                }
                isNewSellerAfterFirstMonday(shopScore, shopAge) -> {
                    when {
                        shopScore < SHOP_SCORE_SIXTY -> {
                            titleHeaderShopService = R.string.title_tenure_new_seller_score_under_60
                        }
                        shopScore in SHOP_SCORE_SIXTY..SHOP_SCORE_SEVENTY_NINE -> {
                            titleHeaderShopService =
                                R.string.title_tenure_new_seller_score_between_60_to_79
                        }
                        shopScore >= SHOP_SCORE_EIGHTY -> {
                            titleHeaderShopService = R.string.title_tenure_new_seller_score_more_80
                        }
                    }
                    descHeaderShopService = R.string.desc_tenure_new_seller
                }
                isReactivatedSellerBeforeFirstMonday(shopScore, shopAge) -> {
                    titleHeaderShopService = R.string.title_reactivated_seller_level_0
                    this.showCard = true
                    descHeaderShopService = R.string.desc_reactivated_seller_level_0
                }
                isReactivatedSellerAfterComeback(
                    shopScore,
                    shopScoreLevelResponse?.shopScoreDetail
                ) -> {
                    headerShopPerformanceUiModel = mapToHeaderPerformanceExistingSeller(
                        shopScore,
                        shopLevel,
                        headerShopPerformanceUiModel
                    )
                }
                else -> {
                    headerShopPerformanceUiModel = mapToHeaderPerformanceExistingSeller(
                        shopScore,
                        shopLevel,
                        headerShopPerformanceUiModel
                    )
                }
            }
            this.powerMerchantData = powerMerchantResponse
            this.shopAge = shopAge
            this.shopLevel =
                if (shopLevel < SHOP_SCORE_ZERO) {
                    "-"
                } else {
                    shopLevel.toString()
                }

            this.shopScore =
                if (shopScore < SHOP_SCORE_ZERO) {
                    "-"
                } else {
                    shopScore.toString()
                }

            this.scorePenalty =
                shopScoreLevelResponse?.shopScoreDetail?.find {
                    it.identifier == PENALTY_IDENTIFIER
                }?.rawValue?.roundToLong().orZero()

            this.isShowPopupEndTenure = getIsShowPopupEndTenure(
                dateShopCreated,
                shopScore,
                shopAge,
                shopScoreLevelResponse?.shopScoreDetail
            )
        }
        return headerShopPerformanceUiModel
    }

    private fun mapToHeaderPerformanceExistingSeller(
        shopScore: Long,
        shopLevel: Long,
        headerShopPerformanceUiModel: HeaderShopPerformanceUiModel
    ): HeaderShopPerformanceUiModel {
        return headerShopPerformanceUiModel.apply {
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
        }
    }

    fun mapToShopInfoLevelUiModel(shopLevel: Long): ShopInfoLevelUiModel {
        val shopInfoLevelUiModel = ShopInfoLevelUiModel()
        shopInfoLevelUiModel.cardTooltipLevelList = mapToCardTooltipLevel(shopLevel)
        return shopInfoLevelUiModel
    }

    private fun mapToItemProtectedParameterTabletUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long
    ): ProtectedParameterTabletUiModel {
        val protectedParameterSection = getProtectedParameterSection(
            shopScoreLevelList, shopAge.toInt()
        )

        return ProtectedParameterTabletUiModel(
            itemProtectedParameterList = protectedParameterSection.itemProtectedParameterList,
            titleParameterRelief = protectedParameterSection.titleParameterRelief,
            descParameterRelief = protectedParameterSection.descParameterRelief,
            descParameterReliefBottomSheet = protectedParameterSection.descParameterReliefBottomSheet,
            protectedParameterDaysDate = protectedParameterSection.protectedParameterDaysDate
        )
    }

    private fun mapToItemProtectedParameterUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long
    ): ProtectedParameterSectionUiModel {
        val protectedParameterSection = getProtectedParameterSection(
            shopScoreLevelList, shopAge.toInt()
        )

        return ProtectedParameterSectionUiModel(
            itemProtectedParameterList = protectedParameterSection.itemProtectedParameterList,
            titleParameterRelief = protectedParameterSection.titleParameterRelief,
            descParameterRelief = protectedParameterSection.descParameterRelief,
            descParameterReliefBottomSheet = protectedParameterSection.descParameterReliefBottomSheet,
            protectedParameterDaysDate = protectedParameterSection.protectedParameterDaysDate
        )
    }

    private fun mapToItemDetailPerformanceTabletUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long,
        shopScore: Long,
        dateShopCreated: String
    ): List<ItemDetailPerformanceTabletUiModel> {
        val basePeriodDetailPerformanceUiModel = mapToBaseDetailPerformanceUiModel(
            shopScoreLevelList, shopAge, shopScore, dateShopCreated
        )

        return basePeriodDetailPerformanceUiModel.map {
            ItemDetailPerformanceTabletUiModel(
                titleDetailPerformance = it.titleDetailPerformance,
                valueDetailPerformance = it.valueDetailPerformance,
                colorValueDetailPerformance = it.colorValueDetailPerformance,
                targetDetailPerformance = it.targetDetailPerformance,
                isDividerHide = it.isDividerHide,
                identifierDetailPerformance = it.identifierDetailPerformance,
                parameterValueDetailPerformance = it.parameterValueDetailPerformance,
                shopAge = it.shopAge,
                shopScore = it.shopScore
            )
        }
    }

    fun mapToItemDetailPerformanceUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long,
        shopScore: Long,
        dateShopCreated: String
    ): List<ItemDetailPerformanceUiModel> {
        val basePeriodDetailPerformanceUiModel = mapToBaseDetailPerformanceUiModel(
            shopScoreLevelList, shopAge, shopScore, dateShopCreated
        )

        return basePeriodDetailPerformanceUiModel.map {
            ItemDetailPerformanceUiModel(
                titleDetailPerformance = it.titleDetailPerformance,
                valueDetailPerformance = it.valueDetailPerformance,
                colorValueDetailPerformance = it.colorValueDetailPerformance,
                targetDetailPerformance = it.targetDetailPerformance,
                isDividerHide = it.isDividerHide,
                identifierDetailPerformance = it.identifierDetailPerformance,
                parameterValueDetailPerformance = it.parameterValueDetailPerformance,
                shopAge = shopAge,
                shopScore = shopScore
            )
        }
    }

    fun mapToBaseDetailPerformanceUiModel(
        shopScoreLevelList: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Long,
        shopScore: Long,
        dateShopCreated: String
    ): List<BaseDetailPerformanceUiModel> {
        return mutableListOf<BaseDetailPerformanceUiModel>().apply {

            val multipleFilterShopScore = listOf(
                CHAT_DISCUSSION_REPLY_SPEED_KEY,
                SPEED_SENDING_ORDERS_KEY,
                ORDER_SUCCESS_RATE_KEY,
                CHAT_DISCUSSION_SPEED_KEY,
                PRODUCT_REVIEW_WITH_FOUR_STARS_KEY,
                TOTAL_BUYER_KEY,
                OPEN_TOKOPEDIA_SELLER_KEY
            )

            val isReactivatedSellerAfterComeback =
                isReactivatedSellerAfterComeback(shopScore, shopScoreLevelList)

            val isShowProtectedParameter =
                isShowProtectedParameterNewSeller(shopAge.toInt(), dateShopCreated) ||
                        isReactivatedSellerAfterComeback

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
                    BaseDetailPerformanceUiModel(
                        titleDetailPerformance = shopScoreDetail.title,
                        valueDetailPerformance = rawValueFormatted,
                        colorValueDetailPerformance = shopScoreDetail.colorText,
                        targetDetailPerformance = if (isReactivatedSellerBeforeFirstMonday(
                                shopScore,
                                shopAge
                            )
                        ) "" else targetDetailPerformanceText,
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
            descPM = R.string.desc_content_pm_not_eligible_pm_pro
        )
    }

    private fun mapToCardPotentialBenefitNonEligible(): SectionRMPotentialPMBenefitUiModel {
        return SectionRMPotentialPMBenefitUiModel(
            potentialPMBenefitList = mapToItemPotentialBenefit()
        )
    }

    private fun mapToSectionPeriodDetailPerformanceTabletUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        isNewSeller: Boolean
    ): PeriodDetailTabletUiModel {
        val mapToBasePeriodDetailPerformanceUiModel = mapToBasePeriodDetailPerformanceUiModel(
            shopScoreLevelResponse,
            isNewSeller
        )
        return PeriodDetailTabletUiModel(
            period = mapToBasePeriodDetailPerformanceUiModel.period,
            nextUpdate = mapToBasePeriodDetailPerformanceUiModel.nextUpdate,
            isNewSeller = mapToBasePeriodDetailPerformanceUiModel.isNewSeller
        )
    }

    fun mapToSectionPeriodDetailPerformanceUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        isNewSeller: Boolean
    ): PeriodDetailPerformanceUiModel {
        val basePeriodDetailPerformance = mapToBasePeriodDetailPerformanceUiModel(
            shopScoreLevelResponse,
            isNewSeller
        )
        return PeriodDetailPerformanceUiModel(
            period = basePeriodDetailPerformance.period,
            nextUpdate = basePeriodDetailPerformance.nextUpdate,
            isNewSeller = basePeriodDetailPerformance.isNewSeller
        )
    }

    private fun mapToBasePeriodDetailPerformanceUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        isNewSeller: Boolean
    ): BasePeriodDetailUiModel {
        return BasePeriodDetailUiModel(
            period = shopScoreLevelResponse?.period
                ?: "-", nextUpdate = shopScoreLevelResponse?.nextUpdate
                ?: "-", isNewSeller = isNewSeller
        )
    }

    private fun mapToItemRecommendationPMUiModel(
        recommendationTools: List<GetRecommendationToolsResponse
        .ValuePropositionGetRecommendationTools.RecommendationTool>?
    ): SectionShopRecommendationUiModel {
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
                R.string.title_header_rm_section_new_seller
            else
                R.string.title_header_rm_section,
            descRMEligible = if (isNewSellerProjection)
                R.string.desc_potential_rm_section_new_seller
            else
                R.string.desc_potential_eligible_power_merchant
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

    fun mapToItemFaqUiModel(
        isNewSeller: Boolean,
        isOfficialStore: Boolean,
        pmData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data.PowerMerchant?,
        shopScore: Long,
        shopAge: Long,
        shopScoreDetail: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?
    ): List<ItemFaqUiModel> {
        val isReactivatedBeforeMonday =
            isReactivatedSellerBeforeFirstMonday(shopScore, shopAge)
        val isReactivatedAfterMonday =
            isReactivatedSellerAfterComeback(shopScore, shopScoreDetail)
        return mutableListOf<ItemFaqUiModel>().apply {
            add(
                ItemFaqUiModel(
                    title = R.string.title_shop_score_performance,
                    desc_first = R.string.desc_shop_score_performance,
                    isShow = true
                )
            )
            add(
                ItemFaqUiModel(
                    title = R.string.title_shop_score_benefit,
                    desc_first = R.string.desc_shop_score_benefit,
                )
            )
            add(
                ItemFaqUiModel(
                    title = R.string.title_shop_score_calculation,
                    desc_first = R.string.desc_section_1_shop_score_calculation,
                    desc_second = R.string.desc_section_2_shop_score_calculation,
                    isCalculationScore = true,
                    cardLevelList = mapToCardTooltipLevel(),
                    parameterFaqList = mapToItemParameterFaq()
                )
            )
            add(
                ItemFaqUiModel(
                    title = R.string.title_calculate_shop_performance_if_parameter_value_missing,
                    desc_first = R.string.desc_calculate_shop_performance_if_parameter_value_missing,
                )
            )

            if (!isOfficialStore) {
                add(
                    ItemFaqUiModel(
                        title = R.string.title_calculate_shop_performance_for_new_seller,
                        desc_first = R.string.desc_calculate_shop_performance_for_new_seller,
                    )
                )
            }

            if (isOfficialStore) {
                add(
                    ItemFaqUiModel(
                        title = R.string.title_shop_score_affect_os,
                        desc_first = R.string.desc_shop_score_affect_os,
                    )
                )
            }

            if (isNewSeller) {
                add(
                    ItemFaqUiModel(
                        title = R.string.title_time_adjustment_what_relief_for_new_seller,
                        desc_first = R.string.desc_time_adjustment_what_relief_for_new_seller,
                    )
                )
            }

            if (isReactivatedBeforeMonday || isReactivatedAfterMonday) {
                add(
                    ItemFaqUiModel(
                        title = R.string.title_time_adjustment_what_relief_for_reactivated_seller,
                        desc_first = R.string.desc_time_adjustment_what_relief_for_reactivated_seller,
                    )
                )
            }

            if (isReactivatedBeforeMonday && isPowerMerchantOrProIdle(pmData)) {
                add(
                    ItemFaqUiModel(
                        title = R.string.title_pm_or_pro_reactivated_seller_faq,
                        desc_first = R.string.desc_pm_or_pro_reactivated_seller_faq,
                    )
                )
            }
        }
    }

    private fun mapToItemParameterFaq(): List<ItemParameterFaqUiModel> {
        return listOf(
            ItemParameterFaqUiModel(
                title = R.string.title_parameter_shop_score_1,
                desc = R.string.desc_parameter_shop_score_1,
                score = R.string.score_parameter_shop_score_1
            ),
            ItemParameterFaqUiModel(
                title = R.string.title_parameter_shop_score_2,
                desc = R.string.desc_parameter_shop_score_2,
                score = R.string.score_parameter_shop_score_2
            ),
            ItemParameterFaqUiModel(
                title = R.string.title_parameter_shop_score_3,
                desc = R.string.desc_parameter_shop_score_3,
                score = R.string.score_parameter_shop_score_3
            )
        )
    }

    fun mapToTimerNewSellerUiModel(shopAge: Long = 0, isEndTenure: Boolean, shopScore: Int)
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

    fun getProtectedParameterSection(
        shopScoreLevelList:
        List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?,
        shopAge: Int
    ): BaseProtectedParameterSectionUiModel {
        val totalBuyer =
            shopScoreLevelList?.find { it.identifier == TOTAL_BUYER_KEY }?.title.orEmpty()
        val openTokopediaSeller =
            shopScoreLevelList?.find { it.identifier == OPEN_TOKOPEDIA_SELLER_KEY }?.title.orEmpty()
        val (titleParameterRelief, descParameterRelief, descParameterReliefBottomSheet) =
            if (getSellerType(shopScoreLevelList) == SellerTypeConstants.REACTIVATED_SELLER) {
                Triple(
                    R.string.title_relief_parameter_for_new_seller_bottom_sheet,
                    R.string.desc_relief_parameter_for_reactivated_seller,
                    R.string.desc_relief_parameter_for_reactivated_seller_bottom_sheet
                )
            } else {
                Triple(
                    R.string.title_parameter_relief_new_seller,
                    R.string.desc_new_seller_parameter_relief,
                    R.string.desc_relief_parameter_for_new_seller_bottom_sheet
                )
            }

        return BaseProtectedParameterSectionUiModel(
            itemProtectedParameterList = listOf(
                ItemProtectedParameterUiModel(totalBuyer),
                ItemProtectedParameterUiModel(openTokopediaSeller)
            ),
            titleParameterRelief = titleParameterRelief,
            descParameterRelief = descParameterRelief,
            descParameterReliefBottomSheet = descParameterReliefBottomSheet,
            protectedParameterDaysDate = getProtectedParameterDaysDate(shopAge)
        )
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

    private fun getIsShowPopupEndTenure(
        dateShopCreated: String,
        shopScore: Long,
        shopAge: Long,
        shopScoreDetail: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?
    ): Boolean {
        return if (shopScorePrefManager.getIsShowPopupEndTenure()
            && !isReactivatedSellerBeforeFirstMonday(shopScore, shopAge)
            && !isReactivatedSellerAfterComeback(shopScore, shopScoreDetail)
        ) {
            return getIsShowPopupCelebratoryEdgeCases(dateShopCreated)
        } else false
    }

    private fun getIsShowPopupCelebratoryEdgeCases(
        dateShopCreated: String
    ): Boolean {
        val shopAge = GoldMerchantUtil.totalDays(dateShopCreated)
        return when (GoldMerchantUtil.getDayNameFromCreatedDate(dateShopCreated)) {
            Calendar.SUNDAY -> {
                false
            }
            Calendar.MONDAY -> {
                shopAge in SHOP_AGE_NINETY..SHOP_AGE_NINETY_SIX
            }
            Calendar.TUESDAY -> {
                shopAge in SHOP_AGE_NINETY_ONE..SHOP_AGE_NINETY_SEVEN
            }
            Calendar.WEDNESDAY -> {
                shopAge in SHOP_AGE_NINETY_TWO..SHOP_AGE_NINETY_EIGHT
            }
            Calendar.THURSDAY -> {
                shopAge in SHOP_AGE_NINETY_THREE..SHOP_AGE_NINETY_NINE
            }
            Calendar.FRIDAY -> {
                shopAge in SHOP_AGE_NINETY_FOUR..SHOP_AGE_ONE_HUNDRED
            }
            Calendar.SATURDAY -> {
                shopAge in SHOP_AGE_NINETY_FIVE..SHOP_AGE_ONE_HUNDRED_ONE
            }
            else -> false
        }
    }

    private fun isShowProtectedParameterNewSeller(shopAge: Int, dateShopCreated: String): Boolean {
        return shopAge in GoldMerchantUtil.getNNStartShowProtectedParameterNewSeller(dateShopCreated)..SHOP_AGE_FIFTY_NINE
    }

    private fun isReactivatedSellerBeforeFirstMonday(
        shopScore: Long,
        shopAge: Long
    ): Boolean {
        return shopAge > SHOP_AGE_NINETY && shopScore < SHOP_SCORE_ZERO
    }

    private fun isReactivatedSellerAfterComeback(
        shopScore: Long,
        shopScoreDetail: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?
    ): Boolean {
        return getSellerType(shopScoreDetail) == SellerTypeConstants.REACTIVATED_SELLER
                && shopScore > SHOP_SCORE_NULL
    }

    private fun isNewSellerBeforeFirstMonday(shopScore: Long, shopAge: Long): Boolean {
        return shopScore < SHOP_SCORE_ZERO && shopAge < SHOP_AGE_NINETY
    }

    private fun isNewSellerAfterFirstMonday(shopScore: Long, shopAge: Long): Boolean {
        return shopScore > SHOP_SCORE_NULL && shopAge < SHOP_AGE_NINETY
    }

    private fun getSellerType(
        shopScoreDetail: List<ShopScoreLevelResponse.ShopScoreLevel.Result.ShopScoreDetail>?
    ): Double? {
        return shopScoreDetail?.find {
            it.identifier == SellerTypeConstants.SELLER_TYPE_IDENTIFIER
        }?.rawValue
    }

    private fun isPowerMerchantOrProIdle(
        pmResponse: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data.PowerMerchant?
    ): Boolean {
        val isPmIdle =
            pmResponse?.pmTier == PMTier.REGULAR && pmResponse.status == PMStatusConst.IDLE
        val isPmProIdle =
            pmResponse?.pmTier == PMTier.PRO && pmResponse.status == PMStatusConst.IDLE

        return isPmIdle || isPmProIdle
    }

    companion object {
        const val SHOP_AGE_NINETY = 90
        const val SHOP_AGE_NINETY_SIX = 96
        const val SHOP_AGE_FIFTY_NINE = 59
        const val ORDER_SUCCESS_RATE_INDEX = 0
        const val CHAT_DISCUSSION_REPLY_SPEED_INDEX = 1
        const val SPEED_SENDING_ORDERS_INDEX = 2
        const val CHAT_DISCUSSION_SPEED_INDEX = 3
        const val PRODUCT_REVIEW_WITH_FOUR_STARS_INDEX = 4
        const val TOTAL_BUYER_INDEX = 5
        const val OPEN_TOKOPEDIA_SELLER_INDEX = 6
    }
}