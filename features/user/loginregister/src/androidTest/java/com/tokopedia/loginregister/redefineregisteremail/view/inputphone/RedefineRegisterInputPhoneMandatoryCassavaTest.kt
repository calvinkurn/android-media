package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.content.Context
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.stub.common.launchFragment
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterRepositoryStub
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterTestState
import com.tokopedia.loginregister.redefineregisteremail.stub.di.DaggerFakeRedefineRegisterComponent
import com.tokopedia.loginregister.redefineregisteremail.stub.di.FakeRedefineRegisterModule
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterInputPhoneMandatoryCassavaTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = false)

    private lateinit var bundleMandatory: Bundle

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: RedefineRegisterRepositoryStub

    @Before
    fun setUp() {
        val paramMandatory = RedefineParamUiModel(isRequiredInputPhone = true)
        bundleMandatory = Bundle()
        bundleMandatory.putParcelable("parameter", paramMandatory)

        val fakeBaseComponent = DaggerFakeRedefineRegisterComponent.builder()
            .fakeRedefineRegisterModule(FakeRedefineRegisterModule(applicationContext.applicationContext))
            .build()

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        repositoryStub = fakeBaseComponent.repository() as RedefineRegisterRepositoryStub
    }

    @Test
    fun test_all_cases() {
        click_button_secondary_on_confirm_dialog()
        click_button_primary_on_confirm_dialog()
        click_button_secondary_on_offering_login_dialog()
        click_button_primary_on_offering_login_dialog()
        failed_register_check()
        user_success_register()
        user_failed_register()

        checkCassavaTest()
    }

    private fun click_button_secondary_on_offering_login_dialog() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        clickSecondaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun click_button_primary_on_offering_login_dialog() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun click_button_secondary_on_confirm_dialog() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        clickSecondaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun click_button_primary_on_confirm_dialog() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun failed_register_check() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()

        activityTestRule.finishActivity()
    }

    private fun user_failed_register() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST,
            RedefineRegisterTestState.REGISTER_V2_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun user_success_register() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST,
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun checkCassavaTest() {
        ViewMatchers.assertThat(
            cassavaRule.validate(QUERY_ID),
            hasAllSuccess()
        )
    }

    companion object {
        private const val QUERY_ID = "308"
    }

}
