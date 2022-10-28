package com.tokopedia.review.feature.createreputation.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil.CreateReviewBadRatingCategoriesDiffUtil
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewBadRatingCategoriesTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable

class CreateReviewBadRatingCategoryAdapter(
    typeFactory: CreateReviewBadRatingCategoriesTypeFactory
) : BaseAdapter<CreateReviewBadRatingCategoriesTypeFactory>(typeFactory) {

    @Suppress("UNCHECKED_CAST")
    fun updateItems(newItems: List<BaseCreateReviewVisitable<CreateReviewBadRatingCategoriesTypeFactory>>) {
        val diffUtilCallback = CreateReviewBadRatingCategoriesDiffUtil(
            visitables.toMutableList() as List<BaseCreateReviewVisitable<CreateReviewBadRatingCategoriesTypeFactory>>,
            newItems
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }
}