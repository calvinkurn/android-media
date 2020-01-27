package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert

class RoomSettingFraudAlertViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingFraudAlert>(itemView) {

    override fun bind(element: RoomSettingFraudAlert?) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_fraud_alert
    }
}