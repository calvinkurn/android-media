package com.tokopedia.updateinactivephone.features

import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.updateinactivephone.common.idling.FragmentTransactionIdle
import com.tokopedia.updateinactivephone.stub.di.FakeInactivePhoneDependency
import com.tokopedia.updateinactivephone.stub.di.InactivePhoneComponentStub
import com.tokopedia.updateinactivephone.stub.di.InactivePhoneComponentStubBuilder
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponent
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponentBuilder
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseInactivePhoneTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry.getInstrumentation().context.applicationContext

    val inactivePhoneDependency = FakeInactivePhoneDependency()
    var fragmentTransactionIdling: FragmentTransactionIdle? = null

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

    open fun runTest(test: () -> Unit) {
        test.invoke()
    }

    open fun runTest(source: String, test: () -> Unit) {
        test.invoke()
    }

    protected fun checkTracker(path: String, sleepTime: Long = 6000L) {
        Thread.sleep(sleepTime)
        assertThat(cassavaTestRule.validate(path), hasAllSuccess())
    }

    protected fun checkTracker(queryList: List<Map<String, Any>>, sleepTime: Long = 6000L) {
        Thread.sleep(sleepTime)
        assertThat(cassavaTestRule.validate(queryList, CassavaTestRule.MODE_SUBSET), hasAllSuccess())
    }

    protected fun createQueryMap(
        event: String,
        category: String,
        action: String,
        label: String,
    ): Map<String, String> {
        return mapOf(
            "event" to event,
            "eventCategory" to category,
            "eventAction" to action,
            "eventLabel" to label
        )
    }

    companion object {
        var baseAppComponent: FakeBaseAppComponent? = null
        var inactivePhoneComponent: InactivePhoneComponentStub? = null
    }
}