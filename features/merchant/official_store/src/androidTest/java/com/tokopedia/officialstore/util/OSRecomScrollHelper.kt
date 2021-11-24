package com.tokopedia.officialstore.util

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.kotlin.extensions.view.gone
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

fun removeProgressBarOnOsPage(recyclerView: RecyclerView, activity: Activity) {
    /**
     * This function needed to remove any loading view, because any infinite loop rendered view such as loading view,
     * shimmering, progress bar, etc can block instrumentation test
     */
    recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val checkLoadingView: View? = activity.findViewById<View>(R.id.loading_view)
            checkLoadingView?.let { checkLoadingView.gone() }
        }
    })
}