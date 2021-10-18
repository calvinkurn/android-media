package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingTypeFactory

class ReviewPendingEmptyUiModel(
    val imageUrl: String = "",
    val title: String = "",
    val subtitle: String = ""
) : Visitable<ReviewPendingTypeFactory> {

    override fun type(typeFactory: ReviewPendingTypeFactory): Int {
        return typeFactory.type(this)
    }

}