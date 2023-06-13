package com.tokopedia.loginregister.login.behaviour.case

import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.utils.LoginSocmedTestHelper
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test

@UiTest
class LoginGenerateIdTest : LoginBase() {

    @Test
    fun generate_view_id_file() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "login.csv")
        }
    }

    @Test
    fun generate_view_id_file_bottom_sheet_socmed() {
        runTest {
            LoginSocmedTestHelper.clickSocmedButton()

            val tagSocmedBottomSheet = activityTestRule.activity.getString(R.string.bottom_sheet_show)
            val rootView = activityTestRule.activity.supportFragmentManager
                .findFragmentByTag(tagSocmedBottomSheet)?.requireView()
            rootView?.let {
                ViewIdGenerator.createViewIdFile(rootView, "login_socmed.csv")
            }
            assert(rootView != null)
        }
    }

}
