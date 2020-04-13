package com.tokopedia.talk.feature.reading.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingShimmerModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingShimmerViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.viewholder.TalkReadingViewHolder

class TalkReadingAdapterTypeFactory : BaseAdapterTypeFactory(), TalkReadingTypeFactory {

    override fun type(talkReadingUiModel: TalkReadingUiModel): Int {
        return TalkReadingViewHolder.LAYOUT
    }

    override fun type(talkReadingShimmerModel: TalkReadingShimmerModel): Int {
        return TalkReadingShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TalkReadingViewHolder.LAYOUT -> TalkReadingViewHolder(parent)
            TalkReadingShimmerViewHolder.LAYOUT -> TalkReadingShimmerViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}