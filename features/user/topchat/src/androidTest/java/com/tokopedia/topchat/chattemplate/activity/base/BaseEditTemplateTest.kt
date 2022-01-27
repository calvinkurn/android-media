package com.tokopedia.topchat.chattemplate.activity.base

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.topchat.stub.chattemplate.di.DaggerTemplateChatComponentStub
import com.tokopedia.topchat.stub.chattemplate.di.TemplateChatComponentStub
import com.tokopedia.topchat.stub.chattemplate.usecase.*
import com.tokopedia.topchat.stub.chattemplate.view.activity.EditTemplateChatActivityStub
import com.tokopedia.topchat.stub.chattemplate.view.activity.TemplateChatActivityStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseEditTemplateTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        EditTemplateChatActivityStub::class.java, false, false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Inject
    lateinit var createTemplateUseCase: CreateTemplateUseCaseStub

    @Inject
    lateinit var editTemplateUseCase: EditTemplateUseCaseStub

    @Inject
    lateinit var deleteTemplateUseCase: DeleteTemplateUseCaseStub

    @Before
    open fun before() {
        setupDaggerComponent()
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

    companion object {
        var chatTemplateComponentStub: TemplateChatComponentStub? = null
    }
}