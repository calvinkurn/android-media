package com.tokopedia.home.generator

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.BuildConfig
import com.tokopedia.home.R
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.screenshot.HomeScreenshotTestHelper
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class HomeIdGenerator {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationAuthHelper.clearUserSession()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            setupAbTestRemoteConfig()
            disableCoachMark(context)
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        }
    }

    private val printConditions = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            val packageName = parent::class.java.`package`?.name.orEmpty()
            val className = parent::class.java.name

            !packageName.startsWith("com.tokopedia")
                    || !className.contains("unify", ignoreCase = true)
        },
        PrintCondition { view ->
            view.id != View.NO_ID || view is ViewGroup
        }
    )

    private val parentPrintCondition = listOf(
        PrintCondition { view ->
            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
            // don't print recyclerview child view, will be printed in separate process
            parent !is RecyclerView
        }
    ) + printConditions

    private val parentViewPrinter = ViewHierarchyPrinter(
        parentPrintCondition,
        customIdPrefix = "P",
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val viewPrinter = ViewHierarchyPrinter(
        printConditions,
        packageName = BuildConfig.LIBRARY_PACKAGE_NAME
    )
    private val fileWriter = FileWriter()

    @Test
    fun printResourceIdHomeLoggedIn() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val homeFragment = parentViewPrinter.printAsCSV(
                view = activityRule.activity.findViewById(R.id.home_swipe_refresh_layout)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "home_fragment.csv",
                text = homeFragment
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun printEachViewHoldersResourceId() {
        Thread.sleep(10000)
        HomeScreenshotTestHelper.turnOffAnimation(activityRule.activity)
        printResourceIdForEachViewHolder()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun printResourceIdForEachViewHolder() {
        val screenshotModelList = HomeScreenshotTestHelper.getWidgetScreenshotList()
        printViewHolders(screenshotModelList)
    }

    private fun printViewHolders(
        screenshotModelList: List<HomeScreenshotTestHelper.ScreenshotModel>
    ) {
        screenshotModelList.forEachIndexed { index, screenshotModel ->
            printHomeViewHolderResourceIdAtPosition(index, screenshotModel.name)
        }
    }

    private fun printHomeViewHolderResourceIdAtPosition(
        position: Int,
        fileNamePostFix: String
    ) {
        doActivityTest(position) {
            if (it is BannerComponentViewHolder) {
                HomeScreenshotTestHelper.turnOffAnimation(activityRule.activity)
            }
            val homeViewHolder = viewPrinter.printAsCSV(
                view = it.itemView
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "home_fragment_$fileNamePostFix.csv",
                text = homeViewHolder
            )
        }
    }

    private fun doActivityTest(
        position: Int,
        action: (viewHolder: RecyclerView.ViewHolder) -> Unit
    ) {
        val homeRecyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        scrollHomeRecyclerViewToPosition(homeRecyclerView, position)
        Thread.sleep(1750)
        val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.let {
            action.invoke(viewHolder)
        }

    }

    private fun scrollHomeRecyclerViewToPosition(
        homeRecyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPosition(position) }
    }
}