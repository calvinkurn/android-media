package com.tokopedia.updateinactivephone.features

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.updateinactivephone.stub.di.FakeInactivePhoneDependency
import com.tokopedia.updateinactivephone.stub.di.InactivePhoneComponentStub
import com.tokopedia.updateinactivephone.stub.di.InactivePhoneComponentStubBuilder
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponent
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponentBuilder
import org.junit.After
import org.junit.Before

abstract class BaseInactivePhoneTest {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry.getInstrumentation().context.applicationContext

    var baseAppComponent: FakeBaseAppComponent? = null
    var inactivePhoneComponent: InactivePhoneComponentStub? = null

    val inactivePhoneDependency = FakeInactivePhoneDependency()

    @Before
    open fun before() {
        baseAppComponent = FakeBaseAppComponentBuilder.getComponent(applicationContext)
        inactivePhoneComponent = InactivePhoneComponentStubBuilder.getComponent(applicationContext, context)
        inactivePhoneComponent?.injectMember(inactivePhoneDependency)

        inactivePhoneDependency.setDefaultResponse()
    }

    @After
    open fun tearDown() {
        baseAppComponent = null
        inactivePhoneComponent = null
    }
}