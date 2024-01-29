package com.tokopedia.recommendation_widget_common.widget.foryou.recom

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouRecomGridBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.HomeRecommendationListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationGridViewHolder constructor(
    view: View,
    private val listener: HomeRecommendationListener
) : BaseForYouViewHolder<HomeRecommendationModel>(
    view,
    HomeRecommendationModel::class.java
) {

    private val binding: WidgetForYouRecomGridBinding? by viewBinding()

    override fun bind(element: HomeRecommendationModel) {
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
    }

    private fun setLayout(element: HomeRecommendationModel) {
        binding?.productCardView?.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: HomeRecommendationModel) {
        binding?.productCardView?.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductImpression(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: HomeRecommendationModel) {
        binding?.productCardView?.setOnClickListener {
            listener.onProductClick(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(productCardItem: HomeRecommendationModel) {
        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductThreeDotsClick(
                productCardItem,
                bindingAdapterPosition
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_recom_grid
    }
}
