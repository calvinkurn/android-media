package com.tokopedia.topchat.chatroom.domain.pojo.voucher

import com.google.gson.annotations.SerializedName

class TopChatRoomVoucherWrapperDto {
    @SerializedName("voucher")
    var voucher: TopChatRoomVoucherAttachmentDto = TopChatRoomVoucherAttachmentDto()
}
