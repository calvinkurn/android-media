package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner

class RoomSettingBannerViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingBanner>(itemView) {

    override fun bind(element: RoomSettingBanner?) {

    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_banner
    }
}