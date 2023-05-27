package com.tokopedia.home_account.main

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.utils.homeAccountRobot
import com.tokopedia.home_account.utils.stubAllIntent
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@CassavaTest
class HomeAccountCassavaTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(HomeAccountUserActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun before() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
        stub.component.inject(this)
    }

    @Test
    fun clickTrackerFirstPage() {
        activityTestRule.launchActivity(Intent())
        stubAllIntent()

        homeAccountRobot {
            clickProfile()
            clickWalletWithText("OVO")
            clickWalletWithText("Saldo Tokopedia")
            clickMember()
            assertClickTrackerAtFirstPage(cassavaTestRule)
        }
    }

    @Test
    fun clickTrackerPengaturanAkun() {
        activityTestRule.launchActivity(Intent())
        stubAllIntent()

        homeAccountRobot {
            scrollToPengaturanAkun()
        }
    }

    @Test
    fun clickTrackerPengaturanAplikasi() {
        activityTestRule.launchActivity(Intent())

        homeAccountRobot {
            scrollToPengaturanAplikasi()
            switchShakeShake()
            switchShakeShake()
            assertClickTracketAtPengaturanAplikasi(cassavaTestRule)
        }
    }
}
