package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.customview.RatingView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.util.OnImageReviewClicked
import com.tokopedia.product.detail.view.adapter.MostHelpfulReviewAdapter
import com.tokopedia.product.detail.view.util.PaddingItemDecoration
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import kotlinx.android.synthetic.main.partial_most_helpful_review_view.view.*

class PartialMostHelpfulReviewView private constructor(private val view: View) {
    var onImageReviewClicked: OnImageReviewClicked? = null

    companion object {
        private const val LIKE_VISIBILITY_LIMIT = 3
        private const val FULL_SPAN = 1
        private const val TWO_SPAN = 2
        fun build(_view: View) = PartialMostHelpfulReviewView(_view)
    }

    fun renderData(reviews: List<Review>) {
        with(view) {
            if (reviews.isEmpty()) {
                gone()
            } else {
                val reviewData = reviews.first()
                val imageData = if (reviewData.imageAttachments.size > 3) reviewData.imageAttachments.take(3) else reviewData.imageAttachments
                if (!reviewData.likeDislike.isShowable) {
                    txt_thumb_like.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    txt_thumb_like.hide()
                    txt_like_static.hide()
                } else {
                    txt_thumb_like.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(context, R.drawable.ic_thumb), null, null, null)
                    txt_thumb_like.text = view.context.getString(R.string.review_like_pattern, reviewData.likeDislike.totalLike)
                    txt_thumb_like.show()
                    txt_like_static.show()
                }

                if (reviews.first().productVariantReview.variantTitle.isNotEmpty()) {
                    txt_variant_review.show()
                    txt_variant_review.text = reviews.first().productVariantReview.variantTitle
                } else {
                    txt_variant_review.hide()
                }

                rating_review.setImageDrawable(ContextCompat.getDrawable(view.context,
                        RatingView.getRatingDrawable(reviewData.productRating)))
                txt_date_user.text = MethodChecker.fromHtml(
                        view.context.getString(R.string.date_review_pattern, reviewData.reviewCreateTime, "<b>" + reviewData.user.fullName + "</b>"))
                txt_desc_review.maxLines = 4
                txt_desc_review.text = ProductDetailUtil.reviewDescFormatter(reviewData.message)

                txt_desc_review.setOnClickListener {
                    txt_desc_review.maxLines = Integer.MAX_VALUE
                    txt_desc_review.text = reviews.first().message
                }

                val moreItemCount = if (reviewData.imageAttachments.size > 3) reviewData.imageAttachments.size - 3 else 0


                rv_review.run {
                    setHasFixedSize(true)
                    layoutManager = StaggeredGridLayoutManager(if (reviewData.imageAttachments.size == 1) FULL_SPAN else TWO_SPAN, GridLayoutManager.VERTICAL)
                    addItemDecoration(PaddingItemDecoration())
                    adapter = MostHelpfulReviewAdapter(imageData, reviewData.reviewId.toString(), moreItemCount, onImageReviewClicked)
                    if (reviewData.imageAttachments.isNotEmpty()) {
                        show()
                    } else {
                        hide()
                    }
                }

                show()

            }
        }
    }
}