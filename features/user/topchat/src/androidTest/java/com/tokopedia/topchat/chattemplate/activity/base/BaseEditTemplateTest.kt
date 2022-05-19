package com.tokopedia.topchat.chattemplate.activity.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment
import com.tokopedia.topchat.common.InboxMessageConstant
import com.tokopedia.topchat.stub.chattemplate.di.FakeTemplateActivityComponentFactory
import com.tokopedia.topchat.stub.chattemplate.usecase.api.ChatTemplateApiStub
import com.tokopedia.topchat.stub.chattemplate.view.activity.EditTemplateChatActivityStub
import org.junit.Before
import org.junit.Rule

abstract class BaseEditTemplateTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        EditTemplateChatActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext
    protected open lateinit var activity: EditTemplateChatActivityStub

    protected lateinit var chatTemplateApi: ChatTemplateApiStub
    private val fakeTemplateActivityComponentFactory = FakeTemplateActivityComponentFactory()

    protected val testTemplateList = arrayListOf(
        "Test template 1",
        "Test template 2",
        "Test template 3",
        "Test template 4",
        "Test template 5"
    )

    @Before
    open fun before() {
        setupDaggerComponent()
        setupResponse()
    }

    private fun setupDaggerComponent() {
        ActivityComponentFactory.instance = fakeTemplateActivityComponentFactory
    }

    private fun setupResponse() {
        chatTemplateApi = fakeTemplateActivityComponentFactory.chatTemplateApiStub
    }

    protected fun launchActivity(
        isSellerApp: Boolean = false,
        message: String? = testTemplateList.first(),
        position: Int = 0,
        sizeList: Int = 5,
        arrayListTemplate: ArrayList<String> = testTemplateList
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val intent = Intent().apply {
            this.putExtras(createBundleData(message, position, sizeList, arrayListTemplate, isSellerApp))
        }
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    private fun createBundleData(
        message: String?,
        position: Int,
        sizeList: Int,
        listTemplate: ArrayList<String>,
        isSellerApp: Boolean
    ): Bundle {
        val bundle = Bundle()
        bundle.putString(InboxMessageConstant.PARAM_MESSAGE, message)
        bundle.putInt(InboxMessageConstant.PARAM_POSITION, position)
        bundle.putInt(InboxMessageConstant.PARAM_NAV, sizeList - 1) //Minus last button
        bundle.putStringArrayList(InboxMessageConstant.PARAM_ALL, listTemplate)
        if (message == null) {
            bundle.putInt(InboxMessageConstant.PARAM_MODE, TemplateChatFragment.CREATE)
        } else {
            bundle.putInt(InboxMessageConstant.PARAM_MODE, TemplateChatFragment.EDIT)
        }
        bundle.putBoolean(TemplateChatActivity.PARAM_IS_SELLER, isSellerApp)
        return bundle
    }

    protected fun assertWithSubText(msg: String) {
        onView(withSubstring(msg)).check(matches(isDisplayed()))
    }
}