package com.tokopedia.topchat.chatroom.view.adapter

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SecurityInfoViewModel

interface TopChatTypeFactory {

    fun type(securityInfoViewModel: SecurityInfoViewModel) : Int

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementViewModel) : Int

    fun type(merchantVoucherViewModel: MerchantVoucherViewModel) : Int

}
