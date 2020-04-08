package com.tokopedia.talk.talkdetails.view.adapter.viewholder

import android.view.View
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import kotlinx.android.synthetic.main.inbox_talk_item.view.*

/**
 * Created by Hendri on 14/09/18.
 */
class TalkDetailsViewHolder(val view: View,
                            val talkItemListener: TalkItemListener,
                            private val talkCommentListener: CommentTalkViewHolder.TalkCommentItemListener,
                            private val talkProductAttachmentItemClickListener: TalkProductAttachmentAdapter.
                            ProductAttachmentItemClickListener,
                            private val talkCommentLoadMoreListener: LoadMoreCommentTalkViewHolder.LoadMoreListener) :
        InboxTalkItemViewHolder(view, talkItemListener, talkCommentListener,
                talkProductAttachmentItemClickListener, talkCommentLoadMoreListener) {
    override fun bind(element: InboxTalkItemViewModel?) {
        super.bind(element)
        containerView.setBackgroundResource(0)
        itemView.replyButton.visibility = View.GONE
        itemSeparator.visibility = View.GONE
    }
}
