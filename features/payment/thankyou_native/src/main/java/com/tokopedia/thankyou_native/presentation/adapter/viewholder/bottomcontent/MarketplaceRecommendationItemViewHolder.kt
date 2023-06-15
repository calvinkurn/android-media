package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankLayoutMarketPlaceRecomBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.MarketplaceRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.views.listener.MarketplaceRecommendationListener
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class MarketplaceRecommendationItemViewHolder(
    view: View?,
    private val listener: MarketplaceRecommendationListener
) : AbstractViewHolder<MarketplaceRecommendationWidgetModel>(view) {

    private var binding: ThankLayoutMarketPlaceRecomBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thank_layout_market_place_recom
    }

    override fun bind(data: MarketplaceRecommendationWidgetModel) {
        try {
            if (data.thanksPageData.configFlagData?.shouldHideProductRecom == true || data.fragment.isDetached) return

            listener.iRecommendationView = binding?.marketPlaceRecommendationView

            binding?.marketPlaceRecommendationView?.loadRecommendation(
                data.thanksPageData,
                data.fragment
            )
        } catch (e: Exception) {
            Timber.d(e)
        }
    }
}
