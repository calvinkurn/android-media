package com.tokopedia.updateinactivephone.features

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.updateinactivephone.common.idling.FragmentTransactionIdle
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

    val inactivePhoneDependency = FakeInactivePhoneDependency()
    var fragmentTransactionIdling: FragmentTransactionIdle? = null

    @Before
    open fun before() {
        baseAppComponent = FakeBaseAppComponentBuilder.getComponent(applicationContext)
        inactivePhoneComponent = InactivePhoneComponentStubBuilder.getComponent(applicationContext)
        inactivePhoneComponent?.injectMember(inactivePhoneDependency)

        inactivePhoneDependency.setDefaultResponse()
        IdlingRegistry.getInstance().register(CountingIdlingResource(RESOURCE))
    }

    @After
    open fun tearDown() {
        IdlingRegistry.getInstance().unregister(CountingIdlingResource(RESOURCE))
        baseAppComponent = null
        inactivePhoneComponent = null
    }

    open fun runTest(test: () -> Unit) {
        test.invoke()
    }

    open fun runTest(source: String, test: () -> Unit) {
        test.invoke()
    }

    companion object {
        private const val RESOURCE = "global"
        private const val THANOS_JOURNEY_LIST_ID = "58"
        var baseAppComponent: FakeBaseAppComponent? = null
        var inactivePhoneComponent: InactivePhoneComponentStub? = null
    }
}