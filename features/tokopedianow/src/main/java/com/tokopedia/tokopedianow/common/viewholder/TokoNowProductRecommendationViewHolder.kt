package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowProductRecommendationView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeProductRecomBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductRecommendationViewHolder(
    itemView: View,
    private val listener: TokoNowProductRecommendationView.TokoNowProductRecommendationListener? = null
) : AbstractViewHolder<TokoNowProductRecommendationUiModel>(itemView) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private var binding: ItemTokopedianowHomeProductRecomBinding? by viewBinding()

    override fun bind(element: TokoNowProductRecommendationUiModel) {
        binding?.productRecom?.setRequstParam(
            getRecommendationRequestParam = element.requestParam
        )
        binding?.productRecom?.setListener(
            productRecommendationListener = listener
        )
    }

}
