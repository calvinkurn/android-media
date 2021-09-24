package com.tokopedia.updateinactivephone.features.accountlist

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import com.tokopedia.updateinactivephone.stub.features.accountlist.InactivePhoneAccountListActivityStub
import org.junit.Rule


abstract class BaseAccountListTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityAccountListRule = IntentsTestRule(
        InactivePhoneAccountListActivityStub::class.java, false, false
    )

    protected var activity: InactivePhoneAccountListActivityStub? = null

    open fun startAccountListActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = createIntent(context)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity

    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, InactivePhoneAccountListActivityStub::class.java)
        }
    }
}