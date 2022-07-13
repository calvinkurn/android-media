package com.tokopedia.review.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.review.databinding.PartialWidgetReviewDetailBasicInfoBinding
import com.tokopedia.review.databinding.WidgetReviewDetailBasicInfoBinding
import com.tokopedia.review.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel
import com.tokopedia.review.feature.media.detail.presentation.uistate.ReviewDetailBasicInfoUiState
import com.tokopedia.reviewcommon.extension.generateHapticFeedback

open class ReviewDetailBasicInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseReviewDetailCustomView<WidgetReviewDetailBasicInfoBinding>(context, attrs, defStyleAttr) {
    override val binding = WidgetReviewDetailBasicInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var showReviewerInfo: Boolean = true
    private var listener: Listener? = null

    private fun hideReviewDetailBasicInfo() {
        gone()
        animateHide()
    }

    private fun showReviewDetailBasicInfoLoading() {
        show()
        binding.layoutReviewDetailBasicInfo.root.gone()
        binding.layoutReviewDetailBasicInfoShimmer.root.show()
        animateShow()
    }

    private fun showReviewDetailBasicInfoData(data: ReviewDetailBasicInfoUiModel, source: Source) {
        show()
        binding.layoutReviewDetailBasicInfoShimmer.root.gone()
        setData(data, source)
        binding.layoutReviewDetailBasicInfo.root.show()
        animateShow()
    }

    protected open fun setData(data: ReviewDetailBasicInfoUiModel, source: Source) {
        with(binding.layoutReviewDetailBasicInfo) {
            setupRating(data.rating)
            setupCreateTime(data.createTimeStr, source)
            setupLikeCount(data.likeCount, source)
            setupLikeButton(data.isLiked, source)
            setupReviewerProfilePicture(data.profilePicture)
            setupReviewerName(data.reviewerName, source)
            setupReviewerStatsSummary(data.reviewerStatsSummary, source)
            setupProductVariant(data.variant, source)
            setupListeners(data, source)
        }
    }

    fun updateUi(uiState: ReviewDetailBasicInfoUiState, source: Source) {
        when (uiState) {
            is ReviewDetailBasicInfoUiState.Hidden -> hideReviewDetailBasicInfo()
            is ReviewDetailBasicInfoUiState.Loading -> showReviewDetailBasicInfoLoading()
            is ReviewDetailBasicInfoUiState.Showing -> showReviewDetailBasicInfoData(uiState.data, source)
        }
    }

    fun hideReviewerInfo() {
        showReviewerInfo = false
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupRating(rating: Int) {
        reviewDetailRating.setRating(rating)
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupCreateTime(
        createTimeStr: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_96
        }
        tvReviewDetailCreateTime.run {
            text = createTimeStr
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(createTimeStr.isNotBlank())
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupLikeCount(
        likeCount: Int,
        source: Source
    ) {
        tvReviewDetailLikeCount.run {
            text = if (likeCount.isMoreThanZero() && source == Source.REVIEW_DETAIL_FRAGMENT) {
                likeCount.toString()
            } else ""
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupLikeButton(liked: Boolean, source: Source) {
        icReviewDetailLikeButton.showWithCondition(!liked && source == Source.REVIEW_DETAIL_FRAGMENT)
        icReviewDetailLikedButton.showWithCondition(liked && source == Source.REVIEW_DETAIL_FRAGMENT)
    }

    protected open fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerProfilePicture(profilePicture: String) {
        ivReviewDetailReviewerProfilePicture.setImageUrl(profilePicture)
    }

    protected open fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerName(
        reviewerName: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_96
        }
        tvReviewDetailReviewerName.run {
            text = reviewerName
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(reviewerName.isNotBlank())
        }
    }

    protected open fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerStatsSummary(
        reviewerStatsSummary: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }
        tvReviewDetailReviewerStatsSummary.run {
            text = buildString {
                append(" • ")
                append(reviewerStatsSummary)
            }
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(reviewerStatsSummary.isNotBlank())
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupProductVariant(
        variant: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }
        tvReviewDetailProductVariant.run {
            text = buildString {
                append("Varian: ")
                append(variant)
            }
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(variant.isNotBlank())
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupListeners(
        data: ReviewDetailBasicInfoUiModel,
        source: Source
    ) {
        setupLikeDislikeListener()
        setupOverlayCredibilityListener(data, source)
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupLikeDislikeListener() {
        overlayLikeDislikeClickArea.setOnClickListener {
            it?.generateHapticFeedback()
            listener?.onToggleLikeClicked()
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupOverlayCredibilityListener(
        data: ReviewDetailBasicInfoUiModel,
        source: Source
    ) {
        overlayCredibilityClickArea.setOnClickListener {
            if (!data.anonymous && source == Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET) {
                listener?.onGoToCredibilityClicked(
                    data.userId, data.reviewerStatsSummary, data.reviewerLabel
                )
            }
        }
    }

    interface Listener {
        fun onToggleLikeClicked()
        fun onGoToCredibilityClicked(
            userId: String,
            reviewerStatsSummary: String,
            reviewerLabel: String
        )
    }

    enum class Source {
        REVIEW_DETAIL_FRAGMENT, EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET
    }
}