package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountRecommendationItemProductCardBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView),
    IAdsViewHolderTrackListener {

    private val binding: HomeAccountRecommendationItemProductCardBinding? by viewBinding()

    private var visibleViewPercentage: Int = 0
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
                sendClickAdsByteIO(element, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                sendClickAdsByteIO(element, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                sendClickAdsByteIO(element, AdsLogConst.Refer.SELLER_NAME)
            }
        })

        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductRecommendationThreeDotsClicked(element, adapterPosition)
        }
    }

    private fun sendClickAdsByteIO(recommendationItem: RecommendationItem?, refer: String) {
        recommendationItem?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.ACCOUNT,
                    it.asAdsLogRealtimeClickModel(refer)
                )
            }
        }
    }

    override fun onViewAttachedToWindow() {
        recommendationItem?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventShow(
                    itemView.context,
                    PageName.ACCOUNT,
                    it.asAdsLogShowModel()
                )
            }
        }
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        recommendationItem?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventShowOver(
                    itemView.context,
                    PageName.ACCOUNT,
                    it.asAdsLogShowOverModel(visiblePercentage)
                )
            }
        }
    }

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.visibleViewPercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = visibleViewPercentage

    companion object {
        val LAYOUT = R.layout.home_account_recommendation_item_product_card
    }
}
