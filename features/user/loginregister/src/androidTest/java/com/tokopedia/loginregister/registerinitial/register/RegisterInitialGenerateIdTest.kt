package com.tokopedia.loginregister.registerinitial.register

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.ViewIdGenerator
import com.tokopedia.loginregister.registerinitial.RegisterInitialBase
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Test

@UiTest
class RegisterInitialGenerateIdTest : RegisterInitialBase() {

    @Test
    fun generate_view_id_file_root() {
        runTest {
            val rootView = findRootView(activityTestRule.activity)
            ViewIdGenerator.createViewIdFile(rootView, "register_initial.csv")
        }
    }

    @Test
    fun generate_view_id_file_bottom_sheet_socmed() {
        isDefaultRegisterCheck = false
        registerCheckUseCase.isError = true

        runTest {
            onView(withId(R.id.socmed_btn)).perform(click())

            val tagSocmedBottomSheet = activityTestRule.activity.getString(R.string.bottom_sheet_show)
            val rootView = activityTestRule.activity.supportFragmentManager
                .findFragmentByTag(tagSocmedBottomSheet)?.requireView()
            rootView?.let {
                ViewIdGenerator.createViewIdFile(rootView, "register_socmed.csv")
            }
            assert(rootView != null)
        }
    }

}
