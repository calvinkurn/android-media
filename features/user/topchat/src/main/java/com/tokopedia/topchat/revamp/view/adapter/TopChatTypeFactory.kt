package com.tokopedia.topchat.revamp.view.adapter

import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageDualAnnouncementViewModel

interface TopChatTypeFactory {

    fun type(securityInfoViewModel: SecurityInfoViewModel) : Int

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel) : Int

}
