package com.tokopedia.loginregister.registerinitial.tracker

import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.validate
import com.tokopedia.loginregister.common.Event
import com.tokopedia.loginregister.registerinitial.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterInitialTest: RegisterInitialBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val emailNotRegistered = "kelvin.saputra+qa99@tokopedia.com"

    @Test
    fun check_register_email_success_tracker() {
        //Given
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = false ,
            isPending = false,
            userID = "0",
            registerType = "email",
            view = emailNotRegistered
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        val profileInfo = ProfileInfo(userId = "123456", fullName = "Kelvin Saputra")
        val profilePojo = ProfilePojo(profileInfo)
        getProfileUseCaseStub.response = profilePojo

        //When
        runTest {
            checkRegisterEmail()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListSuccess())
    }

    @Test
    fun check_register_email_failed_tracker() {
        //Given
        isDefaultRegisterCheck = false
        registerCheckUseCase.isError = true

        //When
        runTest {
            checkRegisterEmail()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListFailed())
    }

    private fun checkRegisterEmail() {
        Thread.sleep(1000)

        onView(ViewMatchers.withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
                .perform(replaceText(""))
                .perform(typeText(emailNotRegistered))

        onView(withId(R.id.register_btn))
                .perform(click())
    }

    private fun getAnalyticValidatorListSuccess(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar - email",
                "click"
            )
        )
    }

    private fun getAnalyticValidatorListFailed(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar",
                "click"
            ),
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar",
                "failed - ${Event.ANY}"
            )
        )
    }

    @After
    fun finishTest() {
        isDefaultRegisterCheck = true
    }
}
