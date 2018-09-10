package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.producttalk.view.viewmodel.EmptyProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState

class EmptyProductTalkViewHolder(val v: View) :
        AbstractViewHolder<EmptyProductTalkViewModel>(v) {


    interface TalkItemListener {
        fun onReplyTalkButtonClick(allowReply: Boolean)
        fun onMenuButtonClicked(menu: TalkState)
    }

    companion object {
        val LAYOUT = R.layout.talk_q_a
    }

    private lateinit var adapter: CommentTalkAdapter

    override fun bind(element: EmptyProductTalkViewModel) {
        element?.run {

        }
    }

}
