package com.tokopedia.updateinactivephone.features.successpage

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.common.idling.FragmentTransactionIdle
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.stub.features.successpage.InactivePhoneSuccessPageActivityStub
import org.junit.Rule

abstract class BaseSuccessPageTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityAccountListRule = IntentsTestRule(
        InactivePhoneSuccessPageActivityStub::class.java, false, false
    )

    protected var activity: InactivePhoneSuccessPageActivityStub? = null
    protected var fakeInactivePhoneUserDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah@tokopedia.com",
        newPhoneNumber = "084444123123"
    )

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        SuccessPageViewAction.checkSuccessPageIsDisplayed()
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    private fun inflateTestFragment() {
        activity?.setUpFragment()
        waitForFragmentResumed()
    }

    open fun startSuccessPageActivity(
        intentModifier: (Intent) -> Unit = {},
        source: String = ""
    ) {
        val intent = InactivePhoneSuccessPageActivityStub.createIntent(context, source, fakeInactivePhoneUserDataModel)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity

        activity?.supportFragmentManager?.let {
            fragmentTransactionIdling = FragmentTransactionIdle(it, InactivePhoneSuccessPageActivityStub.TAG)
        }
    }

    override fun runTest(source: String, test: () -> Unit) {
        startSuccessPageActivity(source = source)
        inflateTestFragment()
        super.runTest(test)
    }

    override fun tearDown() {
        super.tearDown()
        activityAccountListRule.finishActivity()
    }
}