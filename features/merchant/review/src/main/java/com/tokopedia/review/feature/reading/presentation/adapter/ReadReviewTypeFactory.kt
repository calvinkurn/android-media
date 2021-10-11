package com.tokopedia.review.feature.reading.presentation.adapter

import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel

interface ReadReviewTypeFactory {
    fun type(readReviewUiModel: ReadReviewUiModel): Int
}