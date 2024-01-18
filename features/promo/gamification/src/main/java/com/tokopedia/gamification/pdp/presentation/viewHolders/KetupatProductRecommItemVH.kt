package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatBenefitCouponVHModel
import com.tokopedia.gamification.pdp.presentation.viewHolders.viewModel.KetupatProductRecommItemVHmodel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetView
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.gamification.R as gamificationR

class KetupatProductRecommItemVH(itemView: View) : AbstractViewHolder<KetupatProductRecommItemVHmodel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = gamificationR.layout.ketupat_product_recomm_item
    }

    override fun bind(element: KetupatProductRecommItemVHmodel) {
        itemView.findViewById<RecommendationWidgetView>(gamificationR.id.ketupat_recommendation_widget).bind(element.recommendationWidgetModel)
    }
}
