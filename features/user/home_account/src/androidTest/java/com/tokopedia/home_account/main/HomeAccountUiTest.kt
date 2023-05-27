package com.tokopedia.home_account.main

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.utils.fundsAndInvestmentRobot
import com.tokopedia.home_account.utils.homeAccountRobot
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@UiTest
class HomeAccountUiTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(HomeAccountUserActivity::class.java, false, false)

    @Before
    fun before() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.component.inject(this)
    }

    @Test
    fun fundsAndInvestmentPage() {
        activityTestRule.launchActivity(Intent())

        homeAccountRobot {
            clickLihatSemuaSaldoPoint()

            fundsAndInvestmentRobot {
                displayText("GoPay & Coins")
                displayText("Tokopedia Card")
                back()
            }
        }
    }
}
