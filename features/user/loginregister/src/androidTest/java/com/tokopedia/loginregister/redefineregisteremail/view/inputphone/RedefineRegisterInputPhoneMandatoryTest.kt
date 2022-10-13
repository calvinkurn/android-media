package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.content.Context
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.stub.common.launchFragment
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterRepositoryStub
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterTestState
import com.tokopedia.loginregister.redefineregisteremail.stub.di.DaggerFakeRedefineRegisterComponent
import com.tokopedia.loginregister.redefineregisteremail.stub.di.FakeRedefineRegisterModule
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterInputPhoneMandatoryTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java,
        false,
        false
    )

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
    fun is_init_view_is_displayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        isInitViewDisplayed()
    }

    @Test
    fun field_phone_error_empty_is_displayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        isErrorFieldEmpty()
    }

    @Test
    fun field_phone_error_too_short_is_displayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        isErrorFieldPhoneTooShort()
    }

    @Test
    fun field_phone_error_exceed_length_is_displayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        isErrorFieldPhoneExceedLength()
    }

    @Test
    fun input_invalid_phone_and_submit_then_error_is_displayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        isErrorFieldPhoneTooShort()
        clickSubmit()
        isErrorTooShortDisplayed()
    }

    @Test
    fun register_check_then_show_dialog_offer_login() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        isDialogOfferLoginShowing()
    }

    @Test
    fun register_check_then_show_dialog_confirm_phone() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        isDialogConfirmPhoneNumberShowing()
    }

    @Test
    fun register_check_then_show_failed_message() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_CHECK_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)

        inputValidPhone()
        clickSubmit()
        isRegisterCheckFailedMessageDisplayed()
    }

    @Test
    fun register_v2_then_show_failed_view() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_CHECK_NOT_EXIST,
            RedefineRegisterTestState.REGISTER_V2_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleMandatory)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()
        intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        isGlobalErrorShowing()
    }

}
