package com.tokopedia.common.topupbills.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by resakemal on 02/09/19.
 */

class AnalyticUtils {

    companion object {

        fun getVisibleItemIndexes(list: RecyclerView): Pair<Int, Int> {
            var firstPos = 0
            var lastPos = 0
            if (list.layoutManager is LinearLayoutManager) {
                firstPos = (list.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                lastPos = (list.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            } else if (list.layoutManager is GridLayoutManager) {
                firstPos = (list.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                lastPos = (list.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
            }
            return Pair(firstPos, lastPos)
        }

        fun getVisibleItemIndexesOfType(list: RecyclerView, viewType: Int): Pair<Int, Int> {
            val visibleIndexes = getVisibleItemIndexes(list)
            var indexOffset = 0
            list.adapter?.run {
                for (i in visibleIndexes.first..visibleIndexes.second) {
                    if (getItemViewType(i) != viewType) indexOffset++
                }
            }
            return Pair(visibleIndexes.first, visibleIndexes.second - indexOffset)
        }
    }
}
