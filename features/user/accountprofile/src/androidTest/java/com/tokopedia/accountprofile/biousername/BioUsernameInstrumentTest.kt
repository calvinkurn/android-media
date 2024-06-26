package com.tokopedia.accountprofile.biousername

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.changebiousername.view.ChangeBioUsernameActivity
import com.tokopedia.accountprofile.common.helper.checkMessageText
import com.tokopedia.accountprofile.common.helper.checkResultCode
import com.tokopedia.accountprofile.common.helper.checkTextOnEditText
import com.tokopedia.accountprofile.common.helper.clickSubmitButton
import com.tokopedia.accountprofile.common.helper.typingTextOn
import com.tokopedia.accountprofile.common.stub.di.createProfileCompletionComponent
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
open class BioUsernameInstrumentTest {

    lateinit var activity: ChangeBioUsernameActivity

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ChangeBioUsernameActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    fun before() {
        val fakeBaseComponent =
            createProfileCompletionComponent(applicationContext.applicationContext)

        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
    }

    @After
    fun after() {
        activityTestRule.finishActivity()
    }

    @Test
    fun test_username_from_data_is_showed() {
        runTest(true) {
            checkTextOnEditText(R.id.et_username, USERNAME_VALID)
        }
    }

    @Test
    fun test_validate_username_valid() {
        runTest(true) {
            typingTextOn(R.id.et_username, USERNAME_VALID)
            checkMessageText(
                R.id.et_username,
                activity.getString(R.string.description_textfield_username)
            )
        }
    }

    @Test
    fun test_validate_username_already_exists() {
        runTest(true) {
            typingTextOn(R.id.et_username, USERNAME_EXISTS)
            checkMessageText(R.id.et_username, ERROR_MESSAGE_USERNAME)
        }
    }

    @Test
    fun test_validate_username_less_than_min_char() {
        runTest(true) {
            typingTextOn(R.id.et_username, USERNAME_LESS_MIN_CHAR)
            checkMessageText(R.id.et_username, ERROR_MESSAGE_MIN_CHAR)
        }
    }

    @Test
    fun test_success_create_username() {
        runTest(true) {
            typingTextOn(R.id.et_username, USERNAME_VALID)
            checkMessageText(
                R.id.et_username,
                activity.getString(R.string.description_textfield_username)
            )
            clickSubmitButton(R.id.btn_submit)
            checkResultCode(activityTestRule.activityResult, Activity.RESULT_OK)
        }
    }

    @Test
    fun test_failed_create_username() {
        runTest(true) {
            typingTextOn(R.id.et_username, USERNAME_FAILED)
            checkMessageText(
                R.id.et_username,
                activity.getString(R.string.description_textfield_username)
            )
            clickSubmitButton(R.id.btn_submit)
            checkMessageText(R.id.et_username, ERROR_MESSAGE_USERNAME)
        }
    }

    @Test
    fun test_success_create_bio() {
        runTest(false) {
            typingTextOn(R.id.et_bio, BIO_VALID)
            clickSubmitButton(R.id.btn_submit)
            checkResultCode(activityTestRule.activityResult, Activity.RESULT_OK)
        }
    }

    @Test
    fun test_fail_create_bio() {
        runTest(false) {
            typingTextOn(R.id.et_bio, BIO_FAILED)
            clickSubmitButton(R.id.btn_submit)
            checkMessageText(R.id.et_bio, ERROR_MESSAGE_BIO)
        }
    }

    open fun runTest(isUsernamePage: Boolean, test: () -> Unit) {
        val intent = if (isUsernamePage) Intent().apply {
            putExtra(
                ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM,
                ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME
            )
        }
        else Intent().apply {
            putExtra(
                ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM,
                ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO
            )
        }
        activity = activityTestRule.launchActivity(intent)
        test.invoke()
    }

    companion object {
        private val USERNAME_VALID = "ramaputra"
        private val USERNAME_LESS_MIN_CHAR = "rm"
        val USERNAME_EXISTS = "username_exists"
        val USERNAME_FAILED = "username_failed"
        val BIO_VALID = "format bio is valid to create bio on profile"
        val BIO_FAILED = "Bio invalid"
        val ERROR_MESSAGE_MIN_CHAR = "Minimum 3 karakter."
        val ERROR_MESSAGE_USERNAME = "Username ini sudah dipakai orang lain."
        val ERROR_MESSAGE_BIO = "Tidak boleh mengandung kata sensitif."
    }

}
