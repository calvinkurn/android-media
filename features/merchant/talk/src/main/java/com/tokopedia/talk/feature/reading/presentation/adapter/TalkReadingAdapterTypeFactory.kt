package com.tokopedia.talk.feature.reading.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingEmptySpace
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingEmptySpaceViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingShimmerViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingViewHolder
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener

class TalkReadingAdapterTypeFactory(
        private val threadListener: ThreadListener
) : BaseAdapterTypeFactory(), TalkReadingTypeFactory {

    override fun type(talkReadingUiModel: TalkReadingUiModel): Int {
        return TalkReadingViewHolder.LAYOUT
    }

    override fun type(talkReadingEmpty: TalkReadingEmptySpace): Int {
        return TalkReadingEmptySpaceViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return TalkReadingShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReadingViewHolder.LAYOUT -> TalkReadingViewHolder(parent, threadListener)
            TalkReadingShimmerViewHolder.LAYOUT -> TalkReadingShimmerViewHolder(parent)
            TalkReadingEmptySpaceViewHolder.LAYOUT -> TalkReadingEmptySpaceViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}