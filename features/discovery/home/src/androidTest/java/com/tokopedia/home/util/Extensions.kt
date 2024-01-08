package com.tokopedia.home.util

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.test.application.espresso_component.CommonActions

inline fun <reified M : HomeComponentVisitable, reified VH : RecyclerView.ViewHolder> ActivityTestRule<*>.scrollAndCheckRecyclerViewByType(
    info: RecyclerViewInfo,
    targetId: Int,
    condition: (RecyclerView.ViewHolder) -> Boolean = { true },
) {
    val (view, data) = info
    val index = data.findIndexBy<M>().takeIf { it != -1 } ?: return

    scrollPositionTo(view, index)

    val viewHolder = view.getViewHolderType(index)
    if (viewHolder is VH && condition(viewHolder)) {
        Thread.sleep(8000)
        CommonActions.clickOnEachItemRecyclerView(view, targetId, 0)
    }
}

fun <A : Activity> getRecyclerViewInfo(rule: ActivityTestRule<A>): RecyclerViewInfo {
    val homeRecyclerView = rule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)

    val itemList = homeRecyclerView.getItemList()

    return RecyclerViewInfo(
        view = homeRecyclerView,
        data = itemList
    )
}

fun <A : Activity> ActivityTestRule<A>.scrollPositionTo(view: RecyclerView, position: Int) {
    val layoutManager = view.layoutManager as LinearLayoutManager
    runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
}

fun RecyclerView.getViewHolderType(i: Int): RecyclerView.ViewHolder? {
    return findViewHolderForAdapterPosition(i)
}

inline fun <reified T> List<Visitable<*>>.findIndexBy(): Int {
    val data = find { it is T }
    return indexOf(data)
}

fun RecyclerView.getItemList(): List<Visitable<*>> {
    val homeAdapter = this.adapter as? HomeRecycleAdapter

    if (homeAdapter == null) {
        val detailMessage = "Adapter is not ${HomeRecycleAdapter::class.java.simpleName}"
        throw AssertionError(detailMessage)
    }

    return homeAdapter.currentList
}

data class RecyclerViewInfo(
    val view: RecyclerView,
    val data: List<Visitable<*>>
)
