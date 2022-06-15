package com.tokopedia.loginregister.login.behaviour.base

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.di.DaggerTestAppComponent
import com.tokopedia.loginregister.login.behaviour.di.modules.AppModuleStub
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class LoginTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        LoginActivity::class.java, false, false
    )

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponent.builder().appModuleStub(AppModuleStub(ctx)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)

    }

    @Test
    fun test() {
        val intent = Intent()
        activityTestRule.launchActivity(intent)
    }
}