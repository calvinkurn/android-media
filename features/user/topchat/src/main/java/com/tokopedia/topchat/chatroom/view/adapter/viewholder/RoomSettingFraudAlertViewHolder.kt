package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_topchat_room_setting_fraud_alert.view.*

class RoomSettingFraudAlertViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingFraudAlert>(itemView) {

    override fun bind(alert: RoomSettingFraudAlert?) {
        if (alert == null) return
        bindAlertText(alert)
    }

    private fun bindAlertText(alert: RoomSettingFraudAlert) {
        val htmlText = HtmlLinkHelper(itemView.context, alert.text)
        itemView.tvText?.text = htmlText.spannedString
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_fraud_alert
    }
}