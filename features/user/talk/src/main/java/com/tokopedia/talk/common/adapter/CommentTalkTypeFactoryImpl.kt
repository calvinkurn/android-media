package com.tokopedia.talk.common.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.LoadProductTalkViewModel
import com.tokopedia.talk.ProductTalkItemViewModel
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder

/**
 * @author by nisie on 9/5/18.
 */
class CommentTalkTypeFactoryImpl() :
        BaseAdapterTypeFactory(),
        ProductTalkChildThreadTypeFactory {

    override fun type(loadMoreModel: LoadProductTalkViewModel): Int {
        return LoadMoreCommentTalkViewHolder.LAYOUT
    }

    override fun type(viewModel: ProductTalkItemViewModel): Int {
        return CommentTalkViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            LoadMoreCommentTalkViewHolder.LAYOUT -> LoadMoreCommentTalkViewHolder(view)
            CommentTalkViewHolder.LAYOUT -> CommentTalkViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }

}