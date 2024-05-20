package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultRecommendationCardSmallGridBinding
import com.tokopedia.search.result.presentation.model.RecommendationItemDataView
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationItemViewHolder (
        itemView: View,
        private val listener: RecommendationListener
) : AbstractViewHolder<RecommendationItemDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_recommendation_card_small_grid
    }

    private var binding: SearchResultRecommendationCardSmallGridBinding? by viewBinding()

    override fun bind(recommendationItemDataView: RecommendationItemDataView) {
        val view = binding?.root ?: return
        val recommendationItem = recommendationItemDataView.recommendationItem
        val productModel = recommendationItem.toProductCardModel(
            hasThreeDots = true,
            cardInteraction = true,
        )
        view.setProductModel(productModel)

        view.setVisibilityPercentListener(recommendationItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
            override fun onShow() {
                recommendationItem.sendShowAdsByteIo(itemView.context)
            }

            override fun onShowOver(maxPercentage: Int) {
                recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
            }
        })
        view.setOnClickListener(object: ProductCardClickListener {
            override fun onClick(v: View) {
                listener.onProductClick(recommendationItem, "", adapterPosition)
            }

            override fun onAreaClicked(v: View) {
                recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
            }
        })

        view.setImageProductViewHintListener(recommendationItemDataView, createImageProductViewHintListener(recommendationItemDataView))

        view.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemDataView.recommendationItem, adapterPosition)
        }
    }

    private fun createImageProductViewHintListener(recommendationItemDataView: RecommendationItemDataView): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                listener.onProductImpression(recommendationItemDataView.recommendationItem)
            }
        }
    }

    override fun bind(recommendationItemDataView: RecommendationItemDataView, payloads: MutableList<Any>) {
        payloads.getOrNull(0) ?: return

        binding?.root?.setThreeDotsOnClickListener {
            listener.onThreeDotsClick(recommendationItemDataView.recommendationItem, adapterPosition)
        }
    }

    override fun onViewRecycled() {
        binding?.productCardView?.recycle()
    }
}
