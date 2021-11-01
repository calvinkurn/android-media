package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.ReminderTickerUiModel

class ReminderTickerViewHolder(
    itemView: View?
) : AbstractViewHolder<ReminderTickerUiModel>(itemView) {

    override fun bind(element: ReminderTickerUiModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_chat_reminder_ticker
    }
}