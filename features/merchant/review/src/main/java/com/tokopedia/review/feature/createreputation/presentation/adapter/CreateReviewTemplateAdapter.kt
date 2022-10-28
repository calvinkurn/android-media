package com.tokopedia.review.feature.createreputation.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil.CreateReviewTemplateDiffUtil
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewTemplateTypeFactory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel

class CreateReviewTemplateAdapter(
    typeFactory: CreateReviewTemplateTypeFactory
) : BaseAdapter<CreateReviewTemplateTypeFactory>(typeFactory) {

    @Suppress("UNCHECKED_CAST")
    fun updateItems(newItems: List<CreateReviewTemplateItemUiModel>) {
        val diffUtilCallback = CreateReviewTemplateDiffUtil(
            visitables.toMutableList() as List<CreateReviewTemplateItemUiModel>,
            newItems
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(newItems)
        result.dispatchUpdatesTo(this)
    }
}