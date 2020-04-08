package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.producttalk.view.viewmodel.EmptyProductTalkViewModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import android.widget.ImageView

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
    val qaLogo: ImageView = itemView.findViewById(R.id.q_a_logo)
    val bubbleLogo: View = itemView.findViewById(R.id.bubble_logo)

    private lateinit var adapter: CommentTalkAdapter

    override fun bind(element: EmptyProductTalkViewModel) {
        qaLogo.setBackground(MethodChecker.getDrawable(qaLogo.getContext(), R.drawable.ic_q_a))
        bubbleLogo.setBackground(MethodChecker.getDrawable(bubbleLogo.getContext(), R.drawable.ic_bubble_ask));

        element?.run {
            ask.setOnClickListener { qaTalkListener.onAskButtonClick() }
            chat.setOnClickListener { qaTalkListener.onChatClicked() }
        }
    }

}
