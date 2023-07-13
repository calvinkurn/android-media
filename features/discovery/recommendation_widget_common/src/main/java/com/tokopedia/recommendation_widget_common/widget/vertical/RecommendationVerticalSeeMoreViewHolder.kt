package com.tokopedia.recommendation_widget_common.widget.vertical

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomVerticalSeeMoreBinding

class RecommendationVerticalSeeMoreViewHolder(
    itemView: View
): AbstractViewHolder<RecommendationVerticalSeeMoreModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_recom_vertical_see_more
    }

    private val binding = ItemRecomVerticalSeeMoreBinding.bind(itemView)

    init {
        setupSpanSize()
    }

    override fun bind(element: RecommendationVerticalSeeMoreModel) {
        setupListener(element)
    }

    override fun bind(element: RecommendationVerticalSeeMoreModel, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull().takeIf { it is Map<*, *> } as? Map<*, *>
        if (payload.isNullOrEmpty()) {
            bind(element)
        } else {
            if (payload.containsKey(RecommendationVerticalSeeMoreModel.PAYLOAD_SHOULD_RECREATE_LISTENERS)) {
                setupListener(element)
            }
        }
    }

    private fun setupSpanSize() {
        val layoutParams = binding.root.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        layoutParams?.isFullSpan = true
        binding.root.layoutParams = layoutParams
    }

    private fun setupListener(element: RecommendationVerticalSeeMoreModel) {
        binding.root.setOnClickListener { onSeeMoreClicked(element) }
    }

    private fun onSeeMoreClicked(element: RecommendationVerticalSeeMoreModel) {
        RouteManager.route(binding.root.context, element.recomWidget.seeMoreAppLink)
    }
}
