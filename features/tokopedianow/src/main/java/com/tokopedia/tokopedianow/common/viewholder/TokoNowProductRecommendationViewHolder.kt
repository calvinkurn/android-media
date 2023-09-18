package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView.TokoNowProductRecommendationListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductRecommendationBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductRecommendationViewHolder(
    itemView: View,
    private val listener: TokoNowProductRecommendationListener? = null
) : AbstractViewHolder<TokoNowProductRecommendationUiModel>(itemView) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_product_recommendation
    }

    private var binding: ItemTokopedianowProductRecommendationBinding? by viewBinding()

    override fun bind(element: TokoNowProductRecommendationUiModel) {
        binding?.apply {
            productRecommendation.setRequestParam(
                getRecommendationRequestParam = element.requestParam,
                tickerPageSource = element.tickerPageSource
            )
            productRecommendation.setListener(
                productRecommendationListener = listener
            )
            realTimeRecommendationCarousel.hide()
        }
    }
}
