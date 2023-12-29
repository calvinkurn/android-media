package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationLoaderItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationLoaderViewHolder(
    itemView: View
) : AbstractViewHolder<LoadingMoreModel>(itemView) {

    private val binding: UniversalInboxRecommendationLoaderItemBinding? by viewBinding()

    override fun bind(element: LoadingMoreModel) {
        // No-op
    }

    override fun onViewRecycled() {
        binding?.inboxLoaderRecommendation?.avd?.stop()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_loader_item
    }
}
