package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.RecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.adapter.model.TopAdsRequestParams
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent.GyroRecommendationItemViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent.RecommendationItemViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent.TopAdsItemViewHolder
import com.tokopedia.thankyou_native.presentation.views.RegisterMemberShipListener

class BottomContentFactory(
    private val registerMemberShipListener: RegisterMemberShipListener
): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            TopAdsItemViewHolder.LAYOUT_ID -> TopAdsItemViewHolder(parent)
            GyroRecommendationItemViewHolder.LAYOUT_ID -> GyroRecommendationItemViewHolder(parent, registerMemberShipListener)
            RecommendationItemViewHolder.LAYOUT_ID -> RecommendationItemViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(topAdsRequestParams: TopAdsRequestParams): Int {
        return TopAdsItemViewHolder.LAYOUT_ID
    }

    fun type(gyroRecommendationWidgetModel: GyroRecommendationWidgetModel): Int {
        return GyroRecommendationItemViewHolder.LAYOUT_ID
    }

    fun type(recommendationWidgetModel: RecommendationWidgetModel): Int {
        return RecommendationItemViewHolder.LAYOUT_ID
    }
}
