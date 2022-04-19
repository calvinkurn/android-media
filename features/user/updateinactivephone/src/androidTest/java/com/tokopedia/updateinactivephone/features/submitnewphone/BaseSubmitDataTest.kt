package com.tokopedia.updateinactivephone.features.submitnewphone

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
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
        oldPhoneNumber = "084444000000",
        userIndex = 1
    )

    open fun startSubmitDataActivity(
        intentModifier: (Intent) -> Unit = {},
        source: String = ""
    ) {
        val intent = InactivePhoneSubmitDataActivityStub.createIntent(context, source, fakeInactivePhoneUserDataModel)

        intentModifier(intent)
        activityAccountListRule.launchActivity(intent)
        activity = activityAccountListRule.activity
    }

    override fun runTest(test: () -> Unit) {
        startSubmitDataActivity()
        super.runTest(test)
    }

    override fun runTest(source: String, test: () -> Unit) {
        startSubmitDataActivity(source = source)
        super.runTest(test)
    }

    override fun tearDown() {
        super.tearDown()
        activityAccountListRule.finishActivity()
    }
}