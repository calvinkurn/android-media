package com.tokopedia.talk_old.talkdetails.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk_old.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk_old.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk_old.inboxtalk.view.adapter.InboxTalkTypeFactoryImpl
import com.tokopedia.talk_old.inboxtalk.view.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk_old.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
//import com.tokopedia.talk.talkdetails.view.adapter.viewholder.TalkDetailsCommentViewHolder
import com.tokopedia.talk_old.talkdetails.view.adapter.viewholder.TalkDetailsViewHolder
//import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel

/**
 * Created by Hendri on 29/08/18.
 */

class TalkDetailsTypeFactoryImpl(private val talkItemListener: InboxTalkItemViewHolder.TalkItemListener,
                                 private val talkCommentItemListener: CommentTalkViewHolder.TalkCommentItemListener,
                                 private val talkAttachmentItemClickListener: TalkProductAttachmentAdapter
                                 .ProductAttachmentItemClickListener,
                                 private val talkCommentLoadMoreListener: LoadMoreCommentTalkViewHolder.LoadMoreListener
):InboxTalkTypeFactoryImpl(talkItemListener, talkCommentItemListener,
        talkAttachmentItemClickListener,talkCommentLoadMoreListener) {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            InboxTalkItemViewHolder.LAYOUT -> TalkDetailsViewHolder(view, talkItemListener,
                    talkCommentItemListener, talkAttachmentItemClickListener,talkCommentLoadMoreListener)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}