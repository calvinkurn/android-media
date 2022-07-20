package com.tokopedia.home_recom.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.databinding.ItemRecommendationEmptyBinding
import com.tokopedia.home_recom.model.datamodel.RecommendationEmptyDataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationEmptyViewHolder(view: View, private val listener: RecommendationEmptyStateListener) : AbstractViewHolder<RecommendationEmptyDataModel>(view){

    private var binding: ItemRecommendationEmptyBinding? by viewBinding()
    override fun bind(element: RecommendationEmptyDataModel) {
        binding?.emptyState?.run {
            when (element.type) {
                RecommendationEmptyDataModel.TYPE_PAGE_INFINITE_RECOM -> {
                    ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_empty_search_wishlist)?.let { setImageDrawable(it) }
                    setPrimaryCTAText("")
                    setSecondaryCTAText("")
                }
                RecommendationEmptyDataModel.TYPE_DEFAULT -> {
                    ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_empty_search_wishlist)?.let { setImageDrawable(it) }
                    setPrimaryCTAClickListener {
                        listener.onResetFilterClick()
                    }
                }
            }
        }
    }

    interface RecommendationEmptyStateListener{
        fun onResetFilterClick()
    }
}