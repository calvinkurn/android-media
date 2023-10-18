package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlertUiModel
import com.tokopedia.topchat.databinding.ItemTopchatRoomSettingFraudAlertBinding
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.view.binding.viewBinding

class RoomSettingFraudAlertViewHolder constructor(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<RoomSettingFraudAlertUiModel>(itemView) {

    private val binding: ItemTopchatRoomSettingFraudAlertBinding? by viewBinding()

    interface Listener {
        fun onClickBlockChatFraudAlert()
    }

    override fun bind(alert: RoomSettingFraudAlertUiModel?) {
        if (alert == null) return
        bindAlertText(alert)
    }

    private fun bindAlertText(alert: RoomSettingFraudAlertUiModel) {
        val htmlText = HtmlLinkHelper(itemView.context, alert.text)
        bindLinkClick(htmlText)
        binding?.txtTitle?.movementMethod = LinkMovementMethod.getInstance()
        binding?.txtTitle?.text = htmlText.spannedString
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
