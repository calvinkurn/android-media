package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingEmptySpace
import com.tokopedia.talk.R

class TalkReadingEmptySpaceViewHolder(view: View) : AbstractViewHolder<TalkReadingEmptySpace>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reading_empty_space
    }

    override fun bind(element: TalkReadingEmptySpace?) {
        // No op
    }
}