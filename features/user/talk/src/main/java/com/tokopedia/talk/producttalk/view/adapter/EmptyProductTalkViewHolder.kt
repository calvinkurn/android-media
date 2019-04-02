package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.producttalk.view.viewmodel.EmptyProductTalkViewModel

class EmptyProductTalkViewHolder(val v: View, val qaTalkListener: TalkItemListener) :
        AbstractViewHolder<EmptyProductTalkViewModel>(v) {


    interface TalkItemListener {
        fun onAskButtonClick()
        fun onChatClicked()
    }

    companion object {
        val LAYOUT = R.layout.talk_q_a
    }

    val ask: View = itemView.findViewById(R.id.ask)
    val chat: View = itemView.findViewById(R.id.chat)

    private lateinit var adapter: CommentTalkAdapter

    override fun bind(element: EmptyProductTalkViewModel) {
        element?.run {
            ask.setOnClickListener { qaTalkListener.onAskButtonClick() }
            chat.setOnClickListener { qaTalkListener.onChatClicked() }
        }
    }

}
