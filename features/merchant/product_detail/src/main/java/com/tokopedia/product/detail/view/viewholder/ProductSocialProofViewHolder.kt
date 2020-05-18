package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Stats
import com.tokopedia.product.detail.common.data.model.product.TxStatsDynamicPdp
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel.Companion.PAYMENT_VERIFIED
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel.Companion.RATING
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel.Companion.TALK
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel.Companion.VIEW_COUNT
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel.Companion.WISHLIST
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.fragment.partialview.PartialAttributeInfoView
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductStatisticView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import kotlinx.android.synthetic.main.item_hierarchycal_social_proof.view.*
import kotlinx.android.synthetic.main.item_social_proof_with_divider.view.*
import java.lang.reflect.Method


class ProductSocialProofViewHolder(val view: View, private val listener: DynamicProductDetailListener)
    : AbstractViewHolder<ProductSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    private var productStatsView: PartialProductStatisticView? = null
    private var attributeInfoView: PartialAttributeInfoView? = null

    override fun bind(element: ProductSocialProofDataModel) {
        val stats = element.stats ?: Stats()
        val txStats = element.txStats ?: TxStatsDynamicPdp()
        val availableData = element.getAvailableData()
        if (!element.shouldRenderSocialProof) {
            view.pdp_shimmering_social_proof?.show()
        } else {
            view.pdp_shimmering_social_proof?.hide()

            val rootView = view.findViewById<ViewGroup>(R.id.root_socproof)
            rootView.removeAllViews()

            val inflater: LayoutInflater = view.context.layoutInflater
            val key = availableData.first().first

            if (availableData.size == 1 && (key == TALK || key == PAYMENT_VERIFIED || key == VIEW_COUNT)) {
                val socProofView: View = inflater.inflate(R.layout.item_social_proof_with_divider, null)
                generateSingleTextSocialProof(availableData.first(), socProofView, element)
                rootView.addView(socProofView, 0)
            } else {
                availableData.forEachIndexed { index, i ->
                    val socProofView: View = inflater.inflate(R.layout.item_social_proof_with_divider, null)
                    renderSocialProofData(i, element.rating ?: 0F, element, socProofView, index)
                    rootView.addView(socProofView, index)
                }
            }
        }


//        if (productStatsView == null) {
//            productStatsView = PartialProductStatisticView.build(view.base_rating_talk_courier)
//        }
//
//        if (attributeInfoView == null) {
//            attributeInfoView = PartialAttributeInfoView.build(view.base_attribute)
//        }
//
//        view.addOnImpressionListener(element.impressHolder) {
//            listener.onImpressComponent(getComponentTrackData(element))
//        }
//
//        element.rating?.run {
//            productStatsView?.renderRatingNew(this.toString())
//        }
//        attributeInfoView?.renderWishlistCount(element.wishListCount)
//
//        productStatsView?.renderData(stats.countReview, stats.countTalk, listener::onReviewClick, listener::onDiscussionClicked, getComponentTrackData(element))
//        attributeInfoView?.renderDataDynamicPdp(stats.countView, txStats, element.isSocialProofPv)
//
//        productStatsView?.renderClickShipping {
//            listener.onShipmentSocialProofClicked(getComponentTrackData(element))
//        }
    }

    private fun generateSingleTextSocialProof(element: Pair<String, Int>, view: View, data: ProductSocialProofDataModel) {
        val textSocialProof = view.txt_soc_proof
        val socProofDivider = view.social_proof_horizontal_separator
        socProofDivider?.hide()

        when (element.first) {
            TALK -> {
                view.setOnClickListener { listener.onReviewClick() }
                textSocialProof?.text = view.context.getString(R.string.qna_single_text_template_builder, element.second.productThousandFormatted())
            }
            PAYMENT_VERIFIED -> {
                textSocialProof?.text = view.context.getString(R.string.terjual_single_text_template_builder, element.second.productThousandFormatted())
            }
            VIEW_COUNT -> {
                textSocialProof?.text = view.context.getString(R.string.product_view_single_text__template_builder, element.second.productThousandFormatted())
            }
        }
    }

    private fun renderSocialProofData(element: Pair<String, Int>, rating: Float, data: ProductSocialProofDataModel, view: View, index: Int) {
        val textSocialProofView = view.txt_soc_proof
        val socProofDivider = view.social_proof_horizontal_separator
        var textSocialProof = ""
        socProofDivider.showWithCondition(index != 0)

        when (element.first) {
            RATING -> {
                view.setOnClickListener { listener.onReviewClick() }
                textSocialProofView?.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.ic_rating_gold), null, null, null)
                textSocialProof = view.context.getString(R.string.rating_template_builder, rating.toString(), element.second.productThousandFormatted())
            }
            TALK -> {
                view.setOnClickListener { listener.onDiscussionClicked(getComponentTrackData(data)) }
                textSocialProof = view.context.getString(R.string.qna_template_builder, element.second.productThousandFormatted())
            }
            PAYMENT_VERIFIED -> {
                view.setOnClickListener { }
                textSocialProof = view.context.getString(R.string.terjual_template_builder, element.second.productThousandFormatted())
            }
            WISHLIST -> {
                view.setOnClickListener { }
                textSocialProof = view.context.getString(R.string.wishlist_template_builder, element.second.productThousandFormatted())
            }
            VIEW_COUNT -> {
                view.setOnClickListener { }
                textSocialProof = view.context.getString(R.string.product_view_template_builder, element.second.productThousandFormatted())
            }
        }

        textSocialProofView?.text = MethodChecker.fromHtml(textSocialProof)
    }

    private fun getComponentTrackData(element: ProductSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)

}