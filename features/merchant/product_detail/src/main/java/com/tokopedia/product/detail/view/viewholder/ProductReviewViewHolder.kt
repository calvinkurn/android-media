package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.databinding.ItemDynamicReviewBinding
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ProductReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3
        const val GRID_LAYOUT_MANAGER_SPAN_COUNT = 5
        const val RATING_ONE = 1
        const val RATING_TWO = 2
        const val RATING_THREE = 3
        const val RATING_FOUR = 4
        const val RATING_FIVE = 5
        val LAYOUT = R.layout.item_dynamic_review
    }

    private val binding = ItemDynamicReviewBinding.bind(view)

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        element?.let {
            if (it.imageReviews == null && it.listOfReviews == null) {
                showShimmering()
                hideAllOtherElements()
                return
            }
            hideShimmering()
            showTitle()
            val componentData = getComponentTrackData(it)
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentData)
            }
            setSeeAllReviewClickListener(componentData)
            it.imageReviews?.let { images ->
                renderImageReview(images, element.totalRating, element.ratingScore, element.formattedRating, element.totalRatingCount, element.totalReviewCount, componentData)
            }
            val reviewData = it.listOfReviews?.firstOrNull()
            reviewData?.let { review ->
                setReviewStars(review)
                setReviewAuthor(reviewData)
                setReviewVariant(review)
                setReviewDescription(review)
                return
            }
            hideMostHelpfulElements()
        }
    }

    private fun showShimmering() {
        binding.reviewShimmering.root.show()
    }

    private fun hideShimmering() {
        binding.reviewShimmering.root.hide()
    }

    private fun showTitle() {
        binding.txtReviewTitle.show()
    }

    private fun setSeeAllReviewClickListener(componentData: ComponentTrackDataModel) {
        binding.txtSeeAllPartial.apply {
            setOnClickListener {
                listener.onSeeAllTextView(componentData)
            }
            show()
        }
    }

    private fun renderImageReview(imageReviews: List<ImageReviewItem>, totalRating: Int, ratingScore: Float, formattedRating: String, formattedRatingCount: String, formattedReviewCount: String, componentTrackDataModel: ComponentTrackDataModel) {
        val showSeeAll = if (imageReviews.isNotEmpty()) {
            imageReviews.first().hasNext
        } else {
            false
        }

        with(binding) {
            reviewCount.apply {
                text = if (formattedReviewCount == "0") {
                    context.getString(R.string.pdp_review_rating_only_count, formattedRatingCount)
                } else {
                    context.getString(
                        R.string.pdp_review_rating_and_review_count,
                        formattedRatingCount,
                        formattedReviewCount
                    )
                }
                show()
            }
            reviewRating.apply {
                setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(context, R.drawable.ic_review_rating_star), null, null, null)
                text = formattedRating
                show()
            }

            if (imageReviews.isNotEmpty()) {
                imageReviewList.apply {
                    adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll, listener::onImageReviewClick, listener::onSeeAllLastItemImageReview,
                            componentTrackDataModel)
                    show()
                    layoutManager = GridLayoutManager(context, GRID_LAYOUT_MANAGER_SPAN_COUNT)
                }
                return
            }
            imageReviewList.gone()
        }
    }

    private fun setReviewStars(reviewData: Review) {
        binding.ratingReviewPdp.apply {
            if (reviewData.productRating == 0) {
                hide()
                return
            }
            setImageDrawable(MethodChecker.getDrawable(context, getRatingDrawable(reviewData.productRating)))
            show()
        }
    }

    private fun setReviewAuthor(reviewData: Review) {
        binding.txtDateUserPdp.apply {
            if (reviewData.user.fullName.isEmpty()) {
                return
            }
            text = HtmlLinkHelper(context, context.getString(R.string.review_author, reviewData.user.fullName)).spannedString
            show()
        }
    }

    private fun setReviewVariant(review: Review) {
        if (review.variant.variantTitle.isNotEmpty()) {
            binding.txtVariantReviewPdp.apply {
                show()
                text = review.variant.variantTitle
            }
        } else {
            binding.txtVariantReviewPdp.hide()
        }
    }

    private fun setReviewDescription(reviewData: Review) {
        if (reviewData.message.isEmpty()) {
            binding.txtDescReviewPdp.hide()
            return
        }
        binding.txtDescReviewPdp.apply {
            maxLines = MAX_LINES_REVIEW_DESCRIPTION
            val formattingResult = ProductDetailUtil.reviewDescFormatter(context, reviewData.message)
            text = formattingResult.first
            if(formattingResult.second) {
                setOnClickListener {
                    maxLines = Integer.MAX_VALUE
                    text = HtmlLinkHelper(context, reviewData.message).spannedString
                }
            }
            show()
        }
    }

    private fun getComponentTrackData(data: ProductMostHelpfulReviewDataModel): ComponentTrackDataModel =
            ComponentTrackDataModel(data.type, data.name, adapterPosition + 1)

    private fun getRatingDrawable(param: Int): Int {
        return when (param) {
            RATING_ONE -> R.drawable.ic_rating_star_one
            RATING_TWO -> R.drawable.ic_rating_star_two
            RATING_THREE -> R.drawable.ic_rating_star_three
            RATING_FOUR -> R.drawable.ic_rating_star_four
            RATING_FIVE -> R.drawable.ic_rating_star_five
            else -> R.drawable.ic_rating_star_zero
        }
    }

    private fun hideAllOtherElements() {
        binding.apply {
            txtReviewTitle.hide()
            txtSeeAllPartial.hide()
            reviewRating.hide()
            reviewCount.hide()
            imageReviewList.hide()
            ratingReviewPdp.hide()
            txtDateUserPdp.hide()
            txtVariantReviewPdp.hide()
            txtDescReviewPdp.hide()
        }
    }

    private fun hideMostHelpfulElements() {
        binding.apply {
            ratingReviewPdp.hide()
            txtDescReviewPdp.hide()
            txtVariantReviewPdp.hide()
            txtDateUserPdp.hide()
        }
    }
}