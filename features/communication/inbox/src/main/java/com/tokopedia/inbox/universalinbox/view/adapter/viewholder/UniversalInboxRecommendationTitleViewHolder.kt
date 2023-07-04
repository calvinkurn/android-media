package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationTitleItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationTitleViewHolder(itemView: View): BaseViewHolder(itemView) {

    private val binding: UniversalInboxRecommendationTitleItemBinding? by viewBinding()

    fun bind(uiModel: UniversalInboxRecommendationTitleUiModel) {
        binding?.inboxTvRecommendationTitle?.text = uiModel.text
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_title_item
    }
}
