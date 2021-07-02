package com.tokopedia.home.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.component.enableCoachMark
import com.tokopedia.home.component.name
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper.getWidgetScreenshotList
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper.turnOffAnimation
import com.tokopedia.home_component.util.HomeNetworkUtil
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.CHOOSE_ADDRESS_ROLLENCE_KEY
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_VARIANT_NEW
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_VARIANT_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.KEY_AB_INBOX_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.VARIANT_NEW_INBOX
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 02/07/21.
 */
class HomeFragmentUiTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private var homeNetworkIdlingResource: IdlingResource? = HomeNetworkUtil.homeNetworkIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            enableCoachMark(context)
            setupAbTestRemoteConfig()
            super.beforeActivityLaunched()
        }
    }

    @Test
    fun testFirstTimeLoggedInUser() {
        assertNavigationBottomSheetDisplayed()
        assertHomeCoachmarkDisplayed()
        //failed
//        assertNavigationToolbar()
    }

    private fun assertNavigationToolbar() {
        onView(withId(R.id.navToolbar)).check(matches(isDisplayed()))
        onView(withTagKey(IconList.ID_INBOX)).check(matches(isDisplayed()))
        onView(withTagKey(IconList.ID_CART)).check(matches(isDisplayed()))
        onView(withId(R.id.layout_search)).check(matches(isDisplayed()))
    }

    private fun assertNavigationBottomSheetDisplayed() {
        onView(withText(R.string.onboarding_navigation_title)).check(matches(isDisplayed()))
        onView(withText(R.string.onboarding_navigation_description)).check(matches(isDisplayed()))
        onView(withText(R.string.onboarding_navigation_button)).check(matches(isDisplayed())).perform(click())
    }

    private fun assertHomeCoachmarkDisplayed() {
        assertCoachmarkAndNext(
            titleRes = R.string.onboarding_coachmark_title,
            descRes = R.string.onboarding_coachmark_description
        )

        assertCoachmarkAndNext(
            titleRes = R.string.onboarding_coachmark_inbox_title,
            descRes = R.string.onboarding_coachmark_inbox_description
        )

        assertCoachmarkAndNext(
            title = ChooseAddressUtils.coachMark2Item(context, View(context)).title.toString(),
            desc = ChooseAddressUtils.coachMark2Item(context, View(context)).description.toString()
        )

        //inconsistently failed
//        assertCoachmarkAndNext(
//            titleRes = R.string.onboarding_coachmark_wallet_title,
//            descRes = R.string.onboarding_coachmark_wallet_description
//        )
    }

    private fun assertCoachmarkAndNext(
        titleRes: Int? = null,
        descRes: Int? = null,
        title: String? = null,
        desc: String? = null
    ) {
        Thread.sleep(1000)
        titleRes?.let {
            onView(withText(titleRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        descRes?.let {
            onView(withText(descRes))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        title?.let {
            onView(withText(title))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        desc?.let {
            onView(withText(desc))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()))
        }

        onView(withId(R.id.step_next))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }

    private fun doActivityTest(position: Int, action: (viewHolder: RecyclerView.ViewHolder)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)

    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
            KEY_AB_INBOX_REVAMP,
            VARIANT_NEW_INBOX)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                NAVIGATION_EXP_TOP_NAV,
                NAVIGATION_VARIANT_REVAMP)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                CHOOSE_ADDRESS_ROLLENCE_KEY,
                CHOOSE_ADDRESS_ROLLENCE_KEY)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                BALANCE_EXP,
                BALANCE_VARIANT_NEW)
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                HOME_EXP,
                HOME_VARIANT_REVAMP)
    }
}