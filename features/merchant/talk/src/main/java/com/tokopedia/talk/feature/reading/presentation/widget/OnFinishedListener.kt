package com.tokopedia.talk.feature.reading.presentation.widget

import com.tokopedia.talk.feature.reading.data.model.SortOption

interface OnFinishedListener {
    fun onFinishChooseSort(sortOption: SortOption)
}