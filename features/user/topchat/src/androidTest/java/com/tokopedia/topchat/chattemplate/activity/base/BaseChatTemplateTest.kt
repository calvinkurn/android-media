package com.tokopedia.topchat.chattemplate.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.stub.chattemplate.di.DaggerTemplateChatComponentStub
import com.tokopedia.topchat.stub.chattemplate.di.TemplateChatComponentStub
import com.tokopedia.topchat.stub.chattemplate.usecase.*
import com.tokopedia.topchat.stub.chattemplate.view.activity.TemplateChatActivityStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseChatTemplateTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        TemplateChatActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext
    protected open lateinit var activity: TemplateChatActivityStub

    @Inject
    lateinit var getTemplateUseCase: GetTemplateUseCaseStub

    @Inject
    lateinit var setAvailabilityTemplateUseCase: SetAvailabilityTemplateUseCaseStub

    protected var successGetTemplateResponseBuyer: TemplateData = TemplateData()

    @Before
    open fun before() {
        setupDaggerComponent()
        setupResponse()
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    private fun setupDaggerComponent() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        chatTemplateComponentStub = DaggerTemplateChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .build()
        chatTemplateComponentStub!!.inject(this)
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
        successGetTemplateResponseBuyer = AndroidFileUtil.parse(
            "template/success_get_template_buyer.json",
            TemplateData::class.java
        )
    }

    companion object {
        var chatTemplateComponentStub: TemplateChatComponentStub? = null
    }
}