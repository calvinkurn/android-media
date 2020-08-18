package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.customview.RatingView
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import com.tokopedia.product.detail.view.adapter.MostHelpfulReviewAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.PaddingItemDecoration
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import kotlinx.android.synthetic.main.item_dynamic_image_review.view.*
import kotlinx.android.synthetic.main.item_dynamic_mosthelpful_review.view.*
import kotlinx.android.synthetic.main.item_dynamic_mosthelpful_review.view.container_most_helpful_review
import kotlinx.android.synthetic.main.item_dynamic_review.view.container_image_review

class ProductReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_review
        private const val FULL_SPAN = 1
        private const val TWO_SPAN = 2
    }

    init {
        with(view) {
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }
    }

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        element?.let {
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(getComponentTrackData(element))
            }

            renderMostHelpfulReview(it.listOfReviews, getComponentTrackData(element))
        }

        element?.imageReviews?.let {
            renderImageReview(it, element.rating ?: Rating(), getComponentTrackData(element))
        }
    }

    private fun renderImageReview(imageReviews: List<ImageReviewItem>, rating: Rating, componentTrackDataModel: ComponentTrackDataModel) {
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
            review_count.text = context.getString(R.string.review_counter, rating.totalRating)
            review_rating.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable(context, R.drawable.ic_rating_gold), null)
            review_rating.text = context.getString(R.string.counter_pattern_string, rating.ratingScore, 5)

            if (rating.totalRating > 0) container_image_review.visible() else container_image_review.gone()

            if (imageReviews.isNotEmpty())
                image_review_list.visible()
            else
                image_review_list.gone()
        }
    }

    private fun getComponentTrackData(data: ProductMostHelpfulReviewDataModel): ComponentTrackDataModel =
            ComponentTrackDataModel(data.type, data.name, adapterPosition + 1)

    private fun renderMostHelpfulReview(reviews: List<Review>, componentTrackDataModel: ComponentTrackDataModel) {
        with(view) {
            if (reviews.isEmpty()) {
                container_most_helpful_review.gone()
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

                ImageHandler.loadImageRounded2(context, rating_review_pdp, RatingView.getRatingDrawable(reviewData.productRating), 0f)
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

                    if (itemDecorationCount == 0)
                        addItemDecoration(PaddingItemDecoration())

                    adapter = MostHelpfulReviewAdapter(imageData, reviewData.reviewId.toString(), moreItemCount, listener::onImageHelpfulReviewClick, componentTrackDataModel)
                    if (reviewData.imageAttachments.isNotEmpty()) {
                        show()
                    } else {
                        hide()
                    }
                }
                container_most_helpful_review.visible()
            }
        }
    }

}