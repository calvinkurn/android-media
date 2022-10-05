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
import com.tokopedia.loginregister.redefineregisteremail.stub.common.clickOnButtonDialog
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
        RedefineRegisterEmailActivity::class.java, false, false
    )

    private lateinit var bundleOptional: Bundle

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: RedefineRegisterRepositoryStub

    @Before
    fun setUp() {
        val paramOptional = RedefineParamUiModel(isRequiredInputPhone = false)
        bundleOptional = Bundle()
        bundleOptional.putParcelable("parameter", paramOptional)

        val fakeBaseComponent = DaggerFakeRedefineRegisterComponent.builder()
            .fakeRedefineRegisterModule(FakeRedefineRegisterModule(applicationContext.applicationContext))
            .build()

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        repositoryStub = fakeBaseComponent.repository() as RedefineRegisterRepositoryStub
    }

    @Test
    fun initRegisterV2ThenFailedViewDisplayed() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_V2_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isGlobalErrorShowing()
    }

    @Test
    fun initRegisterV2AndFailedGetUserInfoThenFailedViewDisplayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isGlobalErrorShowing()
    }

    @Test
    fun initRegisterThenInitViewDisplayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isInitViewDisplayed()
        cantNavigateBack()
    }

    @Test
    fun fieldPhoneErrorEmptyIsDisplayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldEmpty()
    }

    @Test
    fun fieldPhoneErrorTooShortIsDisplayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldPhoneTooShort()
    }

    @Test
    fun fieldPhoneErrorExceedLengthIsDisplayed() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        isErrorFieldPhoneExceedLength()
    }

    @Test
    fun inputInvalidPhoneAndSubmitThenErrorIsDisplayed() {
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
    fun userProfileValidateThenDataInvalid() {
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
    fun userProfileValidateAndDataValidThenShowConfirmPhoneDialog() {
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
    fun userProfileValidateAndDataValidThenFailedUpdateData() {
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
        clickOnButtonDialog("Ya, Benar")
        intended(hasData(ApplinkConstInternalUserPlatform.COTP))
        isGlobalErrorShowing()
    }
}
