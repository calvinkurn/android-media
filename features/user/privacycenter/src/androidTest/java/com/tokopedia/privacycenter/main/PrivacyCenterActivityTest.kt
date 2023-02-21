package com.tokopedia.privacycenter.main

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.main.di.DaggerTestAppComponent
import com.tokopedia.privacycenter.main.di.FakeActivityComponentFactory
import com.tokopedia.privacycenter.main.di.FakeAppModule
import com.tokopedia.privacycenter.ui.main.PrivacyCenterActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class PrivacyCenterActivityTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(PrivacyCenterActivity::class.java, false, false)

    private lateinit var ctx: Context

    @Before
    fun setUp() {
        ctx = ApplicationProvider.getApplicationContext()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val component = DaggerTestAppComponent.builder().appModule(FakeAppModule(ctx)).build()
//        fakeGql = component.fakeGraphql() as FakeGraphqlUseCase
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        ActivityComponentFactory.instance = FakeActivityComponentFactory()
    }

    @After
    fun tear() {
        InstrumentationAuthHelper.clearUserSession()
    }

    @Test
    fun basic_test() {
        activityTestRule.launchActivity(Intent())
        Thread.sleep(500)

        privacyCenterRobot { } assert {
            shouldShowCorrectName("Hai, Erick Samuel (test)")
            shouldShowIconLinked()
            shouldShowRecommendationSection()
        }

        privacyCenterRobot {
            scrollToBottom()
            clickRiwayatKebijakan()
        } assert {
            shouldDisplayPrivacyTestData()
        }
    }
}
