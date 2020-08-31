package com.tokopedia.talk.feature.reply.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reply.presentation.adapter.uimodel.*
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.*
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.*

class TalkReplyAdapterTypeFactory(
        private val attachedProductCardListener: AttachedProductCardListener,
        private val onKebabClickedListener: OnKebabClickedListener,
        private val talkReplyProductHeaderListener: TalkReplyProductHeaderListener,
        private val talkReplyHeaderListener: TalkReplyHeaderListener,
        private val threadListener: ThreadListener
) : BaseAdapterTypeFactory(), TalkReplyTypeFactory {

    override fun type(talkReplyUiModel: TalkReplyUiModel): Int {
        return TalkReplyViewHolder.LAYOUT
    }

    override fun type(talkReplyEmptyModel: TalkReplyEmptyModel): Int {
        return TalkReplyEmptyViewHolder.LAYOUT
    }

    override fun type(talkReplyAnswerCountModel: TalkReplyAnswerCountModel): Int {
        return TalkReplyAnswerCountViewHolder.LAYOUT
    }

    override fun type(talkReplyHeaderModel: TalkReplyHeaderModel): Int {
        return TalkReplyHeaderViewHolder.LAYOUT
    }

    override fun type(talkReplyProductHeaderModel: TalkReplyProductHeaderModel): Int {
        return TalkReplyProductHeaderViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReplyViewHolder.LAYOUT -> TalkReplyViewHolder(parent, attachedProductCardListener, onKebabClickedListener, threadListener)
            TalkReplyEmptyViewHolder.LAYOUT -> TalkReplyEmptyViewHolder(parent)
            TalkReplyAnswerCountViewHolder.LAYOUT -> TalkReplyAnswerCountViewHolder(parent)
            TalkReplyHeaderViewHolder.LAYOUT -> TalkReplyHeaderViewHolder(parent, onKebabClickedListener, talkReplyHeaderListener, threadListener)
            TalkReplyProductHeaderViewHolder.LAYOUT -> TalkReplyProductHeaderViewHolder(parent, talkReplyProductHeaderListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}