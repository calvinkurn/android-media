package com.tokopedia.shop.score.uitest.features.performance.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.BaseShopScoreTest
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMOStatusResponse
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMShopInfoResponse
import com.tokopedia.shop.score.performance.domain.model.ShopScoreLevelResponse
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemTimerNewSellerUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.stub.common.util.getShopPerformanceFragment
import com.tokopedia.shop.score.stub.common.util.getTextHtml
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.isViewNotDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onContentDescPopup
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.shop.score.stub.common.util.smoothScrollTo
import com.tokopedia.shop.score.stub.common.util.smoothScrollToFaq
import com.tokopedia.shop.score.stub.common.util.withTextStr
import com.tokopedia.shop.score.stub.performance.domain.response.ShopInfoPeriodResponseStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopScoreResponseStub
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.unifycomponents.ProgressBarUnify
import org.hamcrest.CoreMatchers.`is`

abstract class ShopScoreUiTest: BaseShopScoreTest() {

    private fun getShopInfoPeriod(
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ): ShopInfoPeriodUiModel {
        return shopScoreCommonMapperStub.mapToGetShopInfo(
            ShopInfoPeriodWrapperResponse(
                shopInfoByIDResponse = shopInfoPeriodResponseStub.shopInfoByIDResponse
            )
        )
    }

    private fun getHeaderPerformanceUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        powerMerchantResponse: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data?,
        goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ): HeaderShopPerformanceUiModel {
        return shopScoreMapperStub.mapToHeaderShopPerformance(
            shopScoreLevelResponse = shopScoreLevelResponse,
            powerMerchantResponse = powerMerchantResponse,
            shopAge = goldGetPMShopInfoResponse?.shopAge.orZero(),
            dateShopCreated = getShopInfoPeriod(shopInfoPeriodResponseStub).dateShopCreated
        )
    }

    protected fun showHeaderPerformanceNewSellerOs(
        shopScoreResponseStub: ShopScoreResponseStub,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ) {
        val headerShopPerformanceUiModel = getHeaderPerformanceUiModel(
            shopScoreResponseStub.shopScoreLevel.result,
            shopScoreResponseStub.goldGetPMOSStatus.data,
            shopScoreResponseStub.goldGetPMShopInfoResponse,
            shopInfoPeriodResponseStub
        )
        onView(withId(R.id.tvPerformanceLevel)).isViewDisplayed().withTextStr(
            context.getString(
                R.string.shop_performance_level_header,
                headerShopPerformanceUiModel.shopLevel
            )
        )

        onIdView(R.id.tvShopScoreValue).isViewDisplayed()
            .withTextStr("-")

        onIdView(R.id.tvHeaderShopServiceNewSeller).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.titleHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.tvDescShopServiceNewSeller).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.descHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.ivLevelBarShopScore).isViewDisplayed()

        onIdView(R.id.progressBarNewSeller).isViewDisplayed()

        onIdView(R.id.ic_shop_score_performance).isViewNotDisplayed()
    }

    protected fun showHeaderPerformanceExistingPm(
        shopScoreResponseStub: ShopScoreResponseStub,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ) {
        val headerShopPerformanceUiModel = getHeaderPerformanceUiModel(
            shopScoreResponseStub.shopScoreLevel.result,
            shopScoreResponseStub.goldGetPMOSStatus.data,
            shopScoreResponseStub.goldGetPMShopInfoResponse,
            shopInfoPeriodResponseStub
        )

        onView(withId(R.id.tvPerformanceLevel)).isViewDisplayed().withTextStr(
            context.getString(
                R.string.shop_performance_level_header,
                headerShopPerformanceUiModel.shopLevel
            )
        )

        onIdView(R.id.tvShopScoreValue).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.shopScore)

        onIdView(R.id.tvHeaderShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.titleHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.tvDescShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.descHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.ivLevelBarShopScore).isViewDisplayed()

        onIdView(R.id.progressBarScorePerformance).isViewDisplayed()
        val progressBar =
            activityRule.activity.findViewById<ProgressBarUnify>(R.id.progressBarScorePerformance)
        assertThat(progressBar.getValue(), `is`(headerShopPerformanceUiModel.shopScore.toInt()))

        onIdView(R.id.ic_shop_score_performance).isViewDisplayed()
    }

    protected fun showHeaderPerformanceNewOsAfterMonday(
        shopScoreResponseStub: ShopScoreResponseStub,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ) {
        val headerShopPerformanceUiModel = getHeaderPerformanceUiModel(
            shopScoreResponseStub.shopScoreLevel.result,
            shopScoreResponseStub.goldGetPMOSStatus.data,
            shopScoreResponseStub.goldGetPMShopInfoResponse,
            shopInfoPeriodResponseStub
        )
        onView(withId(R.id.tvPerformanceLevel)).isViewDisplayed().withTextStr(
            context.getString(
                R.string.shop_performance_level_header,
                headerShopPerformanceUiModel.shopLevel
            )
        )

        onIdView(R.id.tvShopScoreValue).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.shopScore)

        onIdView(R.id.tvHeaderShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.titleHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.tvDescShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.descHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.ivLevelBarShopScore).isViewDisplayed()

        onIdView(R.id.progressBarScorePerformance).isViewDisplayed()
        val progressBar =
            activityRule.activity.findViewById<ProgressBarUnify>(R.id.progressBarScorePerformance)
        assertThat(progressBar.getValue(), `is`(headerShopPerformanceUiModel.shopScore.toInt()))

        onIdView(R.id.ic_shop_score_performance).isViewDisplayed()
    }

    protected fun showHeaderPerformanceExistingOs(
        shopScoreResponseStub: ShopScoreResponseStub,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ) {
        val headerShopPerformanceUiModel = getHeaderPerformanceUiModel(
            shopScoreResponseStub.shopScoreLevel.result,
            shopScoreResponseStub.goldGetPMOSStatus.data,
            shopScoreResponseStub.goldGetPMShopInfoResponse,
            shopInfoPeriodResponseStub
        )
        onView(withId(R.id.tvPerformanceLevel)).isViewDisplayed().withTextStr(
            context.getString(
                R.string.shop_performance_level_header,
                headerShopPerformanceUiModel.shopLevel
            )
        )

        onIdView(R.id.tvShopScoreValue).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.shopScore)

        onIdView(R.id.tvHeaderShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.titleHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.tvDescShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.descHeaderShopService?.let {
                context.getString(
                    it
                )
            })

        onIdView(R.id.ivLevelBarShopScore).isViewDisplayed()

        onIdView(R.id.progressBarScorePerformance).isViewDisplayed()
        val progressBar =
            activityRule.activity.findViewById<ProgressBarUnify>(R.id.progressBarScorePerformance)
        assertThat(progressBar.getValue(), `is`(headerShopPerformanceUiModel.shopScore.toInt()))

        onIdView(R.id.ic_shop_score_performance).isViewDisplayed()

        onIdView(R.id.tickerShopHasPenalty).isViewDisplayed()
    }

    protected fun showSectionPeriodDetailExisting(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopScoreLevel = shopScoreResponseStub.shopScoreLevel.result
        onIdView(R.id.tvPerformanceDetailLabel).isViewDisplayed()
            .withTextStr(context.getString(R.string.title_detail_performa, shopScoreLevel.period))
        onIdView(R.id.tvPerformanceDetailDate).isViewDisplayed()
            .withTextStr(context.getString(R.string.title_update_date, shopScoreLevel.nextUpdate))
    }

    protected fun showSectionPeriodDetailNew(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopScoreLevel = shopScoreResponseStub.shopScoreLevel.result
        onIdView(R.id.tvPerformanceDetailLabel).isViewDisplayed()
            .withTextStr(
                context.getString(
                    R.string.title_detail_performance_new_seller,
                    shopScoreLevel.period
                )
            )
        onIdView(R.id.tvPerformanceDetailDate).isViewDisplayed()
            .withTextStr(
                context.getString(
                    R.string.title_update_date_new_seller,
                    shopScoreLevel.period
                )
            )
        onIdView(R.id.tvPerformanceDetailDateNewSeller).isViewDisplayed()
            .withTextStr(context.getString(R.string.title_update_date, shopScoreLevel.nextUpdate))
    }

    protected fun showDetailPerformanceNewOsBeforeMonday() {
        activityRule.activity.run {
            val recyclerViewMatcher = RecyclerViewMatcher(R.id.rvShopPerformance)
            val itemDetailPosition =
                getShopPerformanceFragment().shopPerformanceAdapter.list.indexOfFirst {
                    it is ItemDetailPerformanceUiModel
                }

            val itemDetailList =
                getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<ItemDetailPerformanceUiModel>()
            itemDetailList.forEachIndexed { index, itemDetail ->
                val actualPositionItemDetail = itemDetailPosition + index
                smoothScrollTo(actualPositionItemDetail)
                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvTitlePerformanceProgress
                    )
                ).withTextStr(itemDetail.titleDetailPerformance)

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvPerformanceTarget
                    )
                ).withTextStr(
                    context.getString(
                        R.string.item_detail_performance_target,
                        itemDetail.targetDetailPerformance
                    )
                )

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.cardItemDetailShopPerformance
                    )
                ).onClick()
            }
        }
    }

    protected fun showDetailPerformanceReactivatedBeforeMonday() {
        activityRule.activity.run {
            val recyclerViewMatcher = RecyclerViewMatcher(R.id.rvShopPerformance)
            val itemDetailPosition =
                getShopPerformanceFragment().shopPerformanceAdapter.list.indexOfFirst {
                    it is ItemDetailPerformanceUiModel
                }

            val itemDetailList =
                getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<ItemDetailPerformanceUiModel>()
            itemDetailList.forEachIndexed { index, itemDetail ->
                val actualPositionItemDetail = itemDetailPosition + index
                smoothScrollTo(actualPositionItemDetail)
                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvTitlePerformanceProgress
                    )
                ).withTextStr(itemDetail.titleDetailPerformance)

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvPerformanceTarget
                    )
                ).withTextStr("")

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.cardItemDetailShopPerformance
                    )
                ).onClick()
            }
        }
    }

    protected fun showDetailPerformanceExisting() {
        activityRule.activity.run {
            val recyclerViewMatcher = RecyclerViewMatcher(R.id.rvShopPerformance)
            val itemDetailPosition =
                getShopPerformanceFragment().shopPerformanceAdapter.list.indexOfFirst {
                    it is ItemDetailPerformanceUiModel
                }

            val itemDetailList =
                getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<ItemDetailPerformanceUiModel>()
            itemDetailList.forEachIndexed { index, itemDetail ->
                val actualPositionItemDetail = itemDetailPosition + index
                smoothScrollTo(actualPositionItemDetail)
                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvTitlePerformanceProgress
                    )
                ).withTextStr(itemDetail.titleDetailPerformance)

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.tvPerformanceTarget
                    )
                ).withTextStr(
                    context.getString(
                        R.string.item_detail_performance_target,
                        itemDetail.targetDetailPerformance
                    )
                )

                onView(
                    recyclerViewMatcher.atPositionOnView(
                        actualPositionItemDetail,
                        R.id.cardItemDetailShopPerformance
                    )
                ).onClick()
                showDetailPerformanceBottomSheet(itemDetail.identifierDetailPerformance)
            }
        }
    }

    protected fun showFaqItemList() {
        val recyclerViewMatcher = RecyclerViewMatcher(R.id.rv_faq_shop_score)

        val itemFaqList =
            activityRule.activity.getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<SectionFaqUiModel>()
                .first().itemFaqUiModelList

        itemFaqList.forEachIndexed { index, itemFaqUiModel ->
            smoothScrollToFaq(index)

            onView(
                recyclerViewMatcher.atPositionOnView(
                    index,
                    R.id.tv_title_faq_shop_score
                )
            ).withTextStr(itemFaqUiModel.title?.let { context.getString(it) })

            onView(
                recyclerViewMatcher.atPositionOnView(
                    index,
                    R.id.ic_info_toggle_faq
                )
            ).onClick()
        }
    }

    protected fun showTimerNewSellerAfterMonday() {
        activityRule.activity.run {
            val timerNewSeller =
                getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<ItemTimerNewSellerUiModel>()
                    .first()

            onIdView(R.id.containerTimerNewSeller).isViewDisplayed()
            onIdView(R.id.timerNewSeller).isViewDisplayed()
            onIdView(R.id.tv_shop_performance_new_seller).isViewDisplayed().withTextStr(
                context.getString(
                    R.string.desc_shop_performance_timer_after_first_monday,
                    timerNewSeller.effectiveDateText
                )
            )
        }
    }

    protected fun showTimerNewSeller() {
        activityRule.activity.run {
            val timerNewSeller =
                getShopPerformanceFragment().shopPerformanceAdapter.list.filterIsInstance<ItemTimerNewSellerUiModel>()
                    .first()

            onIdView(R.id.containerTimerNewSeller).isViewDisplayed()
            onIdView(R.id.timerNewSeller).isViewDisplayed()
            onIdView(R.id.tv_shop_performance_new_seller).isViewDisplayed().withTextStr(
                context.getString(
                    R.string.title_shop_performance_become_existing_seller,
                    timerNewSeller.effectiveDateText
                )
            )
        }
    }

    private fun showDetailPerformanceBottomSheet(identifierDetailPerformance: String) {
        val shopPerformanceDetail =
            shopScoreMapperStub.mapToShopPerformanceDetail(identifierDetailPerformance)
        onIdView(R.id.tvDescCalculationDetail).isViewDisplayed().withTextStr(
            MethodChecker.fromHtml(shopPerformanceDetail.descCalculation?.let { context.getString(it) })
                .toString()
        )
        closeBottomSheet()
    }

    protected fun showCoachMarkShopScore() {
        Thread.sleep(2000)
        val coachMark2 = CoachMark2(activityRule.activity)
        onContentDescPopup(context.getString(R.string.title_coachmark_shop_score_1)).isViewDisplayed()
        onContentDescPopup(context.getString(R.string.desc_coachmark_shop_score_1)).isViewDisplayed()
        onContentDescPopup(coachMark2.stepButtonText).onClick()
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        onContentDescPopup(context.getString(R.string.title_coachmark_shop_score_2)).isViewDisplayed()
        onContentDescPopup(context.getString(R.string.desc_coachmark_shop_score_2)).isViewDisplayed()
        onContentDescPopup(coachMark2.stepButtonTextLastChild).onClick()
    }

    protected fun showBottomSheetTooltipScoreExisting() {
        onIdView(R.id.tvTitleCalculation).isViewDisplayed()
            .withTextStr(context.getString(R.string.title_calculation_shop_performance))
        onIdView(R.id.tvDescCalculation).isViewDisplayed()
            .withTextStr(context.getString(R.string.desc_calculation_shop_performance))
        onIdView(R.id.tvUpdateCalculation).isViewDisplayed()
            .withTextStr(context.getString(R.string.update_calculation_shop_performance))
        closeBottomSheet()
    }

    protected fun showBottomSheetTooltipLevelOs(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopLevelTooltip = shopScoreResponseStub.shopLevelTooltipResponse.result
        onIdView(R.id.tv_period_information_level)
            .isViewDisplayed()
            .withTextStr(shopLevelTooltip.period)
        onIdView(R.id.tv_value_income_tooltip).isViewDisplayed()
            .withTextStr(
                StringBuilder(
                    "Rp${shopLevelTooltip.niv?.toDouble()?.getNumberFormatted()}"
                ).toString()
            )
        onIdView(R.id.tv_value_product_sold_tooltip).isViewDisplayed()
            .withTextStr(shopLevelTooltip.itemSold.toString())
        onIdView(R.id.tv_value_next_update).isViewDisplayed()
            .withTextStr(
                context.getString(
                    R.string.title_update_date,
                    shopLevelTooltip.nextUpdate
                )
            )
        closeBottomSheet()
    }

    protected fun showBottomSheetTooltipLevelPm(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopLevelTooltip = shopScoreResponseStub.shopLevelTooltipResponse.result
        onIdView(R.id.tv_period_information_level)
            .isViewDisplayed()
            .withTextStr(shopLevelTooltip.period)
        onIdView(R.id.tv_value_income_tooltip).isViewDisplayed().withTextStr("-")
        onIdView(R.id.tv_value_product_sold_tooltip).isViewDisplayed().withTextStr("-")
        onIdView(R.id.tv_value_next_update).isViewDisplayed()
            .withTextStr(
                context.getString(
                    R.string.title_update_date,
                    shopLevelTooltip.nextUpdate
                )
            )
        closeBottomSheet()
    }

    protected fun showBottomSheetTooltipLevelNew(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopLevelTooltip = shopScoreResponseStub.shopLevelTooltipResponse.result
        onIdView(R.id.tv_period_information_level)
            .isViewDisplayed()
            .withTextStr(shopLevelTooltip.period)
        onIdView(R.id.tv_value_income_tooltip).isViewDisplayed().withTextStr("-")
        onIdView(R.id.tv_value_product_sold_tooltip).isViewDisplayed().withTextStr("-")
        onIdView(R.id.tv_value_next_update).isViewDisplayed()
            .withTextStr(
                context.getString(
                    R.string.title_update_date,
                    shopLevelTooltip.nextUpdate
                )
            )
        closeBottomSheet()
    }

    protected fun showPowerMerchantSection() {
        onIdView(R.id.tv_pm_reputation_value)
            .isViewDisplayed()
            .withTextStr(context.getString(R.string.title_pm_value))
        onIdView(R.id.tv_desc_content_pm_section).isViewDisplayed()
            .withTextStr(
                getTextHtml(
                    context,
                    context.getString(R.string.desc_content_pm_not_eligible_pm_pro)
                ).toString()
            )
        onIdView(R.id.potentialPowerMerchantWidget).isViewDisplayed().onClick()
    }

    protected fun showProtectedParameterSection(shopScoreResponseStub: ShopScoreResponseStub) {
        val scoreProtectedParameter = shopScoreMapperStub.getProtectedParameterSection(
            shopScoreResponseStub.shopScoreLevel.result.shopScoreDetail,
            shopScoreResponseStub.goldGetPMShopInfoResponse.shopAge.toInt()
        )
        val recyclerViewMatcher = RecyclerViewMatcher(R.id.rvParameterReliefDetail)

        scoreProtectedParameter.itemProtectedParameterList.forEachIndexed { index, protectedItem ->
            onView(
                recyclerViewMatcher.atPositionOnView(
                    index,
                    R.id.tvTitleParameterReliefDetail
                )
            ).withTextStr(protectedItem.parameterTitle)
        }
        onIdView(R.id.tvTitleParameterRelief).isViewDisplayed().withTextStr(
            scoreProtectedParameter.titleParameterRelief?.let { context.getString(it) }
        )
        onIdView(R.id.tvDescParameterRelief).isViewDisplayed().withTextStr(
            scoreProtectedParameter.descParameterRelief?.let { context.getString(it) }
        )

        onIdView(R.id.cardDescParameterRelief).isViewDisplayed().onClick()

        onIdView(R.id.tvDescProtectedParameter).isViewDisplayed().withTextStr(
            MethodChecker.fromHtml(
                scoreProtectedParameter.descParameterReliefBottomSheet?.let {
                    context.getString(
                        it,
                        scoreProtectedParameter.protectedParameterDaysDate
                    )
                }).toString()
        )

        closeBottomSheet()
    }

}