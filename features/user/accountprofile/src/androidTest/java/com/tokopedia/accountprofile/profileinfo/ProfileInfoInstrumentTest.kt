package com.tokopedia.accountprofile.profileinfo

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addbod.view.activity.AddBodActivity
import com.tokopedia.accountprofile.settingprofile.changebiousername.view.ChangeBioUsernameActivity
import com.tokopedia.accountprofile.settingprofile.changegender.view.activity.ChangeGenderActivity
import com.tokopedia.accountprofile.settingprofile.changename.view.ChangeNameActivity
import com.tokopedia.accountprofile.common.helper.checkToasterShowing
import com.tokopedia.accountprofile.common.helper.clickChildWithViewId
import com.tokopedia.accountprofile.common.helper.clickOnView
import com.tokopedia.accountprofile.common.helper.clickViewHolder
import com.tokopedia.accountprofile.common.helper.goToAnotherActivity
import com.tokopedia.accountprofile.common.helper.isViewsExists
import com.tokopedia.accountprofile.common.helper.isViewsNotExists
import com.tokopedia.accountprofile.common.helper.swipeUp
import com.tokopedia.accountprofile.common.stub.di.createProfileCompletionComponent
import com.tokopedia.accountprofile.common.webview.ProfileSettingWebViewActivity
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.activity.ProfileInfoActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
open class ProfileInfoInstrumentTest {

    lateinit var activity: ProfileInfoActivity

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ProfileInfoActivity::class.java, false, false
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
    fun header_profile_appear() {
        runTest {
            isViewsExists(listOf(R.id.profileInfoImageSubtitle, R.id.profileInfoImageUnify))
            swipeUp(R.id.nested_scroll_view)
            isViewsExists(listOf(R.id.text_close_account))
        }
    }

    @Test
    fun click_name_go_to_name_page() {
        runTest {
            goToAnotherActivity(ChangeNameActivity::class.java)
            clickViewHolder(SECTION_NAME)
        }
    }

    @Test
    fun click_username_go_to_username_page() {
        runTest {
            goToAnotherActivity(ChangeBioUsernameActivity::class.java)
            clickViewHolder(SECTION_USERNAME)
        }
    }

    @Test
    fun click_bio_go_to_bio_page() {
        runTest {
            goToAnotherActivity(ChangeBioUsernameActivity::class.java)
            clickViewHolder(SECTION_BIO)
        }
    }

    @Test
    fun click_user_id_copied() {
        runTest {
            clickViewHolder(
                SECTION_USERID,
                clickChildWithViewId<IconUnify>(R.id.fragmentProfileItemIcon)
            )
            checkToasterShowing(SUBSTRING_COPY_USER_ID)
        }
    }

    @Test
    fun click_email_go_to_change_email() {
        runTest {
            goToAnotherActivity(ProfileSettingWebViewActivity::class.java)
            clickViewHolder(SECTION_EMAIL)
        }
    }

    @Test
    fun click_phone_go_to_change_phone() {
        runTest {
            goToAnotherActivity(null, specifyClass = false)
            clickViewHolder(SECTION_PHONE)
        }
    }

    @Test
    fun click_gender_go_to_change_gender() {
        runTest {
            goToAnotherActivity(ChangeGenderActivity::class.java)
            clickViewHolder(SECTION_GENDER)
        }
    }

    @Test
    fun click_dob_go_to_change_dob() {
        runTest {
            goToAnotherActivity(AddBodActivity::class.java)
            clickViewHolder(SECTION_DOB)
        }
    }

    @Test
    fun click_close_account_then_user_not_eligible() {
        runTest {
            swipeUp(R.id.nested_scroll_view)
            clickOnView(R.id.text_close_account)

            isViewsExists(
                listOf(
                    R.id.tg_requirement_desc_1,
                    R.id.label_requirement_check_1,
                    R.id.tg_requirement_desc_2,
                    R.id.label_requirement_check_2,
                    R.id.tg_requirement_desc_3_center,
                    R.id.img_requirement_1,
                    R.id.img_requirement_2,
                    R.id.img_requirement_3,
                    R.id.tg_sub_title,
                    R.id.tg_title,
                    R.id.btn_oke
                )
            )
            isViewsNotExists(
                R.id.tg_requirement_desc_1_center,
                R.id.tg_requirement_desc_2_center,
                R.id.tg_requirement_desc_3,
                R.id.label_requirement_check_3,
            )
        }
    }

    open fun runTest(test: () -> Unit) {
        activity = activityTestRule.launchActivity(Intent())
        test.invoke()
    }

    companion object {
        const val SECTION_NAME = "Nama"
        const val SECTION_USERNAME = "Username"
        const val SECTION_BIO = "Bio"
        const val SECTION_USERID = "User ID"
        const val SECTION_EMAIL = "E-mail"
        const val SECTION_PHONE = "Nomor HP"
        const val SECTION_GENDER = "Jenis Kelamin"
        const val SECTION_DOB = "Tanggal Lahir"
        const val SUBSTRING_COPY_USER_ID = "disalin!"
    }
}
