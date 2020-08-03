package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
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

    private var productStatsView: PartialProductStatisticView? = null
    private var attributeInfoView: PartialAttributeInfoView? = null

    override fun bind(element: ProductSocialProofDataModel) {
        val stats = element.stats ?: Stats()
        val txStats = element.txStats ?: TxStatsDynamicPdp()

        if (productStatsView == null) {
            productStatsView = PartialProductStatisticView.build(view.base_rating_talk_courier)
        }

        if (attributeInfoView == null) {
            attributeInfoView = PartialAttributeInfoView.build(view.base_attribute)
        }

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        element.stats?.rating?.run {
            productStatsView?.renderRatingNew(this.toString())
        }
        attributeInfoView?.renderWishlistCount(element.wishlistCount)

        productStatsView?.renderData(stats.countReview, stats.countTalk, listener::onReviewClick, listener::onDiscussionClicked, getComponentTrackData(element))
        attributeInfoView?.renderDataDynamicPdp(element.viewCount, txStats, element.isSocialProofPv)

        productStatsView?.renderClickShipping {
            listener.onShipmentSocialProofClicked(getComponentTrackData(element))
        }
    }


    private fun getComponentTrackData(element: ProductSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

}