package com.tokopedia.review.feature.reading.presentation.factory

import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductTopic
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.toPx

class ReadReviewSortFilterFactory {

    companion object {
        const val ICON_SIZE = 16
    }

    fun getTopicFilters(topics: List<ProductTopic>): ArrayList<ListItemUnify> {
        val arrayList = arrayListOf<ListItemUnify>()
        topics.mapTo(arrayList) {
            ListItemUnify(it.formatted, "").apply {
                setVariant(rightComponent = ListItemUnify.CHECKBOX)
            }
        }
        return arrayList
    }

    fun getRatingFilters(ratings: List<String>): ArrayList<ListItemUnify> {
        val arrayList = arrayListOf<ListItemUnify>()
        ratings.mapTo(arrayList) {
            ListItemUnify(it, "").apply {
                setVariant(rightComponent = ListItemUnify.CHECKBOX)
                listDrawableReference = R.drawable.ic_rating_star_item
                listIconWidth = ICON_SIZE.toPx()
                listIconHeight = ICON_SIZE.toPx()
            }
        }
        return arrayList
    }

    fun getSortOptions(sortOptions: List<String>): ArrayList<ListItemUnify> {
        val arrayList = arrayListOf<ListItemUnify>()
        sortOptions.mapTo(arrayList) {
            ListItemUnify(it, "").apply {
                setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            }
        }
        return arrayList
    }
}