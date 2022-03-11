package com.tokopedia.recharge_component.mapper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DigitalPDPAnalyticsUtils {

    companion object {
        fun getVisibleItemIndexes(list: RecyclerView): Pair<Int, Int> {

            var firstPos = 0
            var lastPos = 0
            if (list.layoutManager is LinearLayoutManager) {
                firstPos = (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                lastPos = (list.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            } else if (list.layoutManager is GridLayoutManager) {
                firstPos = (list.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                lastPos = (list.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            return Pair(firstPos, lastPos)
        }

        fun hasVisibleItems(indexes: Pair<Int, Int>): Boolean {
            return indexes.first > -1 && indexes.second > -1
        }
    }
}