package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofItemType
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofChipViewHolder(
        private val view: View,
        private val listener: DynamicProductDetailListener
) : RecyclerView.ViewHolder(view) {

    fun bind(socialProof: ProductMiniSocialProofItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {
        with(socialProof) {
            when (type) {
                ProductMiniSocialProofItemType.ProductMiniSocialProofChip -> {
                    val firstSocialProofTxt = view.findViewById<Typography>(R.id.chip_social_proof_title)
                    val firstSocialProofValue = view.findViewById<Typography>(R.id.chip_social_proof_value)
                    when (socialProof.key) {
                        ProductMiniSocialProofDataModel.RATING -> {
                            view.isClickable = true
                            view.setOnClickListener { listener.onReviewClick() }
                            firstSocialProofTxt?.run {
                                text = reviewTitle
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
                ProductMiniSocialProofItemType.ProductMiniSocialProofText -> {
                    val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
                    firstSocialProofTxt.apply {
                        text = generateFirstSocialProofText(socialProof)
                        setPadding(16.toPx(),7.toPx(),8.toPx(),7.toPx())
                    }
                }
                ProductMiniSocialProofItemType.ProductMiniSocialProofSingleText -> {
                    val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
                    firstSocialProofTxt.apply {
                        text = generateSingleView(socialProof)
                        setPadding(16.toPx(),0,16.toPx(),0)
                    }
                }
            }
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
            else -> {
                ""
            }
        }
    }

}