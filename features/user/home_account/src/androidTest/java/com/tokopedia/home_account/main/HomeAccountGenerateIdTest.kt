package com.tokopedia.home_account.main

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.home_account.common.ViewIdGenerator
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class HomeAccountGenerateIdTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(HomeAccountUserActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun before() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
    }

    @Test
    fun generate_view_id_file() {
        activityTestRule.launchActivity(Intent())

        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "home_account.csv")
    }
}
