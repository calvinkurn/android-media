package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.databinding.ItemTagRecommendationBinding
import com.tokopedia.play.broadcaster.ui.model.tag.PlayTagUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationAdapterDelegate(
        private val listener: TagRecommendationViewHolder.Listener
) : TypedAdapterDelegate<PlayTagUiModel, PlayTagUiModel, TagRecommendationViewHolder>(TagRecommendationViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayTagUiModel, holder: TagRecommendationViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagRecommendationViewHolder {
        return TagRecommendationViewHolder(ItemTagRecommendationBinding.bind(basicView), listener)
    }
}