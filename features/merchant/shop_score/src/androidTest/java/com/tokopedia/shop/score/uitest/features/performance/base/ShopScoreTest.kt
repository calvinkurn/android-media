package com.tokopedia.shop.score.uitest.features.performance.base

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMOStatusResponse
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMShopInfoResponse
import com.tokopedia.shop.score.performance.domain.model.ShopScoreLevelResponse
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemFaqUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.uitest.stub.common.UserSessionStub
import com.tokopedia.shop.score.uitest.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.uitest.stub.common.util.AndroidTestUtil
import com.tokopedia.shop.score.uitest.stub.common.util.ShopPerformanceComponentStubInstance
import com.tokopedia.shop.score.uitest.stub.common.util.ShopScorePrefManagerStub
import com.tokopedia.shop.score.uitest.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.uitest.stub.common.util.onClick
import com.tokopedia.shop.score.uitest.stub.common.util.onContentDescPopup
import com.tokopedia.shop.score.uitest.stub.common.util.onIdView
import com.tokopedia.shop.score.uitest.stub.common.util.onWithText
import com.tokopedia.shop.score.uitest.stub.common.util.scrollToWithPos
import com.tokopedia.shop.score.uitest.stub.common.util.withTextStr
import com.tokopedia.shop.score.uitest.stub.performance.domain.mapper.ShopScoreCommonMapperStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.mapper.ShopScoreMapperStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopInfoPeriodResponseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopScoreResponseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopInfoPeriodUseCaseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopPerformanceUseCaseStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import com.tokopedia.unifycomponents.ProgressBarUnify
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
abstract class ShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Context
    protected lateinit var userSessionStub: UserSessionStub
    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub
    protected lateinit var getShopInfoPeriodUseCaseStub: GetShopInfoPeriodUseCaseStub
    protected lateinit var getShopPerformanceUseCaseStub: GetShopPerformanceUseCaseStub
    protected lateinit var shopScorePrefManagerStub: ShopScorePrefManagerStub
    protected lateinit var shopScoreMapperStub: ShopScoreMapperStub
    protected lateinit var shopScoreCommonMapperStub: ShopScoreCommonMapperStub

    protected val getShopPerformanceComponentStub by lazy {
        ShopPerformanceComponentStubInstance.getShopPerformanceComponentStub(
            applicationContext
        )
    }

    protected val context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val existingSellerOsResponse: ShopScoreResponseStub
        get() = AndroidTestUtil.parse(
            "raw/seller/existing/shop_score_existing_os.json",
            ShopScoreResponseStub::class.java
        )

//    protected val existingSellerPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
//        "raw/seller/existing/shop_score_existing_pm.json",
//        ShopScoreResponseStub::class.java
//    )
//
//    protected val newOsAfterMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
//        "raw/seller/new/new_os_after_monday.json",
//        ShopScoreResponseStub::class.java
//    )
//
//    protected val newOsBeforeMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
//        "raw/seller/new/new_os_before_monday.json",
//        ShopScoreResponseStub::class.java
//    )
//
//    protected val shopInfoPeriodAfterMondayResponse =
//        AndroidTestUtil.parse<ShopInfoPeriodResponseStub>(
//            "raw/seller/new/shop_info_period_after_monday.json",
//            ShopInfoPeriodResponseStub::class.java
//        )
//
//    protected val reactivatedAfterMondayPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
//        "raw/seller/reactivated/reactivated_after_monday_pm.json",
//        ShopScoreResponseStub::class.java
//    )
//
//    protected val reactivatedAfterMondayOsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
//        "raw/seller/reactivated/reactivated_before_monday_os.json",
//        ShopScoreResponseStub::class.java
//    )

    protected val shopInfoPeriodResponse: ShopInfoPeriodResponseStub
        get() = AndroidTestUtil.parse(
            "raw/seller/shopinfo/shop_info_period.json",
            ShopInfoPeriodResponseStub::class.java
        )

    @Before
    open fun setup() {
        applicationContext = InstrumentationRegistry.getInstrumentation().context.applicationContext
        graphqlRepositoryStub =
            getShopPerformanceComponentStub.graphQlRepository() as GraphqlRepositoryStub
        userSessionStub = getShopPerformanceComponentStub.userSessionInterface() as UserSessionStub
        getShopPerformanceUseCaseStub =
            getShopPerformanceComponentStub.getShopPerformanceUseCaseStub() as GetShopPerformanceUseCaseStub
        getShopInfoPeriodUseCaseStub =
            getShopPerformanceComponentStub.getShopInfoPeriodUseCaseStub() as GetShopInfoPeriodUseCaseStub
        shopScoreCommonMapperStub =
            getShopPerformanceComponentStub.shopScoreCommonMapper() as ShopScoreCommonMapperStub
        shopScoreMapperStub =
            getShopPerformanceComponentStub.shopScoreMapper() as ShopScoreMapperStub
        shopScorePrefManagerStub =
            getShopPerformanceComponentStub.shopScorePrefManager() as ShopScorePrefManagerStub
    }

    protected fun getShopPerformancePageIntent(): Intent {
        return ShopPerformanceActivityStub.createIntent(context)
    }

    protected fun getShopInfoPeriod(
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ): ShopInfoPeriodUiModel {
        return shopScoreCommonMapperStub.mapToGetShopInfo(
            ShopInfoPeriodWrapperResponse(
                shopInfoByIDResponse = shopInfoPeriodResponseStub.shopInfoByIDResponse,
                goldGetPMSettingInfo = shopInfoPeriodResponseStub.goldGetPMSettingInfo
            )
        )
    }

    protected fun getHeaderPerformanceUiModel(
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

    protected fun getDetailPerformanceList(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ): List<ItemDetailPerformanceUiModel> {
        return shopScoreMapperStub.mapToItemDetailPerformanceUiModel(
            shopScoreLevelList = shopScoreLevelResponse?.shopScoreDetail,
            shopScore = shopScoreLevelResponse?.shopScore.orZero(),
            shopAge = goldGetPMShopInfoResponse?.shopAge.orZero(),
            dateShopCreated = shopInfoPeriodResponseStub.shopInfoByIDResponse?.result?.firstOrNull()?.createInfo?.shopCreated.orEmpty()
        )
    }

    protected fun getFaqList(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo?,
        pmData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data.PowerMerchant
    ): List<ItemFaqUiModel> {
        return shopScoreMapperStub.mapToItemFaqUiModel(
            isNewSeller = false,
            isOfficialStore = false,
            shopScoreDetail = shopScoreLevelResponse?.shopScoreDetail,
            shopScore = shopScoreLevelResponse?.shopScore.orZero(),
            shopAge = goldGetPMShopInfoResponse?.shopAge.orZero(),
            pmData = pmData
        )
    }

    protected fun getSectionPeriodDetailPerformanceUiModel(
        shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel.Result?,
        isNewSeller: Boolean
    ): PeriodDetailPerformanceUiModel {
        return shopScoreMapperStub.mapToSectionPeriodDetailPerformanceUiModel(
            shopScoreLevelResponse, isNewSeller
        )
    }

    protected fun showHeaderPerformanceExisting(
        shopScoreResponseStub: ShopScoreResponseStub,
        shopInfoPeriodResponseStub: ShopInfoPeriodResponseStub
    ) {
        val headerShopPerformanceUiModel = getHeaderPerformanceUiModel(
            shopScoreResponseStub.shopScoreLevel.result,
            shopScoreResponseStub.goldGetPMOSStatus.data,
            shopScoreResponseStub.goldGetPMShopInfoResponse,
            shopInfoPeriodResponseStub
        )
        onView(withId(R.id.tvPerformanceLevel)).check(matches(isDisplayed())).check(
            matches(
                withText(
                    context.getString(
                        R.string.shop_performance_level_header,
                        headerShopPerformanceUiModel.shopLevel
                    )
                )
            )
        )

        onIdView(R.id.tvShopScoreValue).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.shopScore)

        onIdView(R.id.tvHeaderShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.titleHeaderShopService)

        onIdView(R.id.tvDescShopService).isViewDisplayed()
            .withTextStr(headerShopPerformanceUiModel.descHeaderShopService)

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

    protected fun showDetailPerformanceExisting(shopScoreResponseStub: ShopScoreResponseStub) {
        val shopScoreDetail = shopScoreResponseStub.shopScoreLevel.result.shopScoreDetail

        activityRule.activity.run {
            shopScoreDetail.forEachIndexed { index, shopScoreDetail ->
                scrollToWithPos<ItemDetailPerformanceUiModel>(index)

            }
        }
    }

    protected fun showCoachMarkShopScoreOs() {
        val coachMark2 = CoachMark2(activityRule.activity)
        onContentDescPopup(context.getString(R.string.title_coachmark_shop_score_1)).isViewDisplayed()
        onContentDescPopup(context.getString(R.string.desc_coachmark_shop_score_1)).isViewDisplayed()
        onContentDescPopup(coachMark2.stepButtonText).onClick()
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
        onIdView(com.tokopedia.unifycomponents.R.id.bottom_sheet_close).onClick()
    }

    protected fun showBottomSheetTooltipLevel(shopScoreResponseStub: ShopScoreResponseStub) {
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
            .withTextStr(context.getString(R.string.title_update_date, shopLevelTooltip.nextUpdate))
        onIdView(com.tokopedia.unifycomponents.R.id.bottom_sheet_close).onClick()
    }
}