package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import kotlinx.android.synthetic.main.item_hierarchycal_social_proof.view.*
import kotlinx.android.synthetic.main.item_social_proof_with_divider.view.*

/**
 * Created by Yehezkiel on 18/05/20
 */
class HierarchycalSocialProofViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    override fun bind(element: ProductSocialProofDataModel) {
        if (!element.shouldRenderSocialProof) {
            view.pdp_shimmering_social_proof?.show()
        } else {
            view.pdp_shimmering_social_proof?.hide()

            val rootView = view.findViewById<ViewGroup>(R.id.root_socproof)
            rootView.removeAllViews()


            val inflater: LayoutInflater = view.context.layoutInflater

            (0..2).forEachIndexed { index, i ->
                val socProofView: View = inflater.inflate(R.layout.item_social_proof_with_divider, null)
                if (index == 0) {
                    socProofView.social_proof_horizontal_separator?.hide()
                } else {
                    socProofView.social_proof_horizontal_separator?.show()
                }
//                socProofView.txt_soc_proof?.text = generateTextSocialProof(element)
                rootView.addView(socProofView, i)
            }
        }
    }

    private fun getComponentTrackData(element: ProductSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
//
//    private fun generateTextSocialProof(element: ProductSocialProofDataModel): String = with(view) {
//        return when {
//            element.ratingCount > 0 -> {
//                view.context.getString(R.string.template_success_rate, element.rating.toString(), element.ratingCount.toString())
//            }
//            element.talkCount > 0 -> {
//                view.context.getString(R.string.qna_template_builder, element.talkCount.toString())
//            }
//            element.paymentVerifiedCount > 0 -> {
//                view.context.getString(R.string.terjual_template_builder, element.paymentVerifiedCount.toString())
//            }
//            element.wishlistCount > 0 -> {
//                view.context.getString(R.string.wishlist_template_builder, element.wishlistCount.toString())
//            }
//            element.viewCount > 0 -> {
//                view.context.getString(R.string.product_view_template_builder, element.viewCount.toString())
//            }
//            else -> {
//                ""
//            }
//        }
//    }
}