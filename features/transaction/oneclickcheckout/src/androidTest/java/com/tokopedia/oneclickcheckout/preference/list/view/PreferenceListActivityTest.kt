package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PreferenceListActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(PreferenceListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null
    private var idlingResource1: IdlingResource? = null
    private val interceptor = PreferenceListActivityTestInterceptor()

    private fun setupGraphqlMockResponse() {
        val testInterceptors = listOf(interceptor)
        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    private fun setupIdlingResource() {
        val activity = activityRule.launchActivity(null)
        idlingResource = SwipeRefreshIdlingResource(activity, R.id.swipe_refresh_layout)
        idlingResource1 = ProgressDialogIdlingResource {
            for (fragment in activity.supportFragmentManager.fragments) {
                if (fragment is PreferenceListFragment) {
                    return@ProgressDialogIdlingResource fragment.progressDialog
                }
            }
            return@ProgressDialogIdlingResource null
        }
        IdlingRegistry.getInstance().register(idlingResource, idlingResource1)
    }

    private fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource, idlingResource1)
        activityRule.finishActivity()
    }

    @Test
    fun test() {
        setupGraphqlMockResponse()
        getPreferenceListFailed()
        getPreferenceListSuccess()
        changeDefaultProfileSuccess()
        changeDefaultProfileFailed()
    }

    private fun getPreferenceListFailed() {
        interceptor.customGetPreferenceListThrowable = IOException()

        setupIdlingResource()

        Espresso.onView(ViewMatchers.withId(R.id.main_content)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.GONE, view.visibility)
        }
        Espresso.onView(ViewMatchers.withId(R.id.global_error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        cleanup()
    }

    private fun getPreferenceListSuccess() {
        interceptor.customGetPreferenceListThrowable = null

        setupIdlingResource()

        Espresso.onView(ViewMatchers.withId(R.id.main_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "first profile should not be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                val checkbox = view.findViewById<CheckboxUnify>(R.id.cb_main_preference)
                val lblMain = view.findViewById<Label>(R.id.lbl_main_preference)

                //not default profile
                assertEquals(false, checkbox.isChecked)
                assertEquals(View.GONE, lblMain.visibility)
            }
        }))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "second profile should be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                val checkbox = view.findViewById<CheckboxUnify>(R.id.cb_main_preference)
                val lblMain = view.findViewById<Label>(R.id.lbl_main_preference)

                //default profile
                assertEquals(true, checkbox.isChecked)
                assertEquals(View.VISIBLE, lblMain.visibility)
            }
        }))

        cleanup()
    }

    private fun changeDefaultProfileSuccess() {
        setupIdlingResource()

        Espresso.onView(ViewMatchers.withId(R.id.main_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

        interceptor.customGetPreferenceListResponseString = GET_PREFERENCE_LIST_CHANGED_RESPONSE

        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "perform click on first profile checkbox"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
            }
        }))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "first profile should be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(true, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
                assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
            }
        }))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "second profile should not be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(false, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
                assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
            }
        }))
        cleanup()
    }

    private fun changeDefaultProfileFailed() {
        setupIdlingResource()

        Espresso.onView(ViewMatchers.withId(R.id.main_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

        interceptor.customSetDefaultPreferenceThrowable = IOException()

        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "perform click on second profile checkbox"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
            }
        }))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "first profile should still be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(true, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
                assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
            }
        }))
        Espresso.onView(ViewMatchers.withId(R.id.rv_preference_list)).perform(RecyclerViewActions.actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "second profile should not be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals(false, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
                assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
            }
        }))
        cleanup()
    }
}
