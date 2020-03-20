package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofPvDataModel
import com.tokopedia.product.detail.view.fragment.partialview.PartialAttributeInfoPvView
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductStatisticView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.view.*
import kotlinx.android.synthetic.main.partial_product_social_proof_pv_2.view.*

/**
 * Created by Yehezkiel on 2020-03-18
 */

class ProductSocialProofPvViewHolder(val view: View, private val listener: DynamicProductDetailListener)
    : AbstractViewHolder<ProductSocialProofPvDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_pdp_social_proof_pv
    }

    private var productStatsView: PartialProductStatisticView? = null
    private var attributeInfoView: PartialAttributeInfoPvView? = null

    override fun bind(element: ProductSocialProofPvDataModel) {
        val stats = element.stats ?: Stats()
        val txStats = element.txStats ?: TxStatsDynamicPdp()
        if (productStatsView == null) {
            productStatsView = PartialProductStatisticView.build(view.base_rating_talk_courier)
        }

        if (attributeInfoView == null) {
            attributeInfoView = PartialAttributeInfoPvView.build(view.base_attribute_pv)
        }

        view.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }

        element.rating?.run {
            productStatsView?.renderRatingNew(this.toString())
        }
        attributeInfoView?.renderWishlistCount(element.wishListCount)

        productStatsView?.renderData(stats.countReview, stats.countTalk, listener::onReviewClick, listener::onDiscussionClicked, getComponentTrackData(element))
        attributeInfoView?.renderDataDynamicPdp(stats.countView, txStats)

        productStatsView?.renderClickShipping {
            listener.onShipmentSocialProofClicked(getComponentTrackData(element))
        }
    }

    private fun getComponentTrackData(element: ProductSocialProofPvDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

}