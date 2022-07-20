package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.search.R
import com.tokopedia.search.analytics.RecommendationTracking
import com.tokopedia.search.databinding.RecommendationTitleLayoutBinding
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationTitleViewHolder(
        itemView: View
) :  AbstractViewHolder<RecommendationTitleDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.recommendation_title_layout
    }
    private var binding: RecommendationTitleLayoutBinding? by viewBinding()

    override fun bind(element: RecommendationTitleDataView?) {
        val binding= binding?: return
        element?.let { viewModel ->
            binding.title.text = viewModel.title
            binding.seeMore.visibility = if(viewModel.seeMoreUrl.isNotEmpty()) View.VISIBLE else View.GONE
            binding.seeMore.setOnClickListener {
                RecommendationTracking.eventUserClickSeeMore(viewModel.pageName)
                RouteManager.route(itemView.context, viewModel.seeMoreUrl)
            }
        }
    }


}