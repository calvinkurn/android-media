package com.tokopedia.talk.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import kotlinx.android.synthetic.main.item_talk_reading_shimmer.view.*

class TalkReadingShimmerViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_talk_reading_shimmer
    }

    override fun bind(element: LoadingMoreModel) {
        itemView.talkReadingShimmerContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }
}