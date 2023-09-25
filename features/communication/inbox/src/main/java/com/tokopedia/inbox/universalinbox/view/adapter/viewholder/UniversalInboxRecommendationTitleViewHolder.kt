package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationTitleItemBinding
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationTitleViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalInboxRecommendationTitleUiModel>(itemView) {

    private val binding: UniversalInboxRecommendationTitleItemBinding? by viewBinding()

    override fun bind(uiModel: UniversalInboxRecommendationTitleUiModel) {
        binding?.inboxTvRecommendationTitle?.text = uiModel.text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_title_item
    }
}
