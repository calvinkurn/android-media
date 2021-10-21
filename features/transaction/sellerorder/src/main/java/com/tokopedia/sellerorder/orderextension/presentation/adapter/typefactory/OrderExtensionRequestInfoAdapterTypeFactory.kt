package com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.OrderExtensionRequestInfoDescriptionViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.OrderExtensionRequestInfoOptionViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.OrderExtensionRequestInfoCommentViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoAdapterTypeFactory(
    private val optionListener: OrderExtensionRequestInfoOptionViewHolder.SomRequestExtensionOptionListener,
    private val textAreaListener: OrderExtensionRequestInfoCommentViewHolder.OrderExtensionRequestInfoCommentListener,
    private val temporaryCommentHolderHelper: OrderExtensionRequestInfoCommentViewHolder.TemporaryCommentHolderHelper
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

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderExtensionRequestInfoDescriptionViewHolder.LAYOUT -> OrderExtensionRequestInfoDescriptionViewHolder(parent)
            OrderExtensionRequestInfoOptionViewHolder.LAYOUT -> OrderExtensionRequestInfoOptionViewHolder(parent, optionListener)
            OrderExtensionRequestInfoCommentViewHolder.LAYOUT -> OrderExtensionRequestInfoCommentViewHolder(parent, textAreaListener, temporaryCommentHolderHelper)
            else -> super.createViewHolder(parent, type)
        }
    }
}