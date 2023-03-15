package com.tokopedia.minicart.chatlist.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatSeparatorUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel

interface MiniCartChatListTypeFactory {
    fun type(uiModel: MiniCartChatUnavailableReasonUiModel): Int

    fun type(uiModel: MiniCartChatProductUiModel): Int

    fun type(uiModel: MiniCartChatSeparatorUiModel): Int

    fun createViewHolder(parent: View, viewType: Int): AbstractViewHolder<*>
}
