package com.tokopedia.oneclickcheckout.common.robot

import android.view.View
import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.list.view.NewMainPreferenceListViewHolder
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListViewHolder
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals

fun preferenceListPage(func: PreferenceListRobot.() -> Unit) = PreferenceListRobot().apply(func)

class PreferenceListRobot {

    fun clickAddPreference() {
        onView(withId(R.id.btn_preference_list_action)).perform(click())
    }

    fun clickEditPreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click on first profile checkbox"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<ImageView>(R.id.iv_edit_preference).callOnClick()
            }
        }))
    }

    fun chooseDefaultPreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, SetDefaultProfileAction()))
    }

    inner class SetDefaultProfileAction : ViewAction {
        override fun getDescription(): String = "perform click on profile main checkbox"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
        }
    }

    fun assertNotDefaultPreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, NotDefaultProfileAssertion()))
    }

    inner class NotDefaultProfileAssertion : ViewAction {

        override fun getDescription(): String = "Profile should not be default"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            assertEquals(false, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
            assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
        }
    }

    fun assertDefaultPreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, DefaultProfileAssertion()))
    }

    inner class DefaultProfileAssertion : ViewAction {

        override fun getDescription(): String = "Profile should be default"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            assertEquals(true, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
            assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
        }
    }

    fun assertMainContentGone() {
        onView(withId(R.id.main_content)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.GONE, view.visibility)
        }
    }

    fun assertGlobalErrorVisible() {
        onView(withId(R.id.global_error)).check(matches(isDisplayed()))
    }

    fun assertMainContentVisible() {
        onView(withId(R.id.main_content)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_preference_list)).check(matches(isDisplayed()))
    }

    fun assertPreferenceView(position: Int,
                             addressName: String,
                             addressStreet: String,
                             shippingName: String,
                             shippingDuration: String,
                             paymentName: String,
                             paymentDetail: String?,
                             isDefaultPreference: Boolean) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "assert preference view"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(addressName, view.findViewById<Typography>(R.id.tv_address_name).text.toString())
                assertEquals(addressStreet, view.findViewById<Typography>(R.id.tv_address_detail).text)
                assertEquals(shippingName, view.findViewById<Typography>(R.id.tv_shipping_name).text)
                assertEquals(shippingDuration, view.findViewById<Typography>(R.id.tv_shipping_duration).text)
                assertEquals(paymentName, view.findViewById<Typography>(R.id.tv_payment_name).text)
                if (paymentDetail != null) {
                    assertEquals(paymentDetail, view.findViewById<Typography>(R.id.tv_payment_detail).text)
                    assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_payment_detail).visibility)
                } else {
                    assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_payment_detail).visibility)
                }
            }
        }))
        if (isDefaultPreference) {
            assertDefaultPreference(position)
        } else {
            assertNotDefaultPreference(position)
        }
    }
}

class PreferenceListBottomSheetRobot {

    fun clickAddPreference() {
        onView(withId(R.id.nested_bottom_sheet_preference_list)).perform(object : ViewAction {
            override fun getDescription(): String = "scroll to bottom"

            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun perform(uiController: UiController?, view: View) {
                val nestedScrollView = view as NestedScrollView
                nestedScrollView.scrollTo(0, nestedScrollView.getChildAt(0).height)
            }
        })
        onView(withId(R.id.btn_add_preference)).perform(click())
    }

    fun clickEditPreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click gear"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<ImageView>(R.id.iv_edit_preference).callOnClick()
            }
        }))
    }

    fun clickUsePreference(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click gunakan"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<Typography>(R.id.tv_choose_preference).callOnClick()
            }
        }))
    }

    fun clickUsePreferenceRevamp(position: Int) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<NewMainPreferenceListViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "perform click gunakan"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<View>(R.id.layout_new_preference_card).callOnClick()
            }
        }))
    }

    fun assertProfile(position: Int, func: (View) -> Unit) {
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(position, object : ViewAction {
            override fun getDescription(): String = "assert profile"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                func(view)
            }
        }))
    }
}