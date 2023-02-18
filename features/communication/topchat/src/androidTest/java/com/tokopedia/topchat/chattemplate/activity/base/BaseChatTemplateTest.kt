package com.tokopedia.topchat.chattemplate.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.stub.chattemplate.di.FakeTemplateActivityComponentFactory
import com.tokopedia.topchat.stub.chattemplate.usecase.api.ChatTemplateApiStub
import com.tokopedia.topchat.stub.chattemplate.view.activity.TemplateChatActivityStub
import org.junit.Before
import org.junit.Rule

abstract class BaseChatTemplateTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        TemplateChatActivityStub::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext
    protected open lateinit var activity: TemplateChatActivityStub

    protected var successGetTemplateResponseBuyer: TemplateData = TemplateData()
    protected lateinit var chatTemplateApi: ChatTemplateApiStub
    private val fakeTemplateActivityComponentFactory = FakeTemplateActivityComponentFactory()

    @Before
    open fun before() {
        setupDaggerComponent()
        setupResponse()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    private fun setupDaggerComponent() {
        ActivityComponentFactory.instance = fakeTemplateActivityComponentFactory
    }

    protected fun launchActivity(
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val intent = Intent().apply {
            putExtra(TemplateChatActivity.PARAM_IS_SELLER, isSellerApp)
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    private fun setupResponse() {
        chatTemplateApi = fakeTemplateActivityComponentFactory.chatTemplateApiStub
        successGetTemplateResponseBuyer = AndroidFileUtil.parse(
            "template/success_get_template_buyer.json",
            TemplateData::class.java
        )
    }

    protected fun assertSnackBarWithSubText(msg: String) {
        onView(withSubstring(msg)).check(matches(isDisplayed()))
    }
}
