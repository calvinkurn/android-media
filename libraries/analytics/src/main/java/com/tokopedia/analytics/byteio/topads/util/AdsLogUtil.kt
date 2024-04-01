package com.tokopedia.analytics.byteio.topads.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.analytics.byteio.topads.provider.IAdsLogShowOverProvider
import com.tokopedia.kotlin.extensions.view.orZero

fun RecyclerView.addShowOverListener(
    showOverModelList: (List<AdsLogShowOverModel>) -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        val adsLogShowOverList = mutableListOf<AdsLogShowOverModel>()

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                adsLogShowOverList.clear()

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

                val firstVisiblePos = layoutManager?.findFirstVisibleItemPosition().orZero()
                val lastVisiblePos = layoutManager?.findLastVisibleItemPosition().orZero()

                val rect = Rect()

                recyclerView.getGlobalVisibleRect(rect)

                for (i in firstVisiblePos..lastVisiblePos) {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(i) ?: continue

                    if (viewHolder is IAdsLogShowOverProvider && viewHolder.isAds) {
                        val itemView = layoutManager?.findViewByPosition(i)
                        itemView?.let { view ->
                            val adsLogShowOverModel = viewHolder.adsLogShowOverModel
                            adsLogShowOverModel?.let {
                                it.percentageVisible = getVisibleHeightPercentage(view)
                                adsLogShowOverList.add(it)
                            }
                        }
                    }
                }

                showOverModelList(adsLogShowOverList.toList())
            }
        }
    })
}

fun getVisibleHeightPercentage(view: View): String {
    val itemRect = Rect()
    val isChildViewNotEmpty = view.getLocalVisibleRect(itemRect)

    val visibleHeight = itemRect.height().toFloat()
    val originHeight = view.measuredHeight

    val viewVisibleHeightPercentage = visibleHeight / (originHeight * 100)

    return if (isChildViewNotEmpty) {
        viewVisibleHeightPercentage
    } else {
        0.0f
    }.toString()
}
