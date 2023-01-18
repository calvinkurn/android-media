package com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.*
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoAdapterTypeFactory(
    private val optionListener: OrderExtensionRequestInfoOptionViewHolder.SomRequestExtensionOptionListener,
    private val textAreaListener: OrderExtensionRequestInfoCommentViewHolder.OrderExtensionRequestInfoCommentListener,
    private val onPickTimeListener: OrderExtensionRequestInfoPickTimeViewHolder.SomRequestExtensionPickTimeListener
) : BaseAdapterTypeFactory() {

    fun type(descriptionUiModel: OrderExtensionRequestInfoUiModel.DescriptionUiModel): Int {
        return OrderExtensionRequestInfoDescriptionViewHolder.LAYOUT
    }

    fun type(optionUiModel: OrderExtensionRequestInfoUiModel.OptionUiModel): Int {
        return OrderExtensionRequestInfoOptionViewHolder.LAYOUT
    }

    fun type(textAreaUiModel: OrderExtensionRequestInfoUiModel.CommentUiModel): Int {
        return OrderExtensionRequestInfoCommentViewHolder.LAYOUT
    }

    fun type(descriptionShimmerUiModel: OrderExtensionRequestInfoUiModel.DescriptionShimmerUiModel): Int {
        return OrderExtensionRequestInfoDescriptionShimmerViewHolder.LAYOUT
    }

    fun type(optionShimmerUiModel: OrderExtensionRequestInfoUiModel.OptionShimmerUiModel): Int {
        return OrderExtensionRequestInfoOptionShimmerViewHolder.LAYOUT
    }

    fun type(pickTimeShimmerUiModel: OrderExtensionRequestInfoUiModel.PickTimeShimmerUiModel): Int {
        return OrderExtensionRequestInfoPickTimeShimmerViewHolder.LAYOUT
    }

    fun type(pickTimeShimmerUiModel: OrderExtensionRequestInfoUiModel.PickTimeUiModel): Int {
        return OrderExtensionRequestInfoPickTimeViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderExtensionRequestInfoDescriptionViewHolder.LAYOUT -> OrderExtensionRequestInfoDescriptionViewHolder(parent)
            OrderExtensionRequestInfoOptionViewHolder.LAYOUT -> OrderExtensionRequestInfoOptionViewHolder(parent, optionListener)
            OrderExtensionRequestInfoCommentViewHolder.LAYOUT -> OrderExtensionRequestInfoCommentViewHolder(parent, textAreaListener)
            OrderExtensionRequestInfoDescriptionShimmerViewHolder.LAYOUT -> OrderExtensionRequestInfoDescriptionShimmerViewHolder(parent)
            OrderExtensionRequestInfoOptionShimmerViewHolder.LAYOUT -> OrderExtensionRequestInfoOptionShimmerViewHolder(parent)
            OrderExtensionRequestInfoPickTimeShimmerViewHolder.LAYOUT -> OrderExtensionRequestInfoPickTimeShimmerViewHolder(parent)
            OrderExtensionRequestInfoPickTimeViewHolder.LAYOUT -> OrderExtensionRequestInfoPickTimeViewHolder(parent,onPickTimeListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
