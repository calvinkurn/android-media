package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.data.model.UohRecommendationTitle
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohRecommendationTitleBinding

/**
 * Created by fwidjaja on 25/07/20.
 */
class UohRecommendationTitleViewHolder(private val binding: UohRecommendationTitleBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UohTypeData) {
        if (item.dataObject is UohRecommendationTitle) {
            binding.uohRecommendationTitle.text = item.dataObject.title
        }
    }
}
