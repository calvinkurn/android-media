package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ItemTagPlaceholderBinding
import com.tokopedia.play.broadcaster.view.adapter.TagRecommendationListAdapter

/**
 * Created By : Jonathan Darwin on March 25, 2022
 */
class TagPlaceholderViewHolder private constructor(
    binding: ItemTagPlaceholderBinding
) : BaseViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup) : TagPlaceholderViewHolder {
            return TagPlaceholderViewHolder(
                ItemTagPlaceholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }
}