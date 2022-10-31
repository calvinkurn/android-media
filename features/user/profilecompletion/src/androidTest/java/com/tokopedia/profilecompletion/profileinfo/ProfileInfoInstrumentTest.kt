package com.tokopedia.profilecompletion.profileinfo

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.view.activity.AddBodActivity
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameActivity
import com.tokopedia.profilecompletion.changegender.view.activity.ChangeGenderActivity
import com.tokopedia.profilecompletion.changename.view.ChangeNameActivity
import com.tokopedia.profilecompletion.common.helper.checkToasterShowing
import com.tokopedia.profilecompletion.common.helper.clickChildWithViewId
import com.tokopedia.profilecompletion.common.helper.clickOnView
import com.tokopedia.profilecompletion.common.helper.clickViewHolder
import com.tokopedia.profilecompletion.common.helper.goToAnotherActivity
import com.tokopedia.profilecompletion.common.helper.isViewsExists
import com.tokopedia.profilecompletion.common.helper.isViewsNotExists
import com.tokopedia.profilecompletion.common.helper.swipeUp
import com.tokopedia.profilecompletion.common.stub.di.TestComponentActivityFactory
import com.tokopedia.profilecompletion.common.webview.ProfileSettingWebViewActivity
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.profileinfo.view.activity.ProfileInfoActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class ProfileInfoInstrumentTest {

    val testComponentFactory = TestComponentActivityFactory()

    lateinit var activity: ProfileInfoActivity

    @get:Rule
    var activityTestRule = IntentsTestRule(
            ProfileInfoActivity::class.java, false, false
    )

    @Before
    fun before() {
        ActivityComponentFactory.instance = testComponentFactory
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
            clickViewHolder(SECTION_USERID, clickChildWithViewId<IconUnify>(R.id.fragmentProfileItemIcon))
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

    private fun runTest(test: () -> Unit) {
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
