package com.tokopedia.updateinactivephone.common.viewmatcher

import com.tokopedia.test.application.matcher.RecyclerViewMatcher

/**
 * Example usage:
 *         onView(
 *              withRecyclerView(R.id.recycler_view)
 *              .atPositionOnView(pos, R.id.btn_secondary)
 *         ).check(matches(ViewMatchers.withText("test")))
 */
fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}