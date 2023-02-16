package com.tokopedia.profilecompletion.addphone

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.profilecompletion.addphone.view.activity.NewAddPhoneActivity
import com.tokopedia.profilecompletion.common.ViewIdGenerator
import com.tokopedia.profilecompletion.common.stub.di.createProfileCompletionComponent
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class NewAddPhoneGenerateIdTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        NewAddPhoneActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    fun before() {
        val fakeBaseComponent = createProfileCompletionComponent(applicationContext.applicationContext)

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
    }

    @After
    fun after() {
        activityTestRule.finishActivity()
    }

    @Test
    fun init_register_then_init_view_displayed() {
        activityTestRule.launchActivity(Intent())

        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "profilecompletion_new_add_phone.csv")
    }

}
