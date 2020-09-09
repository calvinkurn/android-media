package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_topchat_room_setting_fraud_alert.view.*

class RoomSettingFraudAlertViewHolder constructor(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<RoomSettingFraudAlert>(itemView) {

    interface Listener {
        fun onClickBlockChatFraudAlert()
    }

    override fun bind(alert: RoomSettingFraudAlert?) {
        if (alert == null) return
        bindAlertText(alert)
    }

    private fun bindAlertText(alert: RoomSettingFraudAlert) {
        val htmlText = HtmlLinkHelper(itemView.context, alert.text)
        bindLinkClick(htmlText)
        itemView.tvText?.movementMethod = LinkMovementMethod.getInstance()
        itemView.tvText?.text = htmlText.spannedString
    }

    private fun bindLinkClick(htmlText: HtmlLinkHelper) {
        htmlText.urlList.forEach { link ->
            link.setOnClickListener {
                if (link.linkUrl == ACTION_BLOCK_USER) {
                    listener.onClickBlockChatFraudAlert()
                } else {
                    RouteManager.route(itemView.context, link.linkUrl)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_fraud_alert

        const val ACTION_BLOCK_USER = "tkpd-internal://topchat_block_personal"
    }
}