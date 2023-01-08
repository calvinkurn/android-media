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
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class RedefineRegisterInputPhoneOptionalCassavaTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        RedefineRegisterEmailActivity::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    private lateinit var bundleOptional: Bundle

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: RedefineRegisterRepositoryStub

    @Before
    fun setUp() {
        val paramOptional = RedefineParamUiModel(isRequiredInputPhone = false)
        bundleOptional = Bundle()
        bundleOptional.putParcelable(PARAMETER, paramOptional)

        val fakeBaseComponent = DaggerFakeRedefineRegisterComponent.builder()
            .fakeRedefineRegisterModule(FakeRedefineRegisterModule(applicationContext.applicationContext))
            .build()

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)

        repositoryStub = fakeBaseComponent.repository() as RedefineRegisterRepositoryStub
    }

    @After
    fun finish() {
        activityTestRule.finishActivity()
    }

    @Test
    fun init_register_v2_then_failed() {
        repositoryStub.setResponseQueue(RedefineRegisterTestState.REGISTER_V2_FAILED)
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)

        checkCassavaTest(TEST_CASE_ID_327)
    }

    @Test
    fun user_profile_validate_then_show_confirm_phone_dialog() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID,
            RedefineRegisterTestState.USER_PROFILE_VALIDATE_VALID
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConstInternalUserPlatform.COTP)).respondWithOk()

        inputValidPhone()
        clickSubmit()
        clickSecondaryButtonDialog()
        clickSubmit()
        clickPrimaryButtonDialog()

        checkCassavaTest(TEST_CASE_ID_324)
    }

    @Test
    fun success_register_then_click_lewati() {
        repositoryStub.setResponseQueue(
            RedefineRegisterTestState.REGISTER_V2_SUCCESS,
            RedefineRegisterTestState.GET_USER_INFO_SUCCESS
        )
        activityTestRule.launchFragment(R.id.redefineRegisterInputPhoneFragment, bundleOptional)
        intending(hasData(ApplinkConst.HOME)).respondWithOk()

        clickLewati()

        checkCassavaTest(TEST_CASE_ID_326)
    }

    private fun checkCassavaTest(queryId: String) {
        ViewMatchers.assertThat(
            cassavaRule.validate(queryId),
            hasAllSuccess()
        )
    }

    companion object {
        private const val PARAMETER = "parameter"

        private const val TEST_CASE_ID_327 = "327"
        private const val TEST_CASE_ID_324 = "324"
        private const val TEST_CASE_ID_326 = "326"
    }

}
