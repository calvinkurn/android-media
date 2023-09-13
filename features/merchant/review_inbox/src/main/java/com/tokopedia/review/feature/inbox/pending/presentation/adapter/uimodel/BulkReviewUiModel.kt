package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory

class BulkReviewUiModel(
    val data: Data
) : Visitable<ReviewPendingAdapterTypeFactory> {
    override fun type(typeFactory: ReviewPendingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Data(
        val title: String,
        val products: List<Product>,
        val productCountFmt: String,
        val appLink: String
    )

    sealed class Product {
        abstract val imageUrl: String

        data class Default(
            override val imageUrl: String
        ) : Product()

        data class More(
            override val imageUrl: String,
            val count: String
        ) : Product()
    }

}
