package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationWidgetItemBinding
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationWidgetViewHolder(itemView: View): BaseViewHolder(itemView) {

    private var binding: UniversalInboxRecommendationWidgetItemBinding? by viewBinding()

    fun bind(uiModel: RecommendationWidgetModel) {
        binding?.inboxRecommendationWidget?.bind(uiModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_widget_item
    }
}
