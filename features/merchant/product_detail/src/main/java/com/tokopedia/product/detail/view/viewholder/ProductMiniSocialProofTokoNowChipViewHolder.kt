package com.tokopedia.product.detail.view.viewholder

import android.text.Html
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ProductMiniSocialProofTokoNowChipViewHolder(
        private val view: View,
        private val listener: DynamicProductDetailListener
) : RecyclerView.ViewHolder(view) {

    fun bind(socialProof: ProductMiniSocialProofTokoNowItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {
        when (socialProof.type) {
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofChip -> bindChip(socialProof, componentTrackDataModel)
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofText -> bindText(socialProof)
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofSingleText -> bindSingleText(socialProof)
            ProductMiniSocialProofTokoNowItemType.ProductMiniSocialProofTextWithDivider -> bindTextDivider(socialProof)
        }
    }

    private fun bindChip(socialProof: ProductMiniSocialProofTokoNowItemDataModel, componentTrackDataModel: ComponentTrackDataModel?) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.chip_social_proof_title)
        val firstSocialProofValue = view.findViewById<Typography>(R.id.chip_social_proof_value)
        when (socialProof.key) {
            ProductMiniSocialProofTokoNowDataModel.RATING -> {
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
            else -> { // ProductMiniSocialProof2DataModel.BUYER_PHOTOS
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

    private fun bindText(socialProof: ProductMiniSocialProofTokoNowItemDataModel) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.apply {
            val htmlText = generateFirstSocialProofText(socialProof)
            text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else Html.fromHtml(htmlText)
            setPadding(8.toPx(), 7.toPx(), 8.toPx(), 7.toPx())
        }
    }

    private fun bindTextDivider(socialProof: ProductMiniSocialProofTokoNowItemDataModel) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.apply {
            val htmlText = generateFirstSocialProofText(socialProof)
            text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            } else Html.fromHtml(htmlText)
            setPadding(16.toPx(), 7.toPx(), 8.toPx(), 7.toPx())
        }
    }

    private fun bindSingleText(socialProof: ProductMiniSocialProofTokoNowItemDataModel) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.apply {
            text = generateSingleView(socialProof)
            setPadding(16.toPx(), 0, 16.toPx(), 0)
        }
    }

    private fun generateSingleView(socialProof: ProductMiniSocialProofTokoNowItemDataModel): String {
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

    private fun generateFirstSocialProofText(socialProof: ProductMiniSocialProofTokoNowItemDataModel): String {
        return when (socialProof.key) {
            ProductMiniSocialProofTokoNowDataModel.PAYMENT_VERIFIED -> {
                view.context.getString(R.string.label_terjual_tokonow_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofTokoNowDataModel.WISHLIST -> {
                view.context.getString(R.string.label_wishlist_tokonow_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofTokoNowDataModel.VIEW_COUNT -> {
                view.context.getString(R.string.label_view_tokonow_builder, socialProof.formattedCount)
            }
            ProductMiniSocialProofTokoNowDataModel.STOCK -> {
                view.context.getString(R.string.label_stock_builder, socialProof.formattedCount)
            }
            else -> {
                ""
            }
        }
    }

}