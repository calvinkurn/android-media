package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemType
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofStockDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofChipViewHolder(
        private val view: View,
        private val listener: DynamicProductDetailListener
) : ProductMiniSocialProofTypeBaseViewHolder(view) {

    private val firstSocialProofTxt: Typography? = view.findViewById(R.id.chip_social_proof_title)
    private val firstSocialProofValue: Typography? = view.findViewById(R.id.chip_social_proof_value)

    override fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {

        if (socialProof.type != ProductMiniSocialProofItemType.ProductMiniSocialProofChip) return

        when (socialProof.key) {
            ProductMiniSocialProofStockDataModel.RATING -> {
                renderChipRating(socialProof)
            }
            ProductMiniSocialProofStockDataModel.TALK -> {
                renderChipTalk(socialProof, componentTrackDataModel)
            }
            else -> {
                renderChipBuyerPhotos(socialProof, componentTrackDataModel)
            }
        }
    }

    private fun renderChipBuyerPhotos(socialProof: ProductMiniSocialProofItemDataModel,
                                      componentTrackDataModel: ComponentTrackDataModel?) = with(view) {
        isClickable = true
        setOnClickListener { listener.onBuyerPhotosClicked(componentTrackDataModel) }
        firstSocialProofTxt?.run {
            text = socialProof.buyerPhotosTitle
        }
        firstSocialProofValue?.run {
            text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
        }
    }

    private fun renderChipTalk(socialProof: ProductMiniSocialProofItemDataModel,
                               componentTrackDataModel: ComponentTrackDataModel?) = with(view) {
        isClickable = true
        setOnClickListener { listener.onDiscussionClicked(componentTrackDataModel) }
        firstSocialProofTxt?.run {
            text = view.context.getString(R.string.product_detail_discussion_label)
        }
        firstSocialProofValue?.run {
            text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
        }
    }


    private fun renderChipRating(socialProof: ProductMiniSocialProofItemDataModel) = with(view) {
        isClickable = true
        setOnClickListener { listener.onReviewClick() }
        firstSocialProofTxt?.run {
            text = socialProof.reviewTitle
            setCompoundDrawablesWithIntrinsicBounds(
                    MethodChecker.getDrawable(view.context, R.drawable.ic_review_one_small),
                    null,
                    null,
                    null)
        }
        firstSocialProofValue?.run {
            text = view.context.getString(R.string.bracket_formated, socialProof.formattedCount)
        }
    }

}
