package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofChipViewHolder(
        private val view: View,
        private val listener: DynamicProductDetailListener
) : ProductMiniSocialProofTypeBaseViewHolder(view) {

    override fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {

        if(socialProof.type != ProductMiniSocialProofItemType.ProductMiniSocialProofChip) return

        val firstSocialProofTxt = view.findViewById<Typography>(R.id.chip_social_proof_title)
        val firstSocialProofValue = view.findViewById<Typography>(R.id.chip_social_proof_value)
        when (socialProof.key) {
            ProductMiniSocialProofDataModel.RATING -> {
                view.isClickable = true
                view.setOnClickListener { listener.onReviewClick() }
                firstSocialProofTxt?.run {
                    text = socialProof.reviewTitle
                    setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.ic_review_one_small), null, null, null)
                }
                firstSocialProofValue?.run {
                    text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
                }
            }
            ProductMiniSocialProofDataModel.TALK -> {
                view.isClickable = true
                view.setOnClickListener { listener.onDiscussionClicked(componentTrackDataModel) }
                firstSocialProofTxt?.run {
                    text = view.context.getString(R.string.product_detail_discussion_label)
                }
                firstSocialProofValue?.run {
                    text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
                }
            }
            else -> {
                view.isClickable = true
                view.setOnClickListener { listener.onBuyerPhotosClicked(componentTrackDataModel) }
                firstSocialProofTxt?.run {
                    text = view.context.getString(R.string.label_buyer_photos)
                }
                firstSocialProofValue?.run {
                    text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
                }
            }
        }
    }

}