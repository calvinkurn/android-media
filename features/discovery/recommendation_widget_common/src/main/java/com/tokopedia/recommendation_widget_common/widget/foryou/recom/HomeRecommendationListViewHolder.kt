package com.tokopedia.recommendation_widget_common.widget.foryou.recom

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouRecomListBinding
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.ParentRecommendationListener
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationListViewHolder constructor(
    view: View,
    private val listener: ParentRecommendationListener
) : BaseForYouViewHolder<HomeRecommendationModel>(
    view,
    HomeRecommendationModel::class.java
) {

    private val binding: WidgetForYouRecomListBinding? by viewBinding()

    override fun bind(element: HomeRecommendationModel) {
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
    }

    override fun bindPayload(newItem: HomeRecommendationModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationModel
    ) {
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

    private fun setItemThreeDotsClickListener(element: HomeRecommendationModel) {
        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductThreeDotsClick(
                element,
                bindingAdapterPosition
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_recom_list
    }
}
