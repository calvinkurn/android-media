package com.tokopedia.shop.score.common

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.common.util.AndroidTestUtil
import com.tokopedia.shop.score.stub.common.util.ShopPerformanceComponentStubInstance
import com.tokopedia.shop.score.stub.common.util.ShopScorePrefManagerStub
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.performance.domain.mapper.ShopScoreCommonMapperStub
import com.tokopedia.shop.score.stub.performance.domain.mapper.ShopScoreMapperStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopInfoPeriodResponseStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopScoreResponseStub
import com.tokopedia.shop.score.stub.performance.domain.usecase.GetShopCreatedInfoUseCaseStub
import com.tokopedia.shop.score.stub.performance.domain.usecase.GetShopPerformanceUseCaseStub
import com.tokopedia.shop.score.stub.performance.presentation.activity.ShopPerformanceActivityStub
import com.tokopedia.unifycomponents.R
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Context
    protected lateinit var userSessionStub: UserSessionStub
    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub
    protected lateinit var getShopInfoPeriodUseCaseStub: GetShopCreatedInfoUseCaseStub
    protected lateinit var getShopPerformanceUseCaseStub: GetShopPerformanceUseCaseStub
    protected lateinit var shopScorePrefManagerStub: ShopScorePrefManagerStub
    protected lateinit var shopScoreMapperStub: ShopScoreMapperStub
    protected lateinit var shopScoreCommonMapperStub: ShopScoreCommonMapperStub

    protected val getShopPerformanceComponentStub by lazy {
        ShopPerformanceComponentStubInstance.getShopPerformanceComponentStub(
            applicationContext
        )
    }

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val existingSellerOsResponse: ShopScoreResponseStub
        get() = AndroidTestUtil.parse(
            "raw/seller/uitest/existing/shop_score_existing_os.json",
            ShopScoreResponseStub::class.java
        )

    protected val existingSellerPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/uitest/existing/shop_score_existing_pm.json",
        ShopScoreResponseStub::class.java
    )

    protected val existingSellerPmRecomToolsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/cassava/performance/existing/shop_score_existing_pm_recom_tools.json",
        ShopScoreResponseStub::class.java
    )

    protected val existingSellerRmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/cassava/performance/existing/shop_score_existing_rm.json",
        ShopScoreResponseStub::class.java
    )

    protected val newSellerRmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/cassava/performance/new/shop_score_new_rm.json",
        ShopScoreResponseStub::class.java
    )

    protected val newOsAfterMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/uitest/new/new_os_after_monday.json",
        ShopScoreResponseStub::class.java
    )

    protected val newOsBeforeMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/uitest/new/new_os_before_monday.json",
        ShopScoreResponseStub::class.java
    )

    protected val shopInfoPeriodAfterMondayResponse =
        AndroidTestUtil.parse<ShopInfoPeriodResponseStub>(
            "raw/seller/uitest/new/shop_info_period_after_monday.json",
            ShopInfoPeriodResponseStub::class.java
        )

    protected val reactivatedAfterMondayPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/uitest/reactivated/reactivated_after_monday_pm.json",
        ShopScoreResponseStub::class.java
    )

    protected val reactivatedBeforeMondayOsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "raw/seller/uitest/reactivated/reactivated_before_monday_os.json",
        ShopScoreResponseStub::class.java
    )

    protected val shopInfoPeriodResponse: ShopInfoPeriodResponseStub
        get() = AndroidTestUtil.parse(
            "raw/seller/uitest/shopinfo/shop_info_period.json",
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
            getShopPerformanceComponentStub.getShopInfoPeriodUseCaseStub() as GetShopCreatedInfoUseCaseStub
        shopScoreCommonMapperStub =
            getShopPerformanceComponentStub.shopScoreCommonMapper() as ShopScoreCommonMapperStub
        shopScoreMapperStub =
            getShopPerformanceComponentStub.shopScoreMapper() as ShopScoreMapperStub
        shopScorePrefManagerStub =
            getShopPerformanceComponentStub.shopScorePrefManager() as ShopScorePrefManagerStub
    }

    @After
    open fun finish() {
        graphqlRepositoryStub.clearMocks()
    }

    protected fun getShopPerformancePageIntent(): Intent {
        return ShopPerformanceActivityStub.createIntent(context)
    }

    protected fun closeBottomSheet() {
        onIdView(com.tokopedia.unifycomponents.R.id.bottom_sheet_close).onClick()
    }

    protected fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}
