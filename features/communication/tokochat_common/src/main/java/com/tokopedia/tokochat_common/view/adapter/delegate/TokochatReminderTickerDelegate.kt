package com.tokopedia.tokochat_common.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.TokochatReminderTickerViewHolder
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel

class TokochatReminderTickerDelegate(
    private val listener: TokochatReminderTickerListener
): TypedAdapterDelegate<TokochatReminderTickerUiModel, Any, TokochatReminderTickerViewHolder>(
    TokochatReminderTickerViewHolder.LAYOUT
) {
    override fun onBindViewHolder(
        item: TokochatReminderTickerUiModel,
        holder: TokochatReminderTickerViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TokochatReminderTickerViewHolder {
        return TokochatReminderTickerViewHolder(basicView, listener)
    }
}
