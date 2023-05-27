package com.tokopedia.home_account.main

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import com.tokopedia.applink.user.DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER
import com.tokopedia.home_account.R
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.utils.fundsAndInvestmentRobot
import com.tokopedia.home_account.utils.homeAccountRobot
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.every
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@LargeTest
@UiTest
class HomeAccountUiTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(HomeAccountUserActivity::class.java, false, false)

    @Inject
    lateinit var abTest: AbTestPlatform

    @Before
    fun before() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.component.inject(this)
    }

    @Test
    fun generalFlowTest() {
        activityTestRule.launchActivity(Intent())

        homeAccountRobot {
            clickLihatSemuaSaldoPoint()

            fundsAndInvestmentRobot {
                displayText("GoPay & Coins")
                displayText("Tokopedia Card")
                back()
            }

            scrollToPengaturanAkun()
            clickSectionWithText(R.string.title_privacy_account)

            Thread.sleep(5000)
        }
    }

    @Test
    fun privacyCenter_toggledOn() {
        every { abTest.getString(ROLLENCE_PRIVACY_CENTER) } returns "true"
        activityTestRule.launchActivity(Intent())

        homeAccountRobot {
            scrollToPengaturanAplikasi()
            Thread.sleep(10000)
        }
    }
}
