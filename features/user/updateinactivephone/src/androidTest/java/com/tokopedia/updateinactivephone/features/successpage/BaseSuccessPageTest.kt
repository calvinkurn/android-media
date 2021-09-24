package com.tokopedia.updateinactivephone.features.successpage

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.stub.features.successpage.InactivePhoneSuccessPageActivityStub
import org.junit.Rule

abstract class BaseSuccessPageTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityAccountListRule = IntentsTestRule(
        InactivePhoneSuccessPageActivityStub::class.java, false, true
    )

    protected var activity: InactivePhoneSuccessPageActivityStub? = null

    open fun startSuccessPageActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = createIntent(context)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, InactivePhoneSuccessPageActivityStub::class.java)
        }
    }
}