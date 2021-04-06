package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel

/**
 * Created By @ilhamsuaib on 05/04/21
 */

class RecommendationViewHolder(itemView: View?) : AbstractViewHolder<RecommendationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_recommendation_widget
    }

    override fun bind(element: RecommendationWidgetUiModel) {

    }
}