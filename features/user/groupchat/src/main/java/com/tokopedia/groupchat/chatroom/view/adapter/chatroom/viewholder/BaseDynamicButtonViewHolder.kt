package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory

/**
 * @author : Steven 24/05/19
 */
abstract class BaseDynamicButtonViewHolder<T : Visitable<DynamicButtonTypeFactory>>(itemView: View) : AbstractViewHolder<T>(itemView)
