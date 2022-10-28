package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.review.UserStatistic
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.databinding.ItemDynamicReviewBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ProductDetailUtil
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ProductReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) :
        AbstractViewHolder<ProductMostHelpfulReviewDataModel>(view) {

    companion object {
        const val MAX_LINES_REVIEW_DESCRIPTION = 3
        val LAYOUT = R.layout.item_dynamic_review
    }

    private val binding = ItemDynamicReviewBinding.bind(view)
    private var element: ProductMostHelpfulReviewDataModel? = null

    init {
        binding.reviewMediaThumbnails.setListener(ReviewMediaThumbnailListener())
    }

    override fun bind(element: ProductMostHelpfulReviewDataModel?) {
        this.element = element
        element?.let {
            if (it.mediaThumbnails == null && element.review == null) {
                showShimmering()
                hideAllOtherElements()
                return
            }
            hideShimmering()
            hideBasicInfoThreeDots()
            showTitle()
            binding.dividerMostHelpfulReviewThumbnails.show()
            val componentData = getComponentTrackData(it)
            view.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(componentData)
            }
            setSeeAllReviewClickListener(componentData)
            renderImageReview(
                element.mediaThumbnails,
                element.formattedRating,
                element.totalRatingCount,
                element.totalReviewCount
            )
            val reviewData = it.review
            reviewData?.let { review ->
                setBasicInfoListener(review)
                setCredibilityData(review)
                setReviewStars(review)
                setReviewTimestamp(review)
                setReviewAuthorProfilePicture(review)
                setReviewAuthorName(reviewData)
                setReviewAuthorLabel(review)
                setReviewAuthorStats(review)
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

    private fun hideBasicInfoThreeDots() {
        binding.basicInfoMostHelpfulReview.hideThreeDots()
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

    private fun renderImageReview(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel?,
        formattedRating: String,
        formattedRatingCount: String,
        formattedReviewCount: String
    ) {
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
            icMostHelpfulReviewRatingStar.apply {
                setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.reviewcommon.R.drawable.ic_rating_star_item))
                show()
            }
            reviewRating.apply {
                text = formattedRating
                show()
            }

            if (reviewMediaThumbnailUiModel?.mediaThumbnails?.isNotEmpty() == true) {
                reviewMediaThumbnails.apply {
                    setData(reviewMediaThumbnailUiModel)
                    show()
                }
                return
            }
            reviewMediaThumbnails.gone()
        }
    }

    private fun setBasicInfoListener(review: Review) {
        binding.basicInfoMostHelpfulReview.setListeners(
            reviewBasicInfoListener = object : ReviewBasicInfoListener {
                override fun onUserNameClicked(
                    feedbackId: String, userId: String, statistics: String, label: String
                ) {
                    element?.let {
                        listener.onSeeReviewCredibility(
                            feedbackId, userId, statistics, label, getComponentTrackData(it)
                        )
                    }
                }
            }, threeDotsListener = null
        )
    }

    private fun setCredibilityData(review: Review) {
        binding.basicInfoMostHelpfulReview.setCredibilityData(
            isProductReview = true,
            isAnonymous = false,
            userId = review.user.userId.toString(),
            feedbackId = review.reviewId.toString()
        )
    }

    private fun setReviewStars(reviewData: Review) {
        binding.basicInfoMostHelpfulReview.setRating(reviewData.productRating)
    }

    private fun setReviewTimestamp(review: Review) {
        binding.basicInfoMostHelpfulReview.setCreateTime(review.reviewCreateTime)
    }

    private fun setReviewAuthorProfilePicture(review: Review) {
        binding.basicInfoMostHelpfulReview.setReviewerImage(review.user.image)
    }

    private fun setReviewAuthorName(reviewData: Review) {
        binding.basicInfoMostHelpfulReview.setReviewerName(reviewData.user.fullName)
    }

    private fun setReviewAuthorLabel(review: Review) {
        binding.basicInfoMostHelpfulReview.setReviewerLabel(review.userLabel)
    }

    private fun setReviewAuthorStats(review: Review) {
        binding.basicInfoMostHelpfulReview.setStatsString(composeUserStatistics(review.userStat.orEmpty()))
    }

    private fun setReviewVariant(review: Review) {
        binding.basicInfoMostHelpfulReview.setVariantName(review.variant.variantTitle)
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

    private fun hideAllOtherElements() {
        binding.apply {
            txtReviewTitle.hide()
            txtSeeAllPartial.hide()
            icMostHelpfulReviewRatingStar.hide()
            reviewRating.hide()
            reviewCount.hide()
            reviewMediaThumbnails.hide()
            dividerMostHelpfulReviewThumbnails.hide()
            basicInfoMostHelpfulReview.hide()
            txtDescReviewPdp.hide()
        }
    }

    private fun hideMostHelpfulElements() {
        binding.apply {
            dividerMostHelpfulReviewThumbnails.hide()
            basicInfoMostHelpfulReview.hide()
            txtDescReviewPdp.hide()
        }
    }

    private inner class ReviewMediaThumbnailListener : ReviewMediaThumbnailTypeFactory.Listener {
        override fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int) {
            element?.let {
                if (item is ReviewMediaImageThumbnailUiModel) {
                    if (item.uiState is ReviewMediaImageThumbnailUiState.ShowingSeeMore) {
                        listener.onSeeAllLastItemMediaReview(getComponentTrackData(it))
                    } else {
                        listener.onMediaReviewClick(
                            item.getReviewID(),
                            position,
                            getComponentTrackData(it),
                            DynamicProductDetailMapper.generateDetailedMediaResult(
                                it.mediaThumbnails
                            )
                        )
                    }
                } else if (item is ReviewMediaVideoThumbnailUiModel) {
                    if (item.uiState is ReviewMediaVideoThumbnailUiState.ShowingSeeMore) {
                        listener.onSeeAllLastItemMediaReview(getComponentTrackData(it))
                    } else {
                        listener.onMediaReviewClick(
                            item.getReviewID(),
                            position,
                            getComponentTrackData(it),
                            DynamicProductDetailMapper.generateDetailedMediaResult(
                                it.mediaThumbnails
                            )
                        )
                    }
                }
                return@let
            }
        }
    }

    private fun composeUserStatistics(userStatistics: List<UserStatistic>): String {
        return userStatistics.joinToString(separator = " • ") { it.formatted }
    }
}