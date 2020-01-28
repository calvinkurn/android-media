package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import androidx.core.text.HtmlCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import kotlinx.android.synthetic.main.item_topchat_room_setting_fraud_alert.view.*

class RoomSettingFraudAlertViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingFraudAlert>(itemView) {

    override fun bind(alert: RoomSettingFraudAlert?) {
        if (alert == null) return
        bindAlertText(alert)
    }

    private fun bindAlertText(alert: RoomSettingFraudAlert) {
        val htmlTEext = HtmlCompat.fromHtml(alert.text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        itemView.tvText?.text = htmlTEext
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_fraud_alert
    }
}