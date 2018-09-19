package com.tokopedia.talk

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk.producttalk.view.adapter.*
import com.tokopedia.talk.producttalk.view.viewmodel.*

/**
 * @author by nisie on 6/12/18.
 */
class ProductTalkTypeFactoryImpl(private val talkItemListener: ProductTalkThreadViewHolder.TalkItemListener,
                                 private val listener: LoadProductTalkThreadViewHolder.LoadTalkListener,
                                 private val commentTalkListener: CommentTalkViewHolder.TalkCommentItemListener,
                                 private val talkProductAttachmentListener:
                                 TalkProductAttachmentAdapter.ProductAttachmentItemClickListener,
                                 private val talkCommentLoadMoreListener: LoadMoreCommentTalkViewHolder.LoadMoreListener,
                                 private val qaTalkListener: EmptyProductTalkViewHolder.TalkItemListener) :
        BaseAdapterTypeFactory(),
        ProductTalkListTypeFactory {
    override fun type(emptyProductTalkViewModel: EmptyProductTalkViewModel): Int {
        return if(emptyProductTalkViewModel.isMyShop){
            EmptyTalksViewHolder.LAYOUT
        }else {
            EmptyProductTalkViewHolder.LAYOUT
        }
    }

    override fun type(viewModel: ProductTalkTitleViewModel): Int {
        return ProductTalkTitleViewHolder.LAYOUT
    }

    override fun type(viewModel: TalkThreadViewModel): Int {
        return ProductTalkThreadViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadProductTalkThreadViewModel): Int {
        return LoadProductTalkThreadViewHolder.LAYOUT
    }


    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            EmptyTalksViewHolder.LAYOUT -> EmptyTalksViewHolder(view)
            EmptyProductTalkViewHolder.LAYOUT -> EmptyProductTalkViewHolder(view, qaTalkListener)
            ProductTalkTitleViewHolder.LAYOUT -> ProductTalkTitleViewHolder(view)
            ProductTalkThreadViewHolder.LAYOUT -> ProductTalkThreadViewHolder(view,
                    talkItemListener, commentTalkListener, talkProductAttachmentListener, talkCommentLoadMoreListener)
            LoadProductTalkThreadViewHolder.LAYOUT -> LoadProductTalkThreadViewHolder(view, listener)
            else -> super.createViewHolder(view, viewType)
        }
    }

}