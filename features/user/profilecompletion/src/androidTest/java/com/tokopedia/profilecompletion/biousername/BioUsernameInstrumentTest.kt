package com.tokopedia.profilecompletion.biousername

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameActivity
import com.tokopedia.profilecompletion.di.ActivityComponentFactory
import com.tokopedia.profilecompletion.common.stub.di.TestComponentActivityFactory
import com.tokopedia.profilecompletion.profileinfo.view.activity.ProfileInfoActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BioUsernameInstrumentTest {

    val testComponentFactory = TestComponentActivityFactory()

    lateinit var activity: ProfileInfoActivity

    @get:Rule
    var activityTestRule = IntentsTestRule(
            ChangeBioUsernameActivity::class.java, false, false
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
    fun test_success_create_username() {

    }

    @Test
    fun test_username_already_exists() {

    }

    @Test
    fun test_fail_create_username() {

    }



}