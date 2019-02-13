package com.tokopedia.topchat.chatroom.view.adapter

import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel

interface TopChatTypeFactory {

    fun type(securityInfoViewModel: SecurityInfoViewModel) : Int

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel) : Int

}
