package com.tokopedia.minicart.chatlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.cartlist.MiniCartListActionListener
import com.tokopedia.minicart.cartlist.viewholder.*
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatProductViewHolder
import com.tokopedia.minicart.chatlist.viewholder.MiniCartChatUnavailableReasonViewHolder
import com.tokopedia.minicart.databinding.*

class MiniCartChatListAdapterTypeFactory(private val listener: MiniCartChatProductViewHolder.ChatProductListener? = null)
    : BaseAdapterTypeFactory(), MiniCartChatListTypeFactory {

    override fun type(uiModel: LoadingModel): Int {
        return MiniCartLoadingViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartChatProductUiModel): Int {
        return MiniCartChatProductViewHolder.LAYOUT
    }

    override fun type(uiModel: MiniCartChatUnavailableReasonUiModel): Int {
        return MiniCartChatUnavailableReasonViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            MiniCartLoadingViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartLoadingBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartLoadingViewHolder(viewBinding)
            }
            MiniCartChatProductViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatProductBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatProductViewHolder(viewBinding, listener)
            }
            MiniCartChatUnavailableReasonViewHolder.LAYOUT -> {
                val viewBinding = ItemMiniCartChatUnavailableReasonBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
                MiniCartChatUnavailableReasonViewHolder(viewBinding)
            }
            else -> super.createViewHolder(parent, viewType)
        }

    }
}