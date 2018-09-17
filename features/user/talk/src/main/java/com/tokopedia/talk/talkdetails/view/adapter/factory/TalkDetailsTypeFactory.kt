package com.tokopedia.talk.talkdetails.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.talkdetails.view.adapter.viewholder.TalkDetailsReplyViewHolder
import com.tokopedia.talk.talkdetails.view.adapter.viewholder.TalkDetailsThreadViewHolder
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsThreadItemViewModel

/**
 * Created by Hendri on 29/08/18.
 */
interface TalkDetailsTypeFactory {

    fun type(talkDetailsCommentViewModel: TalkDetailsCommentViewModel): Int

    fun type(talkDetailsThreadItemViewModel: TalkDetailsThreadItemViewModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}

class TalkDetailsTypeFactoryImpl:BaseAdapterTypeFactory(), TalkDetailsTypeFactory {
    override fun type(talkDetailsCommentViewModel: TalkDetailsCommentViewModel): Int {
        return TalkDetailsReplyViewHolder.LAYOUT
    }

    override fun type(talkDetailsThreadItemViewModel: TalkDetailsThreadItemViewModel): Int {
        return TalkDetailsThreadViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            TalkDetailsReplyViewHolder.LAYOUT -> TalkDetailsReplyViewHolder(parent)
            TalkDetailsThreadViewHolder.LAYOUT -> TalkDetailsThreadViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}