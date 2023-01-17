package com.tokopedia.loginregister.registerinitial.tracker

import android.text.InputType
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher
import com.tokopedia.loginregister.common.CassavaTestRuleMatcher.validate
import com.tokopedia.loginregister.registerinitial.RegisterInitialBase
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.registerinitial.domain.pojo.RegisterCheckPojo
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterInitialPhoneTest: RegisterInitialBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    val phoneNumberNotRegistered = "0851563646951"

    @Test
    fun check_register_phone_failed_tracker() {
        //Given
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = true ,
            isPending = false,
            userID = "0",
            registerType = "phone",
            view = "0851-5636-46951"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        //When
        runTest {
            checkRegisterPhoneNumber()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListFailed())
    }

    @Test
    fun check_register_phone_success_tracker() {
        //Given
        isDefaultRegisterCheck = false
        val data = RegisterCheckData(
            isExist = false ,
            isPending = false,
            userID = "0",
            registerType = "phone",
            view = "0851-5636-46951"
        )
        registerCheckUseCase.response = RegisterCheckPojo(data)

        //When
        runTest {
            checkRegisterPhoneNumber()
        }

        //Then
        validate(cassavaTestRule, getAnalyticValidatorListSuccess())
    }

    //click
    private fun checkRegisterPhoneNumber() {
        Thread.sleep(1000)

        onView(withInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE))
                .perform(replaceText(""))
                .perform(typeText(phoneNumberNotRegistered))

        onView(withId(R.id.register_btn))
                .perform(click())
    }

    private fun getAnalyticValidatorListFailed(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar - phone number",
                "click"
            ),
//            CassavaTestRuleMatcher.getAnalyticValidator(
//                "clickRegister",
//                "register page",
//                "click on button daftar - phone number",
//                "failed - ${Event.ANY}"
//            )
        )
    }

    private fun getAnalyticValidatorListSuccess(): List<Map<String, String>> {
        return listOf(
            CassavaTestRuleMatcher.getAnalyticValidator(
                "clickRegister",
                "register page",
                "click on button daftar - phone number",
                "click"
            ),
            //Add tracker success phone number register
        )
    }

    @After
    fun finishTest() {
        isDefaultRegisterCheck = true
    }
}
