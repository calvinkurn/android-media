package com.tokopedia.topchat.chatroom.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel

interface TopChatTypeFactory {

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel): Int

    fun type(voucherViewModel: TopChatVoucherViewModel): Int

    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    fun createViewHolder(parent: ViewGroup, type: Int): AbstractViewHolder<*>

    fun type(roomSettingFraudAlert: RoomSettingFraudAlert): Int

    fun type(roomSettingBanner: RoomSettingBanner): Int

    fun type(quotationViewModel: QuotationViewModel): Int

}
