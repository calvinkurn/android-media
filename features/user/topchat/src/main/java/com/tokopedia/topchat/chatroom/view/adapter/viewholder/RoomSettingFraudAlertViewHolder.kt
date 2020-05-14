package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
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
        bindLinkClick(htmlText)
        itemView.tvText?.movementMethod = LinkMovementMethod.getInstance()
        itemView.tvText?.text = htmlText.spannedString
    }

    private fun bindLinkClick(htmlText: HtmlLinkHelper) {
        htmlText.urlList.forEach { link ->
            link.setOnClickListener {
                RouteManager.route(itemView.context, link.linkUrl)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_fraud_alert
    }
}