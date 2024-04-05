package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountRecommendationItemProductCardBinding
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class ProductItemViewHolder(itemView: View, val listener: HomeAccountUserListener) :
    BaseViewHolder(itemView), IAdsViewHolderTrackListener {

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
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.ACCOUNT,
                AdsLogRealtimeClickModel(
                    refer,
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogRealtimeClickModel.AdExtraData(
                        productId = recommendationItem.productId.orZero().toString()
                    )
                )
            )
        }
    }

    override fun onViewAttachedToWindow(recyclerView: RecyclerView?) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                PageName.ACCOUNT,
                AdsLogShowModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowModel.AdExtraData(
                        productId = recommendationItem?.productId.orZero().toString(),
                    )
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(recyclerView: RecyclerView?) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShowOver(
                itemView.context,
                PageName.ACCOUNT,
                AdsLogShowOverModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowOverModel.AdExtraData(
                        productId = recommendationItem?.productId.orZero().toString(),
                        sizePercent = visiblePercentage.toString()
                    )
                )
            )
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
