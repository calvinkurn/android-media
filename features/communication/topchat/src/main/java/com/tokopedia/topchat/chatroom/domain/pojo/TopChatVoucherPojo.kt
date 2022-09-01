package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopChatVoucherPojo {

    @SerializedName("voucher")
    @Expose
    var voucher: Voucher = Voucher()
    @SerializedName("message")
    @Expose
    var message: String = ""
    @SerializedName("html")
    @Expose
    var html: String = ""

}
