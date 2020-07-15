package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
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

    @Before
    fun before() {
        val instance = IdlingRegistry.getInstance()
        instance.unregister(*instance.resources.toTypedArray())
    }

    @After
    fun after() {
        val instance = IdlingRegistry.getInstance()
        instance.unregister(*instance.resources.toTypedArray())
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

        onView(withId(R.id.main_content)).check { view, noViewFoundException ->
            noViewFoundException?.printStackTrace()
            assertEquals(View.GONE, view.visibility)
        }
        onView(withId(R.id.global_error)).check(matches(isDisplayed()))

        cleanup()
    }

    private fun getPreferenceListSuccess() {
        interceptor.customGetPreferenceListThrowable = null

        setupIdlingResource()

        onView(withId(R.id.main_content)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_preference_list)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(0, object : ViewAction {
            override fun getDescription(): String = "first profile should not be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals("Address 1", view.findViewById<Typography>(R.id.tv_address_name).text)
                assertEquals(" - User 1 (1)", view.findViewById<Typography>(R.id.tv_address_receiver).text)
                assertEquals("Address Street 1, District 1, City 1, Province 1 1", view.findViewById<Typography>(R.id.tv_address_detail).text)
                assertEquals("Pengiriman Service 1", view.findViewById<Typography>(R.id.tv_shipping_name).text)
                assertEquals(context.getString(R.string.lbl_no_exact_shipping_duration), view.findViewById<Typography>(R.id.tv_shipping_duration).text)
                assertEquals("Payment 1", view.findViewById<Typography>(R.id.tv_payment_name).text)
                assertEquals("Payment Desc 1", view.findViewById<Typography>(R.id.tv_payment_detail).text)
                assertEquals(View.VISIBLE, view.findViewById<Typography>(R.id.tv_payment_detail).visibility)

                val checkbox = view.findViewById<CheckboxUnify>(R.id.cb_main_preference)
                val lblMain = view.findViewById<Label>(R.id.lbl_main_preference)

                //not default profile
                assertEquals(false, checkbox.isChecked)
                assertEquals(View.VISIBLE, checkbox.visibility)
                assertEquals(View.GONE, lblMain.visibility)
            }
        }))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, object : ViewAction {
            override fun getDescription(): String = "second profile should be default"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View) {
                assertEquals("Address 2", view.findViewById<Typography>(R.id.tv_address_name).text)
                assertEquals(" - User 1 (2)", view.findViewById<Typography>(R.id.tv_address_receiver).text)
                assertEquals("Address Street 2, District 2, City 2, Province 2 2", view.findViewById<Typography>(R.id.tv_address_detail).text)
                assertEquals("Pengiriman Service 2", view.findViewById<Typography>(R.id.tv_shipping_name).text)
                assertEquals("Durasi 2", view.findViewById<Typography>(R.id.tv_shipping_duration).text)
                assertEquals("Payment 2", view.findViewById<Typography>(R.id.tv_payment_name).text)
                assertEquals(View.GONE, view.findViewById<Typography>(R.id.tv_payment_detail).visibility)

                val checkbox = view.findViewById<CheckboxUnify>(R.id.cb_main_preference)
                val lblMain = view.findViewById<Label>(R.id.lbl_main_preference)

                //default profile
                assertEquals(true, checkbox.isChecked)
                assertEquals(View.VISIBLE, checkbox.visibility)
                assertEquals(View.VISIBLE, lblMain.visibility)
            }
        }))

        cleanup()
    }

    private fun changeDefaultProfileSuccess() {
        setupIdlingResource()

        onView(withId(R.id.main_content)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

        interceptor.customGetPreferenceListResponseString = GET_PREFERENCE_LIST_CHANGED_RESPONSE

        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(0, SetDefaultProfileAction()))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(0, DefaultProfileAssertion()))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, NotDefaultProfileAssertion()))

        cleanup()
    }

    private fun changeDefaultProfileFailed() {
        setupIdlingResource()

        onView(withId(R.id.main_content)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

        interceptor.customSetDefaultPreferenceThrowable = IOException()

        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, SetDefaultProfileAction()))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(0, DefaultProfileAssertion()))
        onView(withId(R.id.rv_preference_list)).perform(actionOnItemAtPosition<PreferenceListViewHolder>(1, NotDefaultProfileAssertion()))

        cleanup()
    }

    inner class SetDefaultProfileAction : ViewAction {
        override fun getDescription(): String = "perform click on profile main checkbox"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            view.findViewById<CheckboxUnify>(R.id.cb_main_preference).callOnClick()
        }
    }

    inner class DefaultProfileAssertion : ViewAction {

        override fun getDescription(): String = "Profile should be default"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            assertEquals(true, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
            assertEquals(View.VISIBLE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
        }
    }

    inner class NotDefaultProfileAssertion : ViewAction {

        override fun getDescription(): String = "Profile should not be default"

        override fun getConstraints(): Matcher<View>? = null

        override fun perform(uiController: UiController?, view: View) {
            assertEquals(false, view.findViewById<CheckboxUnify>(R.id.cb_main_preference).isChecked)
            assertEquals(View.GONE, view.findViewById<Label>(R.id.lbl_main_preference).visibility)
        }
    }
}
