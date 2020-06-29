package com.tokopedia.talk_old.common.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk_old.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk_old.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk_old.producttalk.view.viewmodel.ProductTalkItemViewModel

/**
 * @author by nisie on 9/5/18.
 */
class CommentTalkTypeFactoryImpl(private val talkCommentItemListener : CommentTalkViewHolder.TalkCommentItemListener,
                                 private val talkProductAttachmentItemClickListener:
                                 TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
                                 private val talkCommentLoadMoreListener : LoadMoreCommentTalkViewHolder.LoadMoreListener) :
        BaseAdapterTypeFactory(),
        ProductTalkChildThreadTypeFactory {

    override fun type(loadMoreModel: LoadMoreCommentTalkViewModel): Int {
        return LoadMoreCommentTalkViewHolder.LAYOUT
    }

    override fun type(viewModel: ProductTalkItemViewModel): Int {
        return CommentTalkViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            LoadMoreCommentTalkViewHolder.LAYOUT -> LoadMoreCommentTalkViewHolder(view, talkCommentLoadMoreListener)
            CommentTalkViewHolder.LAYOUT -> CommentTalkViewHolder(view, talkCommentItemListener, talkProductAttachmentItemClickListener)
            else -> super.createViewHolder(view, viewType)
        }
    }

}