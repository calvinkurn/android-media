package com.tokopedia.updateinactivephone.features.successpage

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.BaseInactivePhoneTest
import org.junit.Rule

abstract class BaseSuccessPageTest : BaseInactivePhoneTest() {

    @get:Rule
    var activityAccountListRule = IntentsTestRule(
        InactivePhoneSuccessPageActivity::class.java, false, false
    )

    protected var fakeInactivePhoneUserDataModel = InactivePhoneUserDataModel(
        email = "rivaldy.firmansyah@tokopedia.com",
        newPhoneNumber = "084444123123"
    )

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        SuccessPageViewAction.checkSuccessPageIsDisplayed()
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    override fun runTest(source: String, test: () -> Unit) {
        val intent = Intent().apply {
            putExtras(Bundle().apply {
                putString(InactivePhoneConstant.KEY_SOURCE, source)
                putParcelable(InactivePhoneConstant.PARAM_USER_DATA, fakeInactivePhoneUserDataModel)
            })
        }
        activityAccountListRule.launchActivity(intent)
        super.runTest(test)
    }

    override fun tearDown() {
        super.tearDown()
        activityAccountListRule.finishActivity()
    }
}
