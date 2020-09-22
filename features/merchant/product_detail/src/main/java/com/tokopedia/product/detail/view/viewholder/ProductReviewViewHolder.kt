package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.customview.RatingView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.item_dynamic_review.view.*
import kotlinx.android.synthetic.main.item_dynamic_review.view.image_review_list
import kotlinx.android.synthetic.main.item_dynamic_review.view.rating_review_pdp
import kotlinx.android.synthetic.main.item_dynamic_review.view.review_count
import kotlinx.android.synthetic.main.item_dynamic_review.view.review_rating
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_date_user_pdp
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_desc_review_pdp
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_review_title
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_see_all_partial

class ProductReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3
        const val IS_NEW_VIEW_HOLDER = true
        val LAYOUT = R.layout.item_dynamic_review
    }

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        element?.let {
            if (it.imageReviews == null && it.listOfReviews == null) {
                showShimmering()
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
                renderImageReview(images, element.totalRating, element.ratingScore, componentData)
            }
            val reviewData = it.listOfReviews?.firstOrNull()
            reviewData?.let { review ->
                setReviewStars(review)
                setReviewAuthor(reviewData)
                setReviewVariant(review)
                setReviewDescription(review)
            }
        }
    }

    private fun showShimmering() {
        view.review_shimmering.show()
    }

    private fun hideShimmering() {
        view.review_shimmering.hide()
    }

    private fun showTitle() {
        view.txt_review_title.show()
    }

    private fun setSeeAllReviewClickListener(componentData: ComponentTrackDataModel) {
        view.txt_see_all_partial.apply {
            setOnClickListener {
                listener.onSeeAllTextView(componentData)
            }
            show()
        }
    }

    private fun renderImageReview(imageReviews: List<ImageReviewItem>, totalRating: Int, ratingScore: Float, componentTrackDataModel: ComponentTrackDataModel) {
        val showSeeAll = if (imageReviews.isNotEmpty()) {
            imageReviews.first().hasNext
        } else {
            false
        }

        with(view) {
            review_count.apply {
                text = context.getString(R.string.review_out_of_total_reviews, totalRating)
                show()
            }
            review_rating.apply {
                setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable(context, R.drawable.ic_rating_gold), null)
                text = ratingScore.toString()
                show()
            }

            if (imageReviews.isNotEmpty()) {
                image_review_list.apply {
                    adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll, listener::onImageReviewClick, listener::onSeeAllLastItemImageReview,
                            componentTrackDataModel, IS_NEW_VIEW_HOLDER)
                    show()
                    layoutManager = GridLayoutManager(context, 5)
                }
                return
            }
            image_review_list.gone()
        }
    }

    private fun setReviewStars(reviewData: Review) {
        view.apply {
            ImageHandler.loadImageRounded2(context, rating_review_pdp, RatingView.getRatingDrawable(reviewData.productRating), 0f)
            rating_review_pdp.show()
        }
    }

    private fun setReviewAuthor(reviewData: Review) {
        view.txt_date_user_pdp.apply {
            text = HtmlLinkHelper(context, context.getString(R.string.review_author, reviewData.user.fullName)).spannedString
            show()
        }
    }

    private fun setReviewVariant(review: Review) {
        if (review.productVariantReview.variantTitle.isNotEmpty()) {
            view.txt_variant_review_pdp.apply {
                show()
                text = review.productVariantReview.variantTitle
            }
        } else {
            view.txt_variant_review_pdp.hide()
        }
    }

    private fun setReviewDescription(reviewData: Review) {
        if (reviewData.message.isEmpty()) {
            view.txt_desc_review_pdp.hide()
            return
        }
        view.txt_desc_review_pdp.apply {
            maxLines = MAX_LINES_REVIEW_DESCRIPTION
            val formattingResult = ProductDetailUtil.reviewDescFormatter(context, reviewData.message)
            text = formattingResult.first
            if(formattingResult.second) {
                setOnClickListener {
                    maxLines = Integer.MAX_VALUE
                    text = reviewData.message
                }
            }
            show()
        }
    }

    private fun getComponentTrackData(data: ProductMostHelpfulReviewDataModel): ComponentTrackDataModel =
            ComponentTrackDataModel(data.type, data.name, adapterPosition + 1)
}