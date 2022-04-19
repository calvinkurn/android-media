package com.tokopedia.home.screenshot.dark

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.component.name
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper.getWidgetScreenshotList
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper.turnOffAnimation
import com.tokopedia.home_component.util.HomeNetworkUtil
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.BALANCE_EXP
import com.tokopedia.remoteconfig.RollenceKey.BALANCE_VARIANT_NEW
import com.tokopedia.test.application.annotations.ScreenshotTest

import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 12/04/21.
 */
@ScreenshotTest
class HomeScreenshotLoggedInTest {
    private val TAG = "HomeScreenshotTest"
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private var homeNetworkIdlingResource: IdlingResource? = HomeNetworkUtil.homeNetworkIdlingResource

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            gtmLogDBSource.deleteAll().subscribe()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            setupDarkModeTest(true)
            setupAbTestRemoteConfig()
            disableCoachMark(context)
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    @Test
    fun screenShotVisibleViewLoggedIn() {
        Thread.sleep(10000)
        turnOffAnimation(activityRule.activity)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "dc".name(true, darkMode = true)
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun screenShotEachViewholders() {
        Thread.sleep(10000)
        turnOffAnimation(activityRule.activity)
        doScreenshotForEachViewholder()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun doScreenshotForEachViewholder() {
        val screenshotModelList = getWidgetScreenshotList()
        screenShotList(screenshotModelList)
    }

    private fun screenShotList(screenshotModelList: List<HomeScreenshotTestHelper.ScreenshotModel>) {
        screenshotModelList.forEachIndexed { index, screenshotModel ->
            screenshotHomeViewholdersAtPosition(index, screenshotModel.name)
        }
    }

    private fun screenshotHomeViewholdersAtPosition(
            position: Int,
            fileNamePostFix: String
    ) {
        val recyclerViewId = R.id.home_fragment_recycler_view
        doActivityTest(position) {
            if (it is BannerComponentViewHolder) {
                turnOffAnimation(activityRule.activity)
            }
            findViewHolderAndScreenshot(
                    recyclerViewId = recyclerViewId,
                    position = position,
                    fileName = fileName(),
                    fileNamePostFix = "$fileNamePostFix-dark"
            )
        }
    }

    private fun setupIdlingResource() {
        IdlingRegistry.getInstance().register(homeNetworkIdlingResource)
    }

    private fun fileName(suffix: String? = null): String {
        val prefix = TAG
        suffix?.let {
            return "$prefix-$suffix"
        }
        return prefix
    }

    private fun doActivityTest(position: Int, action: (viewHolder: RecyclerView.ViewHolder)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        scrollHomeRecyclerViewToPosition(homeRecyclerView, position)
        Thread.sleep(8000)
        val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.let {
            action.invoke(viewHolder)
        }
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun setupAbTestRemoteConfig() {
        RemoteConfigInstance.getInstance().abTestPlatform.setString(
                BALANCE_EXP,
                BALANCE_VARIANT_NEW)
    }
}