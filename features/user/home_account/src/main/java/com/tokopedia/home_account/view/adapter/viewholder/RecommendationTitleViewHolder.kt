package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.data.model.RecommendationTitleView
import com.tokopedia.home_account.databinding.HomeAccountItemRecommendationTitleBinding
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationTitleViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val binding: HomeAccountItemRecommendationTitleBinding? by viewBinding()

    fun bind(setting: RecommendationTitleView) {
        binding?.homeAccountRecommendationTitleTv?.text = setting.title
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_recommendation_title
    }

}