package com.tokopedia.tokochat_common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.TokoChatReminderTickerViewHolder
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel

class TokoChatReminderTickerDelegate(
    private val listener: TokochatReminderTickerListener
): TypedAdapterDelegate<TokochatReminderTickerUiModel, Any, TokoChatReminderTickerViewHolder>(
    TokoChatReminderTickerViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokochatReminderTickerUiModel,
        holder: TokoChatReminderTickerViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokoChatReminderTickerViewHolder {
        return TokoChatReminderTickerViewHolder(basicView, listener)
    }
}
