package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.datamodel.RecommendationEmptyDataModel
import com.tokopedia.kotlin.extensions.view.hide
import kotlinx.android.synthetic.main.item_recommendation_empty.view.*

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Title
 */
class RecommendationEmptyViewHolder(view: View) : AbstractViewHolder<RecommendationEmptyDataModel>(view){

    override fun bind(element: RecommendationEmptyDataModel) {
        itemView.empty_state?.run {
            setPrimaryCTAText("")
        }
        itemView.sort_filter_empty?.run {
            addItem(ArrayList(element.selectedFilter))
            sortFilterHorizontalScrollView.scrollX = 0
            sortFilterPrefix.hide() // hide filter chip
        }
    }

}