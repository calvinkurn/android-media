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

    private fun turnOffAnimation() {
        val homeRv = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)

        //turn off slider banner
        (homeRv.findViewHolderForAdapterPosition(3) as? BannerComponentViewHolder)?.coroutineContext?.cancelChildren()
    }

    private fun doScreenshotForEachViewholder() {
        screenshotHomeViewholdersAtPosition(0, "Header")
        screenshotHomeViewholdersAtPosition(1, "Ticker")
        screenshotHomeViewholdersAtPosition(2, "ATF 1 - Icon")
        screenshotHomeViewholdersAtPosition(3, "ATF 2 - Banner Carousel")
        screenshotHomeViewholdersAtPosition(4, "ATF 3 - Icon")
        screenshotHomeViewholdersAtPosition(5, "ATF 4 - Lego 4 Image")
        screenshotHomeViewholdersAtPosition(6, "Lego 6 Image")
        screenshotHomeViewholdersAtPosition(7, "Lego 4 Image")
        screenshotHomeViewholdersAtPosition(8, "Lego 3 Image")
        screenshotHomeViewholdersAtPosition(9, "1x2 Banner")
        screenshotHomeViewholdersAtPosition(10, "4 Banner Auto")
        screenshotHomeViewholdersAtPosition(11, "6 Image Auto")
        screenshotHomeViewholdersAtPosition(12, "Recommendation List Carousel")
        screenshotHomeViewholdersAtPosition(13, "Product Highlight")
        screenshotHomeViewholdersAtPosition(14, "Category Widget")
        screenshotHomeViewholdersAtPosition(15, "Left Carousel")
        screenshotHomeViewholdersAtPosition(16, "Top Carousel")
        screenshotHomeViewholdersAtPosition(17, "Dg Bills")
        screenshotHomeViewholdersAtPosition(18, "Home Widget 2")
        screenshotHomeViewholdersAtPosition(19, "Popular Keyword")
        screenshotHomeViewholdersAtPosition(20, "Salam Todo")
        screenshotHomeViewholdersAtPosition(21, "Home Widget")
        screenshotHomeViewholdersAtPosition(22, "Home recommendation section")
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