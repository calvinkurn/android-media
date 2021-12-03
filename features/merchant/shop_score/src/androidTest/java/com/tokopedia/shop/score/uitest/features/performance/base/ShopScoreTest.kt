package com.tokopedia.shop.score.uitest.features.performance.base

import android.app.Application
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.kotlin.extensions.view.orZero
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
import com.tokopedia.shop.score.uitest.stub.performance.domain.mapper.ShopScoreCommonMapperStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.mapper.ShopScoreMapperStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopInfoPeriodResponseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopScoreResponseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopInfoPeriodUseCaseStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.usecase.GetShopPerformanceUseCaseStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import org.junit.Before
import org.junit.Rule

abstract class ShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, false, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Application
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

    protected val headerShopPerformanceUiModel by lazy {
        getHeaderPerformanceUiModel(
            existingSellerOsResponse.shopScoreLevel.result,
            existingSellerOsResponse.goldGetPMOSStatus.data,
            existingSellerOsResponse.goldGetPMShopInfoResponse,
            shopInfoPeriodResponse
        )
    }

    protected val context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val existingSellerOsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/existing/shop_score_existing_os.json",
        ShopScoreResponseStub::class.java
    )

    protected val existingSellerPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/existing/shop_score_existing_pm.json",
        ShopScoreResponseStub::class.java
    )

    protected val newOsAfterMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/new/new_os_after_monday.json",
        ShopScoreResponseStub::class.java
    )

    protected val newOsBeforeMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/new/new_os_before_monday.json",
        ShopScoreResponseStub::class.java
    )

    protected val shopInfoPeriodAfterMondayResponse =
        AndroidTestUtil.parse<ShopInfoPeriodResponseStub>(
            "seller/new/shop_info_period_after_monday.json",
            ShopInfoPeriodResponseStub::class.java
        )

    protected val reactivatedAfterMondayPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/reactivated/reactivated_after_monday_pm.json",
        ShopScoreResponseStub::class.java
    )

    protected val reactivatedAfterMondayOsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/reactivated/reactivated_before_monday_os.json",
        ShopScoreResponseStub::class.java
    )

    protected val shopInfoPeriodResponse = AndroidTestUtil.parse<ShopInfoPeriodResponseStub>(
        "seller/shopinfo/shop_info_period.json",
        ShopInfoPeriodResponseStub::class.java
    )

    @Before
    open fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()
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
        return Intent(context, ShopPerformanceActivityStub::class.java)
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
}