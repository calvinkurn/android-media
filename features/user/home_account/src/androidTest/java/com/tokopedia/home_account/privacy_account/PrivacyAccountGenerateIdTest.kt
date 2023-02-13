package com.tokopedia.home_account.privacy_account

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.home_account.common.ViewIdGenerator
import com.tokopedia.home_account.privacy_account.view.PrivacyAccountActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class PrivacyAccountGenerateIdTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        PrivacyAccountActivity::class.java, false, false
    )

    @Test
    fun generate_view_id_file() {

        activityTestRule.launchActivity(Intent())

        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "privacy_account.csv")
    }

}
