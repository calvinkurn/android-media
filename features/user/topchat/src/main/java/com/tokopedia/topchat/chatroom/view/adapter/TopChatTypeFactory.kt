package com.tokopedia.topchat.chatroom.view.adapter

import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherViewModel

interface TopChatTypeFactory {

    fun type(securityInfoViewModel: SecurityInfoViewModel) : Int

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel) : Int

    fun type(voucherViewModel: TopChatVoucherViewModel) : Int

}
