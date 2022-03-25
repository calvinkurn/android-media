package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.databinding.ItemTagBinding
import com.tokopedia.play.broadcaster.ui.viewholder.TagViewHolder
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter
import com.tokopedia.play_common.R as commonR

/**
 * Created by jegul on 18/02/21
 */
class TagAdapterDelegate(
        private val listener: TagViewHolder.Listener
) : TypedAdapterDelegate<TagRecommendationListAdapter.Model.Tag, TagRecommendationListAdapter.Model, TagViewHolder>(commonR.layout.view_play_empty) {

    override fun onBindViewHolder(item: TagRecommendationListAdapter.Model.Tag, holder: TagViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagViewHolder {
        return TagViewHolder.create(parent, listener)
    }
}