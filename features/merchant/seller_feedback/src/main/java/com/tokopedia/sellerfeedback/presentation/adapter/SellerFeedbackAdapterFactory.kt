package com.tokopedia.sellerfeedback.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerfeedback.presentation.uimodel.SellerFeedbackFormUiModel
import com.tokopedia.sellerfeedback.presentation.viewholder.SellerFeedbackFormViewHolder
import com.tokopedia.sellerfeedback.presentation.viewholder.SellerFeedbackFormViewHolder.*

class SellerFeedbackAdapterFactory(
    private val listener: SellerFeedbackFormListener
): BaseAdapterTypeFactory(), SellerFeedbackTypeFactory {


    override fun type(uiModel: SellerFeedbackFormUiModel): Int = SellerFeedbackFormViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SellerFeedbackFormViewHolder.LAYOUT -> SellerFeedbackFormViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
