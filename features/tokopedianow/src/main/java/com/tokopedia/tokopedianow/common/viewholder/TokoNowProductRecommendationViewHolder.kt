package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductRecommendationBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductRecommendationViewHolder(
    itemView: View,
    private val listener: TokoNowProductRecommendationView.TokoNowProductRecommendationListener? = null
) : AbstractViewHolder<TokoNowProductRecommendationUiModel>(itemView) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_product_recommendation
    }

    private var binding: ItemTokopedianowProductRecommendationBinding? by viewBinding()

    override fun bind(element: TokoNowProductRecommendationUiModel) {
        binding?.productRecommendation?.setRequestParam(
            getRecommendationRequestParam = element.requestParam
        )
        binding?.productRecommendation?.setListener(
            productRecommendationListener = listener
        )
    }

}
