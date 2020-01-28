package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import kotlinx.android.synthetic.main.item_topchat_room_setting_banner.view.*

class RoomSettingBannerViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingBanner>(itemView) {

    override fun bind(element: RoomSettingBanner?) {
        if (element == null) return
        bindBannerText(element)
    }

    private fun bindBannerText(element: RoomSettingBanner) {
        itemView.tkBanner?.setHtmlDescription(element.text)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_banner
    }
}