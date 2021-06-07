package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofTextViewHolder(
        private val view: View,
) : ProductMiniSocialProofTypeBaseViewHolder(view) {

    override fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {
        when (socialProof.type) {
            ProductMiniSocialProofItemType.ProductMiniSocialProofSingleText -> {
                val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
                firstSocialProofTxt.apply {
                    text = generateSingleView(socialProof)
                    setPadding(8.toPx(), 0, 16.toPx(), 0)
                }
            }
            ProductMiniSocialProofItemType.ProductMiniSocialProofText -> {
                val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
                firstSocialProofTxt.apply {
                    text = generateFirstSocialProofText(socialProof)
                    setPadding(8.toPx(), 7.toPx(), 8.toPx(), 7.toPx())
                }
            }
            else -> return
        }
    }

    private fun generateSingleView(socialProof: ProductMiniSocialProofItemDataModel): String {
        return when (socialProof.key) {
            ProductMiniSocialProofDataModel.PAYMENT_VERIFIED -> {
                view.context.getString(R.string.terjual_single_text_template_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofDataModel.WISHLIST -> {
                view.context.getString(R.string.wishlist_single_text_template_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofDataModel.VIEW_COUNT -> {
                view.context.getString(R.string.view_single_text__template_builder, socialProof.formattedCount)
            }
            else -> {
                ""
            }
        }
    }

    private fun generateFirstSocialProofText(socialProof: ProductMiniSocialProofItemDataModel): String {
        return when (socialProof.key) {
            ProductMiniSocialProofDataModel.PAYMENT_VERIFIED -> {
                view.context.getString(R.string.label_terjual_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofDataModel.WISHLIST -> {
                view.context.getString(R.string.label_wishlist_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofDataModel.VIEW_COUNT -> {
                view.context.getString(R.string.label_view_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofStockDataModel.STOCK -> {
                view.context.getString(R.string.label_stock_builder, socialProof.formattedCount)
            }
            else -> {
                ""
            }
        }
    }

}