package com.tokopedia.talk_old.inboxtalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk_old.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk_old.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk_old.inboxtalk.view.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk_old.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkItemViewModel

/**
 * @author by nisie on 8/29/18.
 */

open class InboxTalkTypeFactoryImpl(private val talkItemListener: InboxTalkItemViewHolder
.TalkItemListener,
                               private val talkCommentItemListener: CommentTalkViewHolder
                               .TalkCommentItemListener,
                               private val talkAttachmentItemClickListener: TalkProductAttachmentAdapter
                               .ProductAttachmentItemClickListener,
                               private val talkCommentLoadMoreListener: LoadMoreCommentTalkViewHolder.LoadMoreListener) :
        BaseAdapterTypeFactory(),
        InboxTalkTypeFactory {

    override fun type(inboxTalkItemViewModel: InboxTalkItemViewModel): Int {
        return InboxTalkItemViewHolder.LAYOUT
    }

    override fun type(emptyInboxTalkViewModel: EmptyInboxTalkViewModel): Int {
        return EmptyInboxTalkViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            InboxTalkItemViewHolder.LAYOUT -> InboxTalkItemViewHolder(view, talkItemListener,
                    talkCommentItemListener, talkAttachmentItemClickListener, talkCommentLoadMoreListener)
            EmptyInboxTalkViewHolder.LAYOUT -> EmptyInboxTalkViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}