package com.tokopedia.officialstore.util

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.test.application.espresso_component.CommonMatcher

/**
 * Created by yfsx on 11/11/21.
 */
fun preloadRecomOnOSPage(recyclerView: RecyclerView) {
    val itemAdapter: OfficialHomeAdapter = recyclerView.adapter as OfficialHomeAdapter
    Thread.sleep(1000)
    while (!itemAdapter.currentList.any { it is ProductRecommendationDataModel }) {
        Espresso.onView(CommonMatcher.firstView(ViewMatchers.withId(R.id.os_child_recycler_view)))
            .perform(ViewActions.swipeUp())
    }
    Thread.sleep(2500)
    recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)
    Thread.sleep(2500)
}