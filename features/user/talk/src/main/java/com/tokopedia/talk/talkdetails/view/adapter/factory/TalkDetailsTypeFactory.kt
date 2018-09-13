package com.tokopedia.talk.talkdetails.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.common.adapter.ProductTalkChildThreadTypeFactory
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.talkdetails.view.adapter.viewholder.TalkDetailsCommentViewHolder
import com.tokopedia.talk.talkdetails.view.adapter.viewholder.TalkDetailsThreadViewHolder
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsHeaderProductViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsThreadItemViewModel

/**
 * Created by Hendri on 29/08/18.
 */
interface TalkDetailsTypeFactory: ProductTalkChildThreadTypeFactory {

//    fun type(talkDetailsCommentViewModel: TalkDetailsCommentViewModel): Int

    fun type(talkDetailsThreadItemViewModel: TalkDetailsThreadItemViewModel): Int

    fun type(talkDetailsHeaderProductViewModel: TalkDetailsHeaderProductViewModel): Int
}

class TalkDetailsTypeFactoryImpl:BaseAdapterTypeFactory(), TalkDetailsTypeFactory {
    override fun type(viewModel: ProductTalkItemViewModel): Int {
        return CommentTalkViewHolder.LAYOUT
    }

    override fun type(loadMoreCommentTalkViewModel: LoadMoreCommentTalkViewModel): Int {
        return 0 //This is not implemented yet for now.
    }

//    override fun type(talkDetailsCommentViewModel: TalkDetailsCommentViewModel): Int {
//        return TalkDetailsCommentViewHolder.LAYOUT
//    }

    override fun type(talkDetailsThreadItemViewModel: TalkDetailsThreadItemViewModel): Int {
        return TalkDetailsThreadViewHolder.LAYOUT
    }

    override fun type(talkDetailsHeaderProductViewModel: TalkDetailsHeaderProductViewModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            TalkDetailsCommentViewHolder.LAYOUT -> TalkDetailsCommentViewHolder(parent)
            TalkDetailsThreadViewHolder.LAYOUT -> TalkDetailsThreadViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}