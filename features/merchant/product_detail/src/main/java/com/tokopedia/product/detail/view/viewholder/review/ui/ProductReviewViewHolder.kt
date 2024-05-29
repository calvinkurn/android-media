package com.tokopedia.product.detail.view.viewholder.review.ui

import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.ItemSpaceDecorator
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMostHelpfulReviewUiModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.review.UserStatistic
import com.tokopedia.product.detail.data.util.ProductDetailMapper
import com.tokopedia.product.detail.databinding.ItemDynamicReviewBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.review.delegate.ReviewCallback
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.reviewcommon.R as reviewcommonR

class ProductReviewViewHolder(
    val view: View,
    val listener: ProductDetailListener,
    val callback: ReviewCallback
) : ProductDetailPageViewHolder<ProductMostHelpfulReviewUiModel>(view) {

    companion object {
        private const val MAX_LINES_REVIEW_DESCRIPTION = 3
        private val KEYWORD_ITEM_SPACING = 4.toPx()
        val LAYOUT = R.layout.item_dynamic_review
    }

    private val binding = ItemDynamicReviewBinding.bind(view)

    private var element: ProductMostHelpfulReviewUiModel? = null

    private val keywordAdapter by lazyThreadSafetyNone {
        RatingKeywordAdapter(callback = callback, pdpListener = listener).also(::setupRvKeyword)
    }

    private val keywordLayoutManager by lazyThreadSafetyNone {
        LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupRvKeyword(ratingKeywordAdapter: RatingKeywordAdapter) {
        binding.rvKeyword.apply {
            adapter = ratingKeywordAdapter
            layoutManager = keywordLayoutManager
            addItemDecoration(ItemSpaceDecorator(space = KEYWORD_ITEM_SPACING))
//            optimizeNestedRecyclerView()
        }
    }

    private fun RecyclerView.optimizeNestedRecyclerView() {
        setRecycledViewPool(listener.getParentRecyclerViewPool())
        setHasFixedSize(true)
    }

    init {
        binding.reviewMediaThumbnails.setListener(ReviewMediaThumbnailListener())
    }

    override fun bind(element: ProductMostHelpfulReviewUiModel?) {
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
            val componentData = getComponentTrackData(it)
            view.addOnImpressionListener(
                holder = element.impressHolder,
                holders = listener.getImpressionHolders(),
                name = element.name,
                useHolders = listener.isRemoteCacheableActive()
            ) {
                listener.onImpressComponent(componentData)
            }
            setSeeAllReviewClickListener(componentData)
            renderImageReview(
                element.mediaThumbnails,
                element.rating
            )
            renderKeyword(rating = element.rating)

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
        rating: ReviewRatingUiModel
    ) {
        with(binding) {
            reviewCount.apply {
                text = if (rating.totalReviewAndImage == "0") {
                    context.getString(R.string.pdp_review_rating_only_count, rating.ratingScore)
                } else {
                    context.getString(
                        R.string.pdp_review_rating_and_review_count,
                        rating.totalRating,
                        rating.totalReviewAndImage
                    )
                }
                show()
            }
            icMostHelpfulReviewRatingStar.apply {
                setImageDrawable(MethodChecker.getDrawable(context, reviewcommonR.drawable.ic_rating_star_item))
                show()
            }
            reviewRating.apply {
                text = rating.ratingScore
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

    private fun renderKeyword(rating: ReviewRatingUiModel) {
        binding.rvKeyword.showIfWithBlock(rating.keywords.isNotEmpty()) {
            keywordAdapter.submitList(rating.keywords)
        }
    }

    private fun setBasicInfoListener(review: Review) {
        binding.basicInfoMostHelpfulReview.setListeners(
            reviewBasicInfoListener = object : ReviewBasicInfoListener {
                override fun onUserNameClicked(
                    feedbackId: String,
                    userId: String,
                    statistics: String,
                    label: String
                ) {
                    element?.let {
                        listener.onSeeReviewCredibility(
                            feedbackId,
                            userId,
                            statistics,
                            label,
                            getComponentTrackData(it)
                        )
                    }
                }
            },
            threeDotsListener = null
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

        val txtReview = binding.txtDescReviewPdp
        txtReview.show()
        txtReview.text = HtmlLinkHelper(txtReview.context, reviewData.message).spannedString ?: ""
        txtReview.post {
            val lineCount = txtReview.layout?.lineCount ?: return@post
            if (lineCount > MAX_LINES_REVIEW_DESCRIPTION) {
                txtReview.maxLines = MAX_LINES_REVIEW_DESCRIPTION

                val txtExpand = binding.txtDescReviewExpand
                txtExpand.show()
                txtExpand.setOnClickListener(onClickExpand)
                txtReview.setOnClickListener(onClickExpand)
            }
        }
    }

    private val onClickExpand = OnClickListener {
        val txtReview = binding.txtDescReviewPdp
        val txtExpand = binding.txtDescReviewExpand
        txtReview.maxLines = Int.MAX_VALUE
        txtExpand.hide()
    }

    private fun hideAllOtherElements() {
        binding.apply {
            txtReviewTitle.hide()
            txtSeeAllPartial.hide()
            icMostHelpfulReviewRatingStar.hide()
            reviewRating.hide()
            reviewCount.hide()
            reviewMediaThumbnails.hide()
            basicInfoMostHelpfulReview.hide()
            txtDescReviewPdp.hide()
        }
    }

    private fun hideMostHelpfulElements() {
        binding.apply {
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
                            item.getAttachmentID(),
                            position,
                            getComponentTrackData(it),
                            ProductDetailMapper.generateDetailedMediaResult(
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
                            item.getAttachmentID(),
                            position,
                            getComponentTrackData(it),
                            ProductDetailMapper.generateDetailedMediaResult(
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
