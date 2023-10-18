package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.content.Context
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.redefineregisteremail.stub.TestIdlingResourceProvider
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
class RedefineRegisterInputPhoneOptionalTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java,
        false,
        false
    )

    private lateinit var bundleOptional: Bundle

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: RedefineRegisterRepositoryStub

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(TestIdlingResourceProvider.countingIdlingResource)
        val paramOptional = RedefineParamUiModel(isRequiredInputPhone = false)
        bundleOptional = Bundle()
        bundleOptional.putParcelable("parameter", paramOptional)

        val fakeBaseComponent = DaggerFakeRedefineRegisterComponent.builder()
            .fakeRedefineRegisterModule(FakeRedefineRegisterModule(applicationContext.applicationContext))
            .build()

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        ActivityComponentFactory.instance = FakeActivityComponentFactory()
        repositoryStub = fakeBaseComponent.repository() as RedefineRegisterRepositoryStub
    }

    @Test
    fun init_register_v2_then_failed_view_displayed() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_V2_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isGlobalErrorShowing()
    }

    @Test
    fun init_register_v2_and_failed_get_user_info_then_failed_view_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isGlobalErrorShowing()
    }

    @Test
    fun init_register_then_init_view_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isInitViewDisplayed()
        cantNavigateBack()
    }

    @Test
    fun field_phone_error_empty_is_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldEmpty()
    }

    @Test
    fun field_phone_error_too_short_is_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldPhoneTooShort()
    }

    @Test
    fun field_phone_error_exceed_length_is_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldPhoneExceedLength()
    }

    @Test
    fun input_invalid_phone_and_submit_then_error_is_displayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldPhoneTooShort()
        clickSubmit()
        isErrorTooShortDisplayed()
    }

    @Test
    fun user_profile_validate_then_data_invalid() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_INVALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        inputValidPhone()
        clickSubmit()
        isAllViewEnabled()
        isUserProfileValidateMessageDisplayed()
    }

    @Test
    fun user_profile_validate_and_data_valid_then_show_confirm_phone_dialog() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        inputValidPhone()
        clickSubmit()
        isDialogConfirmPhoneNumberShowing()
    }

    @Test
    fun user_profile_validate_and_data_valid_then_failed_update_data() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID,
            RedefineRegisterTestState.USER_PROFILE_UPDATE_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()
        intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        isGlobalErrorShowing()
    }
}
