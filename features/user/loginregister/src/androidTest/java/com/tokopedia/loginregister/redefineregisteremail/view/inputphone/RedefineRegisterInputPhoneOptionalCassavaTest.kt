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
import com.tokopedia.applink.ApplinkConst
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
class RedefineRegisterInputPhoneOptionalCassavaTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = false)

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
    fun test_all_cases() {
        init_register_v2_then_failed()
        init_register_v2_and_failed_get_user_info()
        init_register_then_success()
        user_profile_validate_then_data_invalid()
        user_profile_validate_and_data_valid_then_failed_update_data()
        user_profile_validate_and_data_valid_then_click_primary_button_dialog()
        user_profile_validate_and_data_valid_then_click_secondary_button_dialog()
        success_register_then_click_lewati()

        checkCassavaTest()
    }

    private fun init_register_v2_then_failed() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_V2_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        activityTestRule.finishActivity()
    }

    private fun init_register_v2_and_failed_get_user_info() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_FAILED
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        activityTestRule.finishActivity()
    }

    private fun init_register_then_success() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        activityTestRule.finishActivity()
    }

    private fun user_profile_validate_then_data_invalid() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_INVALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        inputValidPhone()
        clickSubmit()

        activityTestRule.finishActivity()
    }

    private fun user_profile_validate_and_data_valid_then_failed_update_data() {
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

        activityTestRule.finishActivity()
    }

    private fun user_profile_validate_and_data_valid_then_click_primary_button_dialog() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickPrimaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun user_profile_validate_and_data_valid_then_click_secondary_button_dialog() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickSecondaryButtonDialog()

        activityTestRule.finishActivity()
    }

    private fun success_register_then_click_lewati() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConst.HOME)).respondWithOk()

        clickLewati()

        activityTestRule.finishActivity()
    }

    private fun checkCassavaTest() {
        ViewMatchers.assertThat(
            cassavaRule.validate(QUERY_ID),
            hasAllSuccess()
        )
    }

    companion object {
        private const val QUERY_ID = "322"
    }

}
