package com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewBadRatingCategoryViewHolder

class CreateReviewBadRatingCategoriesTypeFactory(
    private val createReviewBadRatingCategoryListener: CreateReviewBadRatingCategoryViewHolder.Listener
) : BaseAdapterTypeFactory() {

    @Suppress("UNUSED_PARAMETER")
    fun type(createReviewBadRatingCategoryUiModel: CreateReviewBadRatingCategoryUiModel): Int {
        return CreateReviewBadRatingCategoryViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CreateReviewBadRatingCategoryViewHolder.LAYOUT -> CreateReviewBadRatingCategoryViewHolder(parent, createReviewBadRatingCategoryListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}