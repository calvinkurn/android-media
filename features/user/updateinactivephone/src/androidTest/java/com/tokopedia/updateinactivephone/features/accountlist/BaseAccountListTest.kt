package com.tokopedia.updateinactivephone.features.accountlist

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

    override fun runTest(test: () -> Unit) {
        startAccountListActivity()
        super.runTest(test)
    }

    open fun startAccountListActivity(
        intentModifier: (Intent) -> Unit = { }
    ) {
        val intent = InactivePhoneAccountListActivityStub.createIntent(context)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity
    }

    override fun tearDown() {
        super.tearDown()
        activityAccountListRule.finishActivity()
    }
}