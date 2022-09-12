package com.tokopedia.profilecompletion.profileinfo

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.common.stub.di.TestComponentActivityFactory
import com.tokopedia.profilecompletion.profileinfo.view.activity.ProfileInfoActivity
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.view.activity.AddBodActivity
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameActivity
import com.tokopedia.profilecompletion.changegender.view.activity.ChangeGenderActivity
import com.tokopedia.profilecompletion.changename.view.ChangeNameActivity
import com.tokopedia.profilecompletion.common.helper.checkToasterShowing
import com.tokopedia.profilecompletion.common.helper.clickViewHolder
import com.tokopedia.profilecompletion.common.helper.goToAnotherActivity
import com.tokopedia.profilecompletion.common.helper.isViewsExists
import com.tokopedia.profilecompletion.common.helper.clickChildWithViewId
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
            goToAnotherActivity(null, specifyClass = false)
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