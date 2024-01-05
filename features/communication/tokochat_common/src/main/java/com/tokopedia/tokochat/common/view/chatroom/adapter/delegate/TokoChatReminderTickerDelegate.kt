package com.tokopedia.tokochat.common.view.chatroom.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.ticker.TokoChatReminderTickerViewHolder
import com.tokopedia.tokochat.common.view.chatroom.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatReminderTickerUiModel

class TokoChatReminderTickerDelegate(
    private val listener: TokochatReminderTickerListener
): TypedAdapterDelegate<TokoChatReminderTickerUiModel, Any, TokoChatReminderTickerViewHolder>(
    TokoChatReminderTickerViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokoChatReminderTickerUiModel,
        holder: TokoChatReminderTickerViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokoChatReminderTickerViewHolder {
        return TokoChatReminderTickerViewHolder(basicView, listener)
    }
}
