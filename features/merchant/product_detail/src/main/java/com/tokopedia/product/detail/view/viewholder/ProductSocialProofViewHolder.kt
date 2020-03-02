package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialAttributeInfoView
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductStatisticView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_detail_visibility.view.*
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.view.*

class ProductSocialProofViewHolder(val view: View, private val listener: DynamicProductDetailListener)
    : AbstractViewHolder<ProductSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_social_proof
    }

    private lateinit var productStatsView: PartialProductStatisticView
    private lateinit var attributeInfoView: PartialAttributeInfoView

    override fun bind(element: ProductSocialProofDataModel) {
        val stats = element.stats ?: Stats()
        val txStats = element.txStats ?: TxStatsDynamicPdp()
        if (!::productStatsView.isInitialized) {
            productStatsView = PartialProductStatisticView.build(view.base_rating_talk_courier)
        }

        if (!::attributeInfoView.isInitialized) {
            attributeInfoView = PartialAttributeInfoView.build(view.base_attribute)
        }

        element.rating?.run {
            productStatsView.renderRatingNew(this.toString())
        }
        attributeInfoView.renderWishlistCount(element.wishListCount)

        productStatsView.renderData(stats.countReview, stats.countTalk, listener::onReviewClick, listener::onDiscussionClicked)
        attributeInfoView.renderDataDynamicPdp(stats.countView, txStats)


        productStatsView.renderClickShipping {
            listener.onShipmentClicked()
        }
    }

}