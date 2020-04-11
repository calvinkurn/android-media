package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder.BaseDynamicButtonViewHolder
import com.tokopedia.groupchat.room.view.viewmodel.BaseDynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton

/**
 * @author : Steven 22/05/19
 */
interface DynamicButtonTypeFactory {

    fun createViewHolder(view: View, viewType: Int): BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>>

    fun type(viewModel: DynamicButton): Int

    fun type(viewModel: InteractiveButton): Int
}