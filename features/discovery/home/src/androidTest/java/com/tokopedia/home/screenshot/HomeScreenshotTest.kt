package com.tokopedia.home.screenshot

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.component.waitForData
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.CHOOSE_ADDRESS_ROLLENCE_KEY
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.BALANCE_VARIANT_NEW
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_EXP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.HOME_VARIANT_REVAMP
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_EXP_TOP_NAV
import com.tokopedia.remoteconfig.abtest.AbTestPlatform.Companion.NAVIGATION_VARIANT_REVAMP
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import kotlinx.coroutines.cancelChildren
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TAG = "HomeScreenshotTesting"
/**
 * Created by devarafikry on 12/04/21.
 */
class HomeScreenshotTesting {
    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeRevampTestActivity>(InstrumentationHomeRevampTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            gtmLogDBSource.deleteAll().subscribe()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            setupDarkModeTest(false)
            setupAbTestRemoteConfig()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        disableCoachMark(context)
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun screenShotVisibleView() {
        waitForData()
        turnOffAnimation()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            takeScreenShotVisibleViewInScreen(
                    activityRule.activity.window.decorView,
                    fileName(),
                    "dc"
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun screenShotEachViewholders() {
        waitForData()
        turnOffAnimation()
        doScreenshotForEachViewholder()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun setupAbTestRemoteConfig() {
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

    private fun turnOffAnimation() {
        val homeRv = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)

        //turn off slider banner
        (homeRv.findViewHolderForAdapterPosition(3) as? BannerComponentViewHolder)?.coroutineContext?.cancelChildren()
    }

    private fun doScreenshotForEachViewholder() {
        screenshotHomeViewholdersAtPosition(0, "Header")
        screenshotHomeViewholdersAtPosition(1, "Ticker")
        screenshotHomeViewholdersAtPosition(2, "ATF1-Icon")
        screenshotHomeViewholdersAtPosition(3, "ATF2-Banner Carousel")
        screenshotHomeViewholdersAtPosition(4, "ATF3-Icon")
        screenshotHomeViewholdersAtPosition(5, "ATF4-Lego4Image")
        screenshotHomeViewholdersAtPosition(6, "Lego6Image")
        screenshotHomeViewholdersAtPosition(7, "Lego4Image")
        screenshotHomeViewholdersAtPosition(8, "Lego3Image")
        screenshotHomeViewholdersAtPosition(9, "1x2Banner")
        screenshotHomeViewholdersAtPosition(10, "4BannerAuto")
        screenshotHomeViewholdersAtPosition(11, "6ImageAuto")
        screenshotHomeViewholdersAtPosition(12, "RecommendationListCarousel")
        screenshotHomeViewholdersAtPosition(13, "ProductHighlight")
        screenshotHomeViewholdersAtPosition(14, "CategoryWidget")
        screenshotHomeViewholdersAtPosition(15, "LeftCarousel")
        screenshotHomeViewholdersAtPosition(16, "TopCarousel")
        screenshotHomeViewholdersAtPosition(17, "DgBills")
        screenshotHomeViewholdersAtPosition(18, "HomeWidget2")
        screenshotHomeViewholdersAtPosition(19, "PopularKeyword")
        screenshotHomeViewholdersAtPosition(20, "SalamTodo")
        screenshotHomeViewholdersAtPosition(21, "HomeWidget")
        screenshotHomeViewholdersAtPosition(22, "HomeRecommendationSection")
    }

    private fun screenshotHomeViewholdersAtPosition(
            position: Int,
            fileNamePostFix: String
    ) {
        val recyclerViewId = R.id.home_fragment_recycler_view
        doActivityTest(position) {
            findViewHolderAndScreenshot(
                    recyclerViewId = recyclerViewId,
                    position = position,
                    fileName = fileName(),
                    fileNamePostFix = fileNamePostFix
            )
        }
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
        Thread.sleep(5000)
        val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.let {
            action.invoke(viewHolder)
        }
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }
}