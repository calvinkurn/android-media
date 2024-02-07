package com.tokopedia.recommendation_widget_common.widget.foryou.recom

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouRecomListBinding
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.ParentRecommendationListener
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationCardListViewHolder constructor(
    view: View,
    private val listener: ParentRecommendationListener
) : BaseForYouViewHolder<RecommendationCardModel>(
    view,
    RecommendationCardModel::class.java
) {

    private val binding: WidgetForYouRecomListBinding? by viewBinding()

    override fun bind(element: RecommendationCardModel) {
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
    }

    override fun bindPayload(newItem: RecommendationCardModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it)
        }
    }

    private fun setLayout(
        element: RecommendationCardModel
    ) {
        binding?.productCardView?.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: RecommendationCardModel) {
        binding?.productCardView?.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductCardImpressed(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: RecommendationCardModel) {
        binding?.productCardView?.setOnClickListener {
            listener.onProductCardClicked(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(element: RecommendationCardModel) {
        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductCardThreeDotsClicked(
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
