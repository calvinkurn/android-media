package com.tokopedia.loginregister.redefineregisteremail.view.inputemail

import android.content.Context
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.redefineregisteremail.stub.common.launchFragment
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterRepositoryStub
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterTestState
import com.tokopedia.loginregister.redefineregisteremail.stub.di.DaggerFakeRedefineRegisterComponent
import com.tokopedia.loginregister.redefineregisteremail.stub.di.FakeRedefineRegisterModule
import com.tokopedia.loginregister.redefineregisteremail.view.RedefineRegisterEmailActivity
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterEmailTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: RedefineRegisterRepositoryStub

    @Before
    fun setUp() {
        val fakeBaseComponent = DaggerFakeRedefineRegisterComponent.builder()
            .fakeRedefineRegisterModule(FakeRedefineRegisterModule(applicationContext.applicationContext))
            .build()

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        repositoryStub = fakeBaseComponent.repository() as RedefineRegisterRepositoryStub
    }

    @Test
    fun isInitViewIsDisplayed() {
        val bundle = Bundle()
        bundle.putParcelable("parameter", RedefineParamUiModel())
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, bundle)

        isInitViewDisplayed()
    }

    @Test
    fun fieldEmailErrorEmptyIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputThenClearFieldTextEmail()
        isErrorFieldEmpty()
    }

    @Test
    fun fieldEmailErrorFormatIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldEmailInvalid()
    }

    @Test
    fun fieldPasswordErrorEmptyIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputThenClearFieldPassword()
        isErrorFieldEmpty()
    }

    @Test
    fun fieldPasswordErrorTooShortIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldPasswordTooShort()
    }

    @Test
    fun fieldPasswordErrorExceedLengthIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldPasswordExceedLength()
    }

    @Test
    fun fieldNameErrorEmptyIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputThenClearFieldName()
        isErrorFieldEmpty()
    }

    @Test
    fun fieldNameErrorTooSortIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldNameTooShort()
    }

    @Test
    fun fieldNameErrorExceedLengthIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldNameExceedLength()
    }

    @Test
    fun fieldNameErrorFormatIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldNameInvalid()
    }

    @Test
    fun inputInvalidFieldAndSubmitClickedThenAllFieldErrorIsDisplayed() {
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        isErrorFieldEmailInvalid()
        isErrorFieldPasswordTooShort()
        clickSubmit()
        isAllFieldErrorDisplayed()
    }

    @Test
    fun submitFieldThenFailedGenerateKey() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.GENERATE_KEY_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputValidValue()
        isButtonSubmitEnabled()
        clickSubmit()
        isAllFieldEnabled()
        isButtonSubmitEnabled()
    }

    @Test
    fun submitFieldThenShowDialogOfferLogin() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.GENERATE_KEY_SUCCESS,
            RedefineRegisterTestState.VALIDATE_USER_DATA_REGISTERED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputValidValue()
        isButtonSubmitEnabled()
        clickSubmit()
        isDialogOfferLoginDisplayed()
    }

    @Test
    fun submitFieldThenShowFailedMessage() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.GENERATE_KEY_SUCCESS,
            RedefineRegisterTestState.VALIDATE_USER_DATA_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterEmailFragment, Bundle())

        inputValidValue()
        isButtonSubmitEnabled()
        clickSubmit()
        isFailedMessageDisplayed()
    }
}
