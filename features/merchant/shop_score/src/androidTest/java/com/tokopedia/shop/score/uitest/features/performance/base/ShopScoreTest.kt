package com.tokopedia.shop.score.uitest.features.performance.base

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.shop.score.uitest.stub.common.util.AndroidTestUtil
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopScoreResponseStub
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import org.junit.Before
import org.junit.Rule

abstract class ShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, false, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Application

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

    protected val shopInfoPeriodAfterMondayResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/new/shop_info_period_after_monday.json",
        ShopScoreResponseStub::class.java
    )

    protected val reactivatedAfterMondayPmResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/reactivated/reactivated_after_monday_pm.json",
        ShopScoreResponseStub::class.java
    )

    protected val reactivatedAfterMondayOsResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/reactivated/reactivated_before_monday_os.json",
        ShopScoreResponseStub::class.java
    )

    protected val shopInfoPeriodResponse = AndroidTestUtil.parse<ShopScoreResponseStub>(
        "seller/shopinfo/shop_info_period.json",
        ShopScoreResponseStub::class.java
    )

    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()

    }
}