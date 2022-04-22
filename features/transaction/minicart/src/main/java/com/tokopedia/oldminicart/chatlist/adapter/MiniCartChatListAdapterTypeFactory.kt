package com.tokopedia.oldminicart.chatlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.oldminicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.oldminicart.chatlist.uimodel.MiniCartChatSeparatorUiModel
import com.tokopedia.oldminicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel
import com.tokopedia.oldminicart.chatlist.viewholder.MiniCartChatLoadingViewHolder
import com.tokopedia.oldminicart.chatlist.viewholder.MiniCartChatProductViewHolder
import com.tokopedia.oldminicart.chatlist.viewholder.MiniCartChatSeparatorViewHolder
import com.tokopedia.oldminicart.chatlist.viewholder.MiniCartChatUnavailableReasonViewHolder
import com.tokopedia.minicart.databinding.*

class MiniCartChatListAdapterTypeFactory(private val listener: MiniCartChatProductViewHolder.ChatProductListener? = null)
    : BaseAdapterTypeFactory(), MiniCartChatListTypeFactory {

    override fun type(uiModel: LoadingModel): Int {
        return MiniCartChatLoadingViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartChatUnavailableReasonUiModel): Int {
        return MiniCartChatUnavailableReasonViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartChatProductUiModel): Int {
        return MiniCartChatProductViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartChatSeparatorUiModel): Int {
        return MiniCartChatSeparatorViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            MiniCartChatLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatLoadingViewHolder(viewBinding)
            }
            MiniCartChatProductViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatProductBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatProductViewHolder(viewBinding, listener)
            }
            MiniCartChatUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatUnavailableReasonBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatUnavailableReasonViewHolder(viewBinding)
            }
            MiniCartChatSeparatorViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatSeparatorViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }

    }
}