package com.tokopedia.topchat.chatroom.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.TopChatRoomBroadcastFlashSaleProductViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.broadcast.TopChatRoomBroadcastSeeMoreViewHolder
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomBroadcastProductListener
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel

class TopChatRoomBroadcastFlashSaleTypeFactoryImpl(
    private val broadcastUiModel: TopChatRoomBroadcastUiModel,
    private val productListener: TopChatRoomBroadcastProductListener
) : TopChatRoomBroadcastTypeFactory {

    override fun type(
        visitable: Visitable<*>
    ): Int {
        return when (visitable) {
            is ProductAttachmentUiModel -> {
                if (visitable.isProductDummySeeMore()) {
                    TopChatRoomBroadcastSeeMoreViewHolder.LAYOUT
                } else {
                    TopChatRoomBroadcastFlashSaleProductViewHolder.LAYOUT
                }
            }
            else -> {
                throw TypeNotSupportedException.create("Type not supported")
            }
        }
    }

    override fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TopChatRoomBroadcastFlashSaleProductViewHolder.LAYOUT -> TopChatRoomBroadcastFlashSaleProductViewHolder(
                itemView = view,
                broadcastUiModel = broadcastUiModel,
                productListener = productListener
            )
            TopChatRoomBroadcastSeeMoreViewHolder.LAYOUT -> TopChatRoomBroadcastSeeMoreViewHolder(
                itemView = view,
                broadcastUiModel = broadcastUiModel,
                productListener = productListener
            )
            else -> {
                throw TypeNotSupportedException.create("Layout not supported")
            }
        }
    }

    override fun sortData(listVisitable: List<Visitable<*>>): List<Visitable<*>> {
        return listVisitable.sortedWith(
            compareBy {
                if (it is ProductAttachmentUiModel && it.isProductDummySeeMore()) {
                    Int.MAX_VALUE // move dummy to the back
                } else if (it is ProductAttachmentUiModel && it.hasEmptyStock()) {
                    Int.MAX_VALUE - 1 // move products with stock 0 to the back but in front of dummy
                } else {
                    0 // keep original order
                }
            }
        )
    }
}
