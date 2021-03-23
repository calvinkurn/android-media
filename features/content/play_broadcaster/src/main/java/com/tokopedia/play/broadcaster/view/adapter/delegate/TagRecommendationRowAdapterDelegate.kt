package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.databinding.ItemTagRecommendationBinding
import com.tokopedia.play.broadcaster.databinding.ItemTagRecommendationRowBinding
import com.tokopedia.play.broadcaster.ui.model.tag.TagRecommendationRowUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationRowViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationRowAdapterDelegate(
        private val listener: TagRecommendationViewHolder.Listener
) : TypedAdapterDelegate<TagRecommendationRowUiModel, TagRecommendationRowUiModel, TagRecommendationRowViewHolder>(TagRecommendationRowViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: TagRecommendationRowUiModel, holder: TagRecommendationRowViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagRecommendationRowViewHolder {
        return TagRecommendationRowViewHolder(ItemTagRecommendationRowBinding.bind(basicView), listener)
    }
}