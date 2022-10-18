package com.tokopedia.usercomponents.userconsent

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.usercomponents.common.ViewIdGenerator
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class UserConsentGenerateIdTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        UserConsentDebugActivity::class.java,
        false,
        false
    )

    @Test
    fun generate_view_id_file() {
        activityRule.launchActivity(null)

        val rootView = findRootView(activityRule.activity)
        ViewIdGenerator.createViewIdFile(rootView, "user_consent.csv")
    }

}
