package com.tokopedia.home_recom.generator

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home_recom.SimilarProductRecommendationActivity
import com.tokopedia.home_recom.test.R
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dhaba
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class SimilarRecomIdGenerator {
    companion object{
        private const val PACKAGE = "com.tokopedia.home_recom"
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
        packageName = PACKAGE
    )
    private val viewPrinter = ViewHierarchyPrinter(
        printConditions,
        packageName = PACKAGE
    )
    private val fileWriter = FileWriter()

    @get:Rule
    var activityRule = ActivityTestRule<SimilarProductRecommendationActivity>(SimilarProductRecommendationActivity::class.java, false, false)

    @Before
    fun setup() {
        setupGraphqlMockResponse(SimilarRecommendationPageMockResponseConfig())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SimilarProductRecommendationActivity::class.java))
    }

    @Test
    fun printResourceSimilarRecom() {
        Thread.sleep(10000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val homeFragment = parentViewPrinter.printAsCSV(
                view = activityRule.activity.findViewById(R.id.container_similar_recom)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "similar_recom_fragment.csv",
                text = homeFragment
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun printEachViewHoldersResourceId() {
        Thread.sleep(10000)
        printResourceIdForEachViewHolder()
        activityRule.activity.finishAndRemoveTask()
    }

    private fun printResourceIdForEachViewHolder() {
        val screenshotModelList = SimilarRecomScreenshotTestHelper.getWidgetScreenshotList()
        printViewHolders(screenshotModelList)
    }

    private fun printViewHolders(
        screenshotModelList: List<ScreenshotModel>
    ) {
        screenshotModelList.forEachIndexed { index, screenshotModel ->
            printSimilarRecomViewHolderResourceIdAtPosition(index, screenshotModel.name)
        }
    }

    private fun printSimilarRecomViewHolderResourceIdAtPosition(
        position: Int,
        fileNamePostFix: String
    ) {
        doActivityTest(position) {
            val homeViewHolder = viewPrinter.printAsCSV(
                view = it.itemView
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "similar_recom_fragment_$fileNamePostFix.csv",
                text = homeViewHolder
            )
        }
    }

    private fun doActivityTest(
        position: Int,
        action: (viewHolder: RecyclerView.ViewHolder) -> Unit
    ) {
        val similarRecomRecyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        scrollSimilarRecomRecyclerViewToPosition(similarRecomRecyclerView, position)
        Thread.sleep(1750)
        val viewHolder = similarRecomRecyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.let {
            action.invoke(viewHolder)
        }
    }

    private fun scrollSimilarRecomRecyclerViewToPosition(
        similarRecomRecyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = similarRecomRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPosition(position) }
    }
}
