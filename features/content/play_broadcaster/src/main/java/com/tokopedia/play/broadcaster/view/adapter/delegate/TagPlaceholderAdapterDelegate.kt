package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.viewholder.TagPlaceholderViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on March 25, 2022
 */
class TagPlaceholderAdapterDelegate : TypedAdapterDelegate<TagRecommendationListAdapter.Model.Placeholder, TagRecommendationListAdapter.Model, TagPlaceholderViewHolder>(commonR.layout.view_play_empty) {
    override fun onBindViewHolder(
        item: TagRecommendationListAdapter.Model.Placeholder,
        holder: TagPlaceholderViewHolder
    ) { }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagPlaceholderViewHolder {
        return TagPlaceholderViewHolder.create(parent)
    }
}