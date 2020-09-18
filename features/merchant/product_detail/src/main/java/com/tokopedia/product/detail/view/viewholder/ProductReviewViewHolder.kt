package com.tokopedia.product.detail.view.viewholder

import android.view.View
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
import kotlinx.android.synthetic.main.item_dynamic_review.view.rating_review_pdp
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_date_user_pdp
import kotlinx.android.synthetic.main.item_dynamic_review.view.txt_desc_review_pdp

class ProductReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3
        val LAYOUT = R.layout.item_dynamic_review
    }

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        element?.let {
            val componentData = getComponentTrackData(it)
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentData)
            }
            setSeeAllReviewClickListener(componentData)
            it.imageReviews?.let { images ->
                renderImageReview(images, element.totalRating, element.ratingScore, componentData)
            }
            val reviewData = it.listOfReviews.firstOrNull()
            reviewData?.let { review ->
                setReviewStars(review)
                setReviewAuthor(reviewData)
                setReviewVariant(review)
                setReviewDescription(review)
            }
        }
    }

    private fun setSeeAllReviewClickListener(componentData: ComponentTrackDataModel) {
        view.txt_see_all_partial.setOnClickListener {
            listener.onSeeAllTextView(componentData)
        }
    }

    private fun renderImageReview(imageReviews: List<ImageReviewItem>, totalRating: Int, ratingScore: Float, componentTrackDataModel: ComponentTrackDataModel) {
        val showSeeAll = if (imageReviews.isNotEmpty()) {
            imageReviews.first().hasNext
        } else {
            false
        }

        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll, listener::onImageReviewClick, listener::onSeeAllLastItemImageReview,
                componentTrackDataModel)

        with(view) {
            txt_see_all_partial.setOnClickListener {
                listener.onSeeAllTextView(componentTrackDataModel)
            }
            review_count.text = context.getString(R.string.review_out_of_total_reviews, totalRating)
            review_rating.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable(context, R.drawable.ic_rating_gold), null)
            review_rating.text = ratingScore.toString()

            if (imageReviews.isNotEmpty())
                image_review_list.visible()
            else
                image_review_list.gone()
        }
    }

    private fun setReviewStars(reviewData: Review) {
        view.apply {
            ImageHandler.loadImageRounded2(context, rating_review_pdp, RatingView.getRatingDrawable(reviewData.productRating), 0f)
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
            view.txt_variant_review_pdp.apply{
                show()
                text = review.productVariantReview.variantTitle
            }
        } else {
            view.txt_variant_review_pdp.hide()
        }
    }

    private fun setReviewDescription(reviewData: Review) {
        if(reviewData.message.isEmpty()) {
            view.txt_desc_review_pdp.hide()
            return
        }
        view.txt_desc_review_pdp.apply {
            maxLines = MAX_LINES_REVIEW_DESCRIPTION
            text = ProductDetailUtil.reviewDescFormatter(reviewData.message)
            setOnClickListener {
                maxLines = Integer.MAX_VALUE
                text = reviewData.message
            }
            show()
        }
    }

    private fun getComponentTrackData(data: ProductMostHelpfulReviewDataModel): ComponentTrackDataModel =
            ComponentTrackDataModel(data.type, data.name, adapterPosition + 1)
}