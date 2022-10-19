package com.tokopedia.home_recom.generator

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home_recom.HomeRecommendationActivity
import com.tokopedia.home_recom.SimilarProductRecommendationActivity
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.home_recom.test.R

/**
 * Created by dhaba
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class HomeRecomIdGenerator {
    companion object{
        private const val PACKAGE = "com.tokopedia.home_recom"
        private const val HOME_RECOM_FRAGMENT = "home_recom_fragment_"
        private const val SIMILAR_RECOM_FRAGMENT = "similar_recom_fragment_"
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
    var activityRule = ActivityTestRule(HomeRecommendationActivity::class.java, false, false)

    @get:Rule
    var activitySimilarRecomRule = ActivityTestRule(
        SimilarProductRecommendationActivity::class.java, false, false
    )

    @Test
    fun printResourceRecom() {
        setupGraphqlMockResponse(RecommendationPageMockResponseConfig())
        activityRule.launchActivity(
                HomeRecommendationActivity.newInstance(InstrumentationRegistry.getInstrumentation().targetContext, "547113763").apply {
                    data = Uri.parse("tokopedia://rekomendasi/547113763/?ref=googleshopping")
                }
        )

        Thread.sleep(10000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val homeFragment = parentViewPrinter.printAsCSV(
                view = activityRule.activity.findViewById(R.id.container_home_recom)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "home_recom_fragment.csv",
                text = homeFragment
            )
        }
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun printEachViewHoldersResourceId() {
        setupGraphqlMockResponse(RecommendationPageMockResponseConfig())
        activityRule.launchActivity(
                HomeRecommendationActivity.newInstance(InstrumentationRegistry.getInstrumentation().targetContext, "547113763").apply {
                    data = Uri.parse("tokopedia://rekomendasi/547113763/?ref=googleshopping")
                }
        )

        Thread.sleep(10000)
        printResourceIdForEachViewHolder()
        activityRule.activity.finishAndRemoveTask()
    }

    @Test
    fun printResourceSimilarRecom() {
        setupGraphqlMockResponse(SimilarRecommendationPageMockResponseConfig())
        activitySimilarRecomRule.launchActivity(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                SimilarProductRecommendationActivity::class.java
            )
        )

        Thread.sleep(10000)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val homeFragment = parentViewPrinter.printAsCSV(
                view = activitySimilarRecomRule.activity.findViewById(R.id.container_similar_recom)
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "similar_recom_fragment.csv",
                text = homeFragment
            )
        }
        activitySimilarRecomRule.activity.finishAndRemoveTask()
    }

    @Test
    fun printEachViewHoldersSimilarRecomResourceId() {
        setupGraphqlMockResponse(SimilarRecommendationPageMockResponseConfig())
        activitySimilarRecomRule.launchActivity(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                SimilarProductRecommendationActivity::class.java
            )
        )

        Thread.sleep(10000)
        printResourceIdSimilarRecomForEachViewHolder()
        activitySimilarRecomRule.activity.finishAndRemoveTask()
    }

    private fun printResourceIdForEachViewHolder() {
        val screenshotModelList = HomeRecomScreenshotTestHelper.getWidgetScreenshotList()
        val recyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        printViewHolders(screenshotModelList, recyclerView, HOME_RECOM_FRAGMENT)
    }

    private fun printResourceIdSimilarRecomForEachViewHolder() {
        val screenshotModelList = SimilarRecomScreenshotTestHelper.getWidgetScreenshotList()
        val recyclerView =
            activitySimilarRecomRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        printViewHolders(screenshotModelList, recyclerView, SIMILAR_RECOM_FRAGMENT)
    }

    private fun printViewHolders(
        screenshotModelList: List<ScreenshotModel>,
        recyclerView: RecyclerView,
        fragmentClass: String
    ) {
        screenshotModelList.forEachIndexed { index, screenshotModel ->
            printHomeRecomViewHolderResourceIdAtPosition(index, screenshotModel.name, recyclerView, fragmentClass)
        }
    }

    private fun printHomeRecomViewHolderResourceIdAtPosition(
        position: Int,
        fileNamePostFix: String,
        recyclerView: RecyclerView,
        fragmentClass: String
    ) {
        doActivityTest(position, recyclerView) {
            val homeViewHolder = viewPrinter.printAsCSV(
                view = it.itemView
            )
            fileWriter.writeGeneratedViewIds(
                fileName = "$fragmentClass$fileNamePostFix.csv",
                text = homeViewHolder
            )
        }
    }

    private fun doActivityTest(
        position: Int,
        recyclerView: RecyclerView,
        action: (viewHolder: RecyclerView.ViewHolder) -> Unit,
    ) {
        scrollHomeRecomRecyclerViewToPosition(recyclerView, position)
        Thread.sleep(1750)
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.let {
            action.invoke(viewHolder)
        }
    }

    private fun scrollHomeRecomRecyclerViewToPosition(
        homeRecomRecyclerView: RecyclerView,
        position: Int
    ) {
        val layoutManager = homeRecomRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPosition(position) }
    }
}
