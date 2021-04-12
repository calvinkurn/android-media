package com.tokopedia.home.screenshot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.component.waitForData
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions.findViewHolderAndScreenshot
import com.tokopedia.test.application.espresso_component.CommonActions.screenShotFullRecyclerView
import com.tokopedia.test.application.espresso_component.CommonActions.takeScreenShotVisibleViewInScreen
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
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
        val homeRv = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
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
    fun screenShotFullRecyclerView() {
        val homeRv = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val adapter = homeRv.adapter as? HomeRecycleAdapter
        waitForData()
        screenShotFullRecyclerView(
                R.id.home_fragment_recycler_view,
                0,
                adapter!!.itemCount,
                fileName("full")
        )
//        findViewAndScreenShot()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun screenShotEachViewholders() {
        findViewHolderAndScreenshot(
                recyclerViewId = R.id.home_fragment_recycler_view,
                position = 0,
                fileName = fileName(),
                fileNamePostFix = "Header",
                shouldDelay = true
        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 6 Image",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 4 Image",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 3 Image",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 1x2 Banner",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 4 Auto",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Lego 6 Auto",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Recommendation List Carousel",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Product Highlight",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Category Widget",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Left Carousel",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Top Carousel",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Dg Bills",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Home Widget 2",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Popular Keyword",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Salam Todo",
//                shouldDelay = true
//        )
//        findViewHolderAndScreenshot(
//                recyclerViewId = R.id.home_fragment_recycler_view,
//                position = 0,
//                fileName = fileName(),
//                fileNamePostFix = "Home Widget",
//                shouldDelay = true
//        )
        activityRule.activity.finishAndRemoveTask()
    }

    private fun fileName(suffix: String? = null): String {
        val prefix = "screenshot-home"
        suffix?.let {
            return "$prefix-$suffix"
        }
        return prefix
    }
}