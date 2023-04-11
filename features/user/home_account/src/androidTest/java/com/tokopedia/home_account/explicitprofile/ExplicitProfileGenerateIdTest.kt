package com.tokopedia.home_account.explicitprofile

import com.tokopedia.home_account.R
import android.view.View
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.home_account.common.ViewIdGenerator
import com.tokopedia.home_account.explicitprofile.fakes.FakeExplicitProfileActivity
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class ExplicitProfileGenerateIdTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        FakeExplicitProfileActivity::class.java, false, false
    )

    @Test
    fun generate_view_id_file() {
        activityRule.launchActivity(null)

        val rootView = activityRule.activity.findViewById<View>(R.id.pageHeaderLayout).parent as View
        ViewIdGenerator.createViewIdFile(rootView, "explicit_profile.csv")
    }

}
