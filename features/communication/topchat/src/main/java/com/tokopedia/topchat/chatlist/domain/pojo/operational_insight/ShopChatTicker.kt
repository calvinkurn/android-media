package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactory

data class ShopChatTicker(
    @SerializedName("Date")
    var date: String? = "",

    @SerializedName("Data")
    var data: ShopChatMetricData? = ShopChatMetricData(),

    @SerializedName("ColorLight")
    var colorLight: ShopChatMetricColorLight? = ShopChatMetricColorLight(),

    @SerializedName("ColorDark")
    var colorDark: ShopChatMetricColorDark? = ShopChatMetricColorDark(),

    @SerializedName("Target")
    var target: ShopChatMetricTarget? = ShopChatMetricTarget(),

    @SerializedName("ShopAppLink")
    var shopScoreApplink: String? = "",

    @SerializedName("OperationalAppLink")
    var operationalApplink: String? = "",

    @SerializedName("IsMaintain")
    var isMaintain: Boolean? = false,

    @SerializedName("ShowTicker")
    var showTicker: Boolean? = false
) : Visitable<ChatListTypeFactory> {

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
