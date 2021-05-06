package com.tokopedia.oneclickcheckout.preference.list.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.GET_PREFERENCE_LIST_CHANGED_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.preferenceListPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class PreferenceListActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(PreferenceListActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null
    private val interceptor = OneClickCheckoutInterceptor.preferenceInterceptor

    @Before
    fun setupIdlingResource() {
        OneClickCheckoutInterceptor.resetAllCustomResponse()
        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun getPreferenceListFailed() {
        interceptor.customGetPreferenceListThrowable = IOException()

        activityRule.launchActivity(null)

        preferenceListPage {
            assertMainContentGone()
            assertGlobalErrorVisible()
        }
    }

    @Test
    fun getPreferenceListSuccess() {
        activityRule.launchActivity(null)

        preferenceListPage {

            assertMainContentVisible()

            assertPreferenceView(
                    position = 0,
                    addressName = "Address 1 - User 1 (1)",
                    addressStreet = "Address Street 1, District 1, City 1, Province 1 1",
                    shippingName = "Pengiriman Service 1",
                    shippingDuration = context.getString(R.string.lbl_no_exact_shipping_duration),
                    paymentName = "Payment 1",
                    paymentDetail = "Payment Desc 1",
                    isDefaultPreference = false
            )

            assertPreferenceView(
                    position = 1,
                    addressName = "Address 2 - User 1 (2)",
                    addressStreet = "Address Street 2, District 2, City 2, Province 2 2",
                    shippingName = "Pengiriman Service 2",
                    shippingDuration = "Durasi 2",
                    paymentName = "Payment 2",
                    paymentDetail = null,
                    isDefaultPreference = true
            )
        }
    }

    @Test
    fun changeDefaultProfileSuccess() {
        activityRule.launchActivity(null)

        preferenceListPage {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()))
            onView(withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

            assertNotDefaultPreference(0)
            assertDefaultPreference(1)

            interceptor.customGetPreferenceListResponsePath = GET_PREFERENCE_LIST_CHANGED_RESPONSE_PATH

            chooseDefaultPreference(0)

            assertDefaultPreference(0)
            assertNotDefaultPreference(1)
        }
    }

    @Test
    fun changeDefaultProfileFailed() {
        activityRule.launchActivity(null)

        preferenceListPage {
            onView(withId(R.id.main_content)).check(matches(isDisplayed()))
            onView(withId(R.id.rv_preference_list)).check { view, _ -> (view as RecyclerView).adapter!!.itemCount > 1 }

            assertNotDefaultPreference(0)
            assertDefaultPreference(1)

            interceptor.customSetDefaultPreferenceThrowable = IOException()

            chooseDefaultPreference(0)

            assertNotDefaultPreference(0)
            assertDefaultPreference(1)
        }
    }
}
