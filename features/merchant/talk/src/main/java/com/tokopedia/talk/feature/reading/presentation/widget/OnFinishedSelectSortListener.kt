package com.tokopedia.talk.feature.reading.presentation.widget

import com.tokopedia.talk.feature.reading.data.model.SortOption

interface OnFinishedSelectSortListener {
    fun onFinishChooseSort(sortOption: SortOption)
}