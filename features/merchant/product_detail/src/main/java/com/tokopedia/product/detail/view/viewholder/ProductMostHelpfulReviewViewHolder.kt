package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.customview.RatingView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.MostHelpfulReviewAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.PaddingItemDecoration
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import kotlinx.android.synthetic.main.item_dynamic_mosthelpful_review.view.*

class ProductMostHelpfulReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_mosthelpful_review
        private const val FULL_SPAN = 1
        private const val TWO_SPAN = 2
    }

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        element?.let {
            renderData(it.listOfReviews)
        }
    }

    fun renderData(reviews: List<Review>) {
        with(view) {
            if (reviews.isEmpty()) {
                gone()
            } else {
                val reviewData = reviews.first()
                val imageData = if (reviewData.imageAttachments.size > 3) reviewData.imageAttachments.take(3) else reviewData.imageAttachments
                if (!reviewData.likeDislike.isShowable) {
                    txt_thumb_like_pdp.hide()
                    txt_like_static_pdp.hide()
                } else {
                    txt_thumb_like_pdp.text = view.context.getString(R.string.review_like_pattern, reviewData.likeDislike.totalLike)
                    txt_thumb_like_pdp.show()
                    txt_like_static_pdp.show()
                }

                if (reviews.first().productVariantReview.variantTitle.isNotEmpty()) {
                    txt_variant_review_pdp.show()
                    txt_variant_review_pdp.text = reviews.first().productVariantReview.variantTitle
                } else {
                    txt_variant_review_pdp.hide()
                }

                rating_review_pdp.setImageDrawable(ContextCompat.getDrawable(view.context,
                        RatingView.getRatingDrawable(reviewData.productRating)))
                txt_date_user_pdp.text = MethodChecker.fromHtml(
                        view.context.getString(R.string.date_review_pattern, reviewData.reviewCreateTime, "<b>" + reviewData.user.fullName + "</b>"))
                txt_desc_review_pdp.maxLines = 4
                txt_desc_review_pdp.text = ProductDetailUtil.reviewDescFormatter(reviewData.message)

                txt_desc_review_pdp.setOnClickListener {
                    txt_desc_review_pdp.maxLines = Integer.MAX_VALUE
                    txt_desc_review_pdp.text = reviews.first().message
                }

                val moreItemCount = if (reviewData.imageAttachments.size > 3) reviewData.imageAttachments.size - 3 else 0
                rv_review_pdp.run {
                    setHasFixedSize(true)
                    layoutManager = StaggeredGridLayoutManager(if (reviewData.imageAttachments.size == 1) FULL_SPAN else TWO_SPAN, GridLayoutManager.VERTICAL)
                    addItemDecoration(PaddingItemDecoration())
                    adapter = MostHelpfulReviewAdapter(imageData, reviewData.reviewId.toString(), moreItemCount, listener::onImageHelpfulReviewClick)
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