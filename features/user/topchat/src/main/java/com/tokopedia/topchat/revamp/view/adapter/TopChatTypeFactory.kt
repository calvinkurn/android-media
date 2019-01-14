package com.tokopedia.topchat.revamp.view.adapter

import com.tokopedia.topchat.revamp.view.viewmodel.SecurityInfoViewModel
import com.tokopedia.topchat.revamp.view.viewmodel.ImageDualAnnouncementViewModel

interface TopChatTypeFactory {

    fun type(securityInfoViewModel: SecurityInfoViewModel) : Int

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel) : Int

}
