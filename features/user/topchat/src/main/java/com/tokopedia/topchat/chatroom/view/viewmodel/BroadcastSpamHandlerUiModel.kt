package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class BroadcastSpamHandlerUiModel constructor() : Visitable<TopChatTypeFactory> {

    var isLoadingFollowShop = false
    var isLoadingStopBroadCast = false
    val isLoading: Boolean get() = isLoadingFollowShop || isLoadingStopBroadCast

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun startFollowShop() {
        isLoadingFollowShop = true
    }

    fun stopFollowShop() {
        isLoadingFollowShop = false
    }

    fun startBlockPromo() {
        isLoadingStopBroadCast = true
    }

    fun stopBlockPromo() {
        isLoadingStopBroadCast = false
    }

}