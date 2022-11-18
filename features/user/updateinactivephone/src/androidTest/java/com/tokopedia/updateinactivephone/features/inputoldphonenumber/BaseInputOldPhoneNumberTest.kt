package com.tokopedia.updateinactivephone.features.inputoldphonenumber

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.stub.features.inputoldphonenumber.InputOldPhoneNumberActivityStub
import org.junit.Rule

abstract class BaseInputOldPhoneNumberTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityInputOldPhoneNumberRule = IntentsTestRule(
        InputOldPhoneNumberActivityStub::class.java, false, false
    )

    private var activity: InputOldPhoneNumberActivityStub? = null

    override fun runTest(test: () -> Unit) {
        startInputOldPhoneActivity()
        super.runTest(test)
    }

    open fun startInputOldPhoneActivity(
        intentModifier: (Intent) -> Unit = { }
    ) {
        val intent = InputOldPhoneNumberActivityStub.createIntent(context)

        intentModifier(intent)
        activityInputOldPhoneNumberRule.launchActivity(intent)
        activity = activityInputOldPhoneNumberRule.activity
    }

    override fun tearDown() {
        super.tearDown()
        activityInputOldPhoneNumberRule.finishActivity()
    }
}