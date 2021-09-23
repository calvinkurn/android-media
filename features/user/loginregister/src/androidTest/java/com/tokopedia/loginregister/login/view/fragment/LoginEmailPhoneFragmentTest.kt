package com.tokopedia.loginregister.login.view.fragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.test.application.annotations.UiAnalyticsTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@UiAnalyticsTest
class LoginEmailPhoneFragmentTest: LoginBase() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    val email = "fernanda.panca+qc8@tokopedia.com"

    @Test
    fun check_login_events_after_email_password_login() {
        //Given
        val password = "nopassword"

        //When
        runTest {
            inputEmailOrPhone("yoris.prayogo@tokopedia.com")
            clickSubmit()
            inputPassword(password)
            clickSubmit()
        }

        //Then
        //TODO: Validate BranchIO and Appsflyer tracker
        val loginBranchIOQuery = "tracker/loginregister/login_branch_io.json"
        val loginAppsFlyerQuery = "tracker/loginregister/login_app_flyer.json"
    }
}