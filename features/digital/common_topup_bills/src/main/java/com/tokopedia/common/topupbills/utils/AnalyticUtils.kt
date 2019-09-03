package com.tokopedia.common.topupbills.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackImpressionItem

/**
 * Created by resakemal on 02/09/19.
 */

class AnalyticUtils {

    companion object {
        fun <T: Any> getVisibleItems(data: List<TopupBillsTrackImpressionItem<T>>,
                                     list: RecyclerView,
                                     callback: (List<TopupBillsTrackImpressionItem<T>>) -> Unit):
                List<TopupBillsTrackImpressionItem<T>> {
            val trackList = mutableListOf<TopupBillsTrackImpressionItem<T>>()
            val itemPos = getVisibleItemIndexes(list)
            if (itemPos.first >= 0 && itemPos.second <= data.size - 1) {
                for (i in itemPos.first..itemPos.second) {
                    if (!data[i].isTracked) {
                        trackList.add(data[i])
                        data[i].isTracked = true
                    }
                }
                callback(data)
            }
            return trackList
        }

        fun <T: Any> getVisibleItemsOfViewType(data: List<TopupBillsTrackImpressionItem<T>>,
                                               list: RecyclerView,
                                               viewType: Int,
                                               callback: (List<TopupBillsTrackImpressionItem<T>>) -> Unit):
                List<TopupBillsTrackImpressionItem<T>> {
            val trackList = mutableListOf<TopupBillsTrackImpressionItem<T>>()
            val itemPos = getVisibleItemIndexes(list)
            var indexOffset = 0
            list.adapter?.run {
                for (i in itemPos.first..itemPos.second) {
                    if (getItemViewType(i) == viewType) {
                        val index = i - indexOffset
                        if (!data[index].isTracked) {
                            trackList.add(data[index])
                            data[index].isTracked = true
                        }
                    } else {
                        indexOffset++
                    }
                }
            }
            callback(data)
            return trackList
        }

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
    }
}