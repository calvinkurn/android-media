package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationWidgetItemBinding
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationWidgetViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalInboxRecommendationWidgetUiModel>(itemView) {

    private var binding: UniversalInboxRecommendationWidgetItemBinding? by viewBinding()

    override fun bind(uiModel: UniversalInboxRecommendationWidgetUiModel) {
        binding?.inboxRecommendationWidget?.bind(uiModel.recommendationWidgetModel)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_widget_item
    }
}
