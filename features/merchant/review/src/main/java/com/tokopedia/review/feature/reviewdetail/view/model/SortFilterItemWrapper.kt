package com.tokopedia.review.feature.reviewdetail.view.model

import com.tokopedia.sortfilter.SortFilterItem

/**
 * Created by Yehezkiel on 29/04/20
 */
data class SortFilterItemWrapper(
        val sortFilterItem: SortFilterItem? = null,
        var isSelected: Boolean = false,
        val count: Int = 0,
        val titleUnformated: String = ""
)