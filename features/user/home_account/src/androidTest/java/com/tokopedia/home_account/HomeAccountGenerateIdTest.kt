package com.tokopedia.home_account

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.common.ViewIdGenerator
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class HomeAccountGenerateIdTest : HomeAccountTest() {

    @Test
    fun generate_view_id_file() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "home_account.csv")
        }
    }

}
