package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.viewholder.TagRecommendationViewHolder

/**
 * Created by jegul on 18/02/21
 */
class TagRecommendationAdapterDelegate : TypedAdapterDelegate<String, String, TagRecommendationViewHolder>(TagRecommendationViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: String, holder: TagRecommendationViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): TagRecommendationViewHolder {
        return TagRecommendationViewHolder(basicView)
    }
}