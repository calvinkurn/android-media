package com.tokopedia.sellerfeedback.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel
import com.tokopedia.sellerfeedback.presentation.viewholder.FeedbackPageViewHolder

class FeedbackPageAdapterFactory(
        private val listener: FeedbackPageViewHolder.FeedbackPageListener
) : BaseAdapterTypeFactory(), FeedbackPageTypeFactory {

    override fun type(uiModel: FeedbackPageUiModel): Int = FeedbackPageViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FeedbackPageViewHolder.LAYOUT -> FeedbackPageViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}