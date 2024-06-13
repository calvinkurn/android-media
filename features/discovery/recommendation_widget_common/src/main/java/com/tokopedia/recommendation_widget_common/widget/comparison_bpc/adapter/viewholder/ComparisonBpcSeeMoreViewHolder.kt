package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking.ComparisonBpcAnalyticListener
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by Frenzel
 */
class ComparisonBpcSeeMoreViewHolder(
    view: View,
    private val listener: ComparisonBpcAnalyticListener
) : AbstractViewHolder<ComparisonBpcSeeMoreDataModel>(view) {
    override fun bind(element: ComparisonBpcSeeMoreDataModel) {
        itemView.findViewById<CardUnify2>(R.id.comparison_bpc_see_more_card).animateOnPress = CardUnify2.ANIMATE_OVERLAY
        itemView.setOnClickListener {
            listener.onViewAllCardClicked(element)
        }
    }
    companion object {
        val LAYOUT = R.layout.comparison_bpc_see_more_view_holder
    }
}
