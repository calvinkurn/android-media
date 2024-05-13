package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountRecommendationItemProductCardBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener) : BaseViewHolder(itemView) {
    private val binding: HomeAccountRecommendationItemProductCardBinding? by viewBinding()

    private var recommendationItem: RecommendationItem? = null

    fun bind(element: RecommendationItem) {
        recommendationItem = element
        binding?.productCardView?.setProductModel(element.toProductCardModel(hasThreeDots = true))
        binding?.productCardView?.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductRecommendationImpression(element, adapterPosition)
                }
            })

        binding?.productCardView?.setOnClickListener(object : ProductCardClickListener {
            override fun onClick(v: View) {
                listener.onProductRecommendationClicked(element, adapterPosition)
            }

            override fun onAreaClicked(v: View) {
                element.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                element.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                element.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
            }
        })

        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductRecommendationThreeDotsClicked(element, adapterPosition)
        }

        binding?.productCardView?.setVisibilityPercentListener(
            isTopAds = element.isTopAds,
            eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.sendShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            }
        )
    }

    companion object {
        val LAYOUT = R.layout.home_account_recommendation_item_product_card
    }
}
