package com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel
import com.tokopedia.review.feature.createreputation.presentation.viewholder.CreateReviewTemplateItemViewHolder

class CreateReviewTemplateTypeFactory(
    private val createReviewTemplateListener: CreateReviewTemplateItemViewHolder.Listener
): BaseAdapterTypeFactory() {

    @Suppress("UNUSED_PARAMETER")
    fun type(createReviewTemplateItemUiModel: CreateReviewTemplateItemUiModel): Int {
        return CreateReviewTemplateItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CreateReviewTemplateItemViewHolder.LAYOUT -> CreateReviewTemplateItemViewHolder(parent, createReviewTemplateListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}