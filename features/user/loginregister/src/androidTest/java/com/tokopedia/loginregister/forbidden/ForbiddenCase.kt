package com.tokopedia.loginregister.forbidden

import android.Manifest
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.loginregister.login.behaviour.base.LoginRegisterBase
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import com.tokopedia.loginregister.R as loginregisterR

class ForbiddenCase: LoginRegisterBase() {

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.READ_PHONE_STATE
    )

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ForbiddenActivity::class.java,
        false,
        false
    )

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    private fun setupActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
    }

    @Test
    fun finishingPage_ifClickOnRetryButton() {
        setupActivity()
        shouldBeDisplayed(loginregisterR.id.tv_title_forbidden)
        shouldBeDisplayed(loginregisterR.id.tv_message_forbidden)
        shouldBeDisplayed(loginregisterR.id.btn_retry_forbidden)

        val viewInteraction = Espresso.onView(ViewMatchers.withId(loginregisterR.id.btn_retry_forbidden))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Assert.assertTrue(activityTestRule.activity.isFinishing)
    }
}
