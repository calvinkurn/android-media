package com.tokopedia.loginregister.login.behaviour.case

import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.domain.pojo.RegisterCheckPojo
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity
import com.tokopedia.managepassword.forgotpassword.view.activity.ForgotPasswordActivity
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test


class LoginNormalCase : LoginBase() {

    @Before
    fun defaultResponse() {
        setDefaultDiscover()
    }


    @Test
    /* Show password input field when user click on submit button */
    fun passwordInputField_Showing() {
        setRegisterCheckDefaultResponse()

        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()

        shouldBeDisplayed(R.id.wrapper_password)
        isDisplayingGivenText(R.id.register_btn, "Masuk")
    }

    @Test
    /* Hide password input field when user click on Ubah button */
    fun passwordInputField_Hidden() {
        setRegisterCheckDefaultResponse()

        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()
        clickUbahButton()

        shouldBeHidden(R.id.wrapper_password)
        isDisplayingGivenText(R.id.register_btn, "Selanjutnya")
    }

    @Test
    /* Show Discover Bottom Sheet when user click on Metode Lain button */
    fun showSocialMediaBottomSheet_True() {
        setRegisterCheckDefaultResponse()
        launchDefaultFragment()

        clickSocmedButton()
        shouldBeDisplayed(R.id.socmed_container)
    }

    @Test
    fun showNotRegisteredDialog_IfEmailNotRegistered() {
        val data = RegisterCheckData(isExist = false, userID = "0", registerType = "email", view = "yoris.prayogo@tokopedia.com")
        registerCheckUseCaseStub.response = RegisterCheckPojo(data = data)
        launchDefaultFragment()

        inputEmailOrPhone("yoris.prayogo@tokopedia.com")
        clickSubmit()

        isDialogDisplayed("Email Belum Terdaftar")
    }

    @Test
    /* Check if RegisterInitialActivity is launching when Daftar button clicked */
    fun goToRegisterInitial_Toolbar() {
        launchDefaultFragment()
        clickTopRegister()
        intended(hasComponent(RegisterInitialActivity::class.java.name))
    }

    @Test
    /* Check if ForgotPasswordActivity is launching when Daftar button clicked */
    fun openTokopediaCarePage() {
        launchDefaultFragment()
        clickForgotPass()
        intended(hasComponent(ForgotPasswordActivity::class.java.name))
    }


    @Test
    fun checkEmailExtensionShownAfterAddAt() {
        launchDefaultFragment()
        inputEmailOrPhone("yoris.prayogo@")
        isEmailExtensionDisplayed()
    }

    @Test
    fun checkEmailExtensionVisibility() {
        checkEmailExtensionShownAfterAddAt()
        onView(withId(R.id.input_email_phone))
                .perform(pressKey(KeyEvent.KEYCODE_DEL))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo")
        isEmailExtensionDismissed()
        inputEmailOrPhone("@")
        isEmailExtensionDisplayed()
    }


    @Test
    fun checkEmailExtensionAdded() {
        checkEmailExtensionShownAfterAddAt()

        onView(withId(R.id.recyclerViewEmailExtension))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(0, clickOnViewChild(R.id.textEmailExtension)))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo@gmail.com")
    }

    @Test
    fun checkEmailExtensionAdded2() {
        checkEmailExtensionShownAfterAddAt()
        onView(withId(R.id.recyclerViewEmailExtension)).perform(scrollToPosition<EmailExtensionAdapter.ViewHolder>(6))
        onView(withId(R.id.recyclerViewEmailExtension))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition<EmailExtensionAdapter.ViewHolder>(6, clickOnViewChild(R.id.textEmailExtension)))
        isDisplayingGivenText(R.id.input_email_phone, "yoris.prayogo@outlook.com")
    }

    @Test
    fun checkEmailExtensionDismissedAfterAdded() {
        checkEmailExtensionAdded()
        isEmailExtensionDismissed()
    }

    @Test
    fun checkDeveloperOptions() {
        launchDefaultFragment()
        val viewDevOpts = onView(withText("Developer Options"))
        if(GlobalConfig.isAllowDebuggingTools()){
            viewDevOpts.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        } else {
            viewDevOpts.check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        }
    }

    @Test
    fun checkOnlyLoading(){
        launchDefaultFragment()
//        onView(withId(R.id.ll_layout)).check(ViewAssertions.matches(hasChildCount(1)))
        val parent : ConstraintLayout = activityTestRule.activity.findViewById<View>(R.id.parent_container) as ConstraintLayout

        assertEquals(parent.childCount, 4)
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }
}
