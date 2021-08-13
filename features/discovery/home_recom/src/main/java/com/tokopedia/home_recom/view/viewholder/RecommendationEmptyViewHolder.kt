package com.tokopedia.home_recom.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.datamodel.RecommendationEmptyDataModel
import kotlinx.android.synthetic.main.item_recommendation_empty.view.*

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationEmptyViewHolder(view: View, private val listener: RecommendationEmptyStateListener) : AbstractViewHolder<RecommendationEmptyDataModel>(view){

    override fun bind(element: RecommendationEmptyDataModel) {
        itemView.empty_state?.run {
            ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_empty_search_wishlist)?.let { setImageDrawable(it) }
            setPrimaryCTAClickListener {
                listener.onResetFilterClick()
            }
        }
    }

    interface RecommendationEmptyStateListener{
        fun onResetFilterClick()
    }
}