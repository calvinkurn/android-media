package com.tokopedia.updateinactivephone.features.submitnewphone

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.common.idling.FragmentTransactionIdle
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.stub.features.submitnewphone.InactivePhoneSubmitDataActivityStub
import org.junit.Rule

open class BaseSubmitDataTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityAccountListRule = IntentsTestRule(
        InactivePhoneSubmitDataActivityStub::class.java, false, false
    )

    protected var activity: InactivePhoneSubmitDataActivityStub? = null
    protected var fakeInactivePhoneUserDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah@tokopedia.com",
    )

    private fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        SubmitDataViewAction.checkSubmitDataPageDisplayed()
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    private fun inflateTestFragment() {
        activity?.setUpFragment()
        waitForFragmentResumed()
    }

    open fun startSubmitDataActivity(
        intentModifier: (Intent) -> Unit = {},
        source: String = ""
    ) {
        val intent = InactivePhoneSubmitDataActivityStub.createIntent(context, source, fakeInactivePhoneUserDataModel)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity

        activity?.supportFragmentManager?.let {
            fragmentTransactionIdling = FragmentTransactionIdle(it, InactivePhoneSubmitDataActivityStub.TAG)
        }
    }

    override fun runTest(source: String, test: () -> Unit) {
        startSubmitDataActivity(source = source)
        inflateTestFragment()
        super.runTest(test)
    }

    override fun tearDown() {
        super.tearDown()
        activityAccountListRule.finishActivity()
    }
}