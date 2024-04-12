package com.tokopedia.topchat.chatroom.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

interface TopChatRoomBroadcastFlashSaleTypeFactory {

    fun type(visitable: Visitable<*>): Int

    fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>>
}
