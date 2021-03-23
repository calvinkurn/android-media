package com.tokopedia.play.broadcaster.ui.viewholder

import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemTagRecommendationRowBinding
import com.tokopedia.play.broadcaster.ui.itemdecoration.TagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.tag.TagRecommendationRowUiModel
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter

/**
 * Created by jegul on 23/03/21
 */
class TagRecommendationRowViewHolder(
        private val binding: ItemTagRecommendationRowBinding,
        listener: TagRecommendationViewHolder.Listener
) : BaseViewHolder(binding.root) {

    private val adapter = TagRecommendationListAdapter(listener)

    init {
        binding.rvTagRow.adapter = adapter
        binding.rvTagRow.addItemDecoration(TagItemDecoration(binding.rvTagRow.context))
    }

    fun bind(item: TagRecommendationRowUiModel) {
        adapter.setItemsAndAnimateChanges(item.tagList)
    }

    companion object {

        val LAYOUT = R.layout.item_tag_recommendation_row
    }
}