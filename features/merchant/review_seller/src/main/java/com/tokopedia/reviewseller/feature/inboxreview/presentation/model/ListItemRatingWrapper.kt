package com.tokopedia.reviewseller.feature.inboxreview.presentation.model

import com.tokopedia.unifycomponents.list.ListItemUnify

data class ListItemRatingWrapper(
        val listItemUnify: ListItemUnify? = null,
        var isSelected: Boolean = false,
        var sortValue: String = ""
)