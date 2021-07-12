package com.tokopedia.search.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.performance.util.PerformanceDataFileUtils
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.search.R
import com.tokopedia.search.getProductListAdapter
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder

internal class SearchInteractionRobot(
        private val idlingResources: List<IdlingResource?> = listOf(),
        private val searchActivity: SearchActivity
) {

    private val recyclerViewId = R.id.recyclerview
    private val recyclerView = searchActivity.findViewById<RecyclerView>(recyclerViewId)
    private val productListAdapter = recyclerView.getProductListAdapter()
    private var topAdsIndex = productListAdapter.itemList?.getFirstTopAdsProductPosition() ?: -1
    private var organicIndex = productListAdapter.itemList?.getFirstOrganicProductPosition() ?: -1
    private val topAdsCount = productListAdapter.itemList?.filter { it is ProductItemDataView && it.isTopAdsOrOrganicAds() }?.size ?: 0

    private fun List<Visitable<*>>.getFirstTopAdsProductPosition(): Int {
        return indexOfFirst { it is ProductItemDataView && it.isTopAds }
    }

    private fun List<Visitable<*>>.getFirstOrganicProductPosition(): Int {
        return indexOfFirst { it is ProductItemDataView && !it.isTopAds }
    }

    private fun ProductItemDataView.isTopAdsOrOrganicAds() = isTopAds || isOrganicAds

    fun clickNextOrganicProduct() {
        if (organicIndex == -1) return

        onView(withId(recyclerViewId)).perform(scrollToPosition<ProductItemViewHolder>(organicIndex))
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(organicIndex, click()))
    }

    fun clickNextTopAdsProduct() {
        if (topAdsIndex == -1) return

        onView(withId(recyclerViewId)).perform(scrollToPosition<ProductItemViewHolder>(topAdsIndex))
        onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(topAdsIndex, click()))
    }

    fun clickAllTopAdsProduct() {
        val visitableList = recyclerView.getProductListAdapter().itemList
        visitableList.forEachIndexed(this::scrollAndClickTopAds)
    }

    private fun scrollAndClickTopAds(index: Int, visitable: Visitable<*>) {
        if (visitable is ProductItemDataView && visitable.isTopAdsOrOrganicAds()) {
            onView(withId(recyclerViewId)).perform(scrollToPosition<ProductItemViewHolder>(index))
            onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(index, click()))
        }
    }

    fun finish() {
        searchActivity.finish()

        IdlingRegistry.getInstance().unregister(*idlingResources.toTypedArray())
    }

    fun savePLTPerformanceResultData() {
        val performanceData = searchActivity.pltPerformanceResultData
        performanceData?.let {
            val dataSource = getDataSource(it)

            PerformanceDataFileUtils.writePLTPerformanceFile(
                    searchActivity,
                    TEST_CASE_PAGE_LOAD_TIME_SEARCH_PERFORMANCE,
                    it,
                    dataSource
            )
        }
    }

    private fun getDataSource(it: PltPerformanceData): String {
        return if (!it.isSuccess) "failed" else "network"
    }

    infix fun checkGTMTracking(action: GTMTrackingResultRobot.() -> Unit) = GTMTrackingResultRobot().apply {
        action()
    }

    infix fun checkTopAdsTracking(action: TopAdsTrackingResultRobot.() -> Unit) =
            TopAdsTrackingResultRobot(topAdsCount).apply {
                action()
            }

    companion object {
        const val TEST_CASE_PAGE_LOAD_TIME_SEARCH_PERFORMANCE = "search_test_case_page_load_time"
    }
}