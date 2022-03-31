package com.tokopedia.reviewcommon.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.databinding.PartialWidgetReviewDetailBasicInfoBinding
import com.tokopedia.reviewcommon.databinding.WidgetReviewDetailBasicInfoBinding
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailBasicInfoUiState
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewDetailBasicInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetReviewDetailBasicInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var listener: Listener? = null
    private var uiState: ReviewDetailBasicInfoUiState? = null

    init {
        binding.layoutReviewDetailBasicInfo.icReviewDetailToggleExpandButton.setOnClickListener {
            listener?.onToggleExpandClicked()
        }
        binding.layoutReviewDetailBasicInfo.icReviewDetailLikeButton.setOnClickListener {
            listener?.onToggleLikeClicked()
        }
        binding.layoutReviewDetailBasicInfo.icReviewDetailLikedButton.setOnClickListener {
            listener?.onToggleLikeClicked()
        }
        binding.layoutReviewDetailBasicInfo.root.setOnClickListener {
            uiState?.let {
                if (it is ReviewDetailBasicInfoUiState.Showing && !it.data.anonymous) {
                    listener?.onGoToCredibilityClicked(it.data.userId, it.data.reviewerStatsSummary)
                }
            }
        }
    }

    private fun hideReviewDetailBasicInfo() {
        gone()
    }

    private fun showReviewDetailBasicInfoLoading() {
        show()
        binding.layoutReviewDetailBasicInfo.root.gone()
        binding.layoutReviewDetailBasicInfoShimmer.root.show()
    }

    private fun showReviewDetailBasicInfoData(data: ReviewDetailBasicInfoUiModel, source: Source) {
        show()
        binding.layoutReviewDetailBasicInfoShimmer.root.gone()
        setData(data, source)
        binding.layoutReviewDetailBasicInfo.root.show()
    }

    private fun setData(data: ReviewDetailBasicInfoUiModel, source: Source) {
        with(binding.layoutReviewDetailBasicInfo) {
            setupRating(data.rating)
            setupCreateTime(data.createTimeStr, source)
            setupLikeCount(data.likeCount, source)
            setupLikeButton(data.isLiked, source)
            setupToggleExpandButton(data.expanded, source)
            setupReviewerProfilePicture(data.profilePicture)
            setupReviewerName(data.reviewerName, source)
            setupReviewerStatsSummary(data.reviewerStatsSummary, source)
        }
    }

    fun updateUi(uiState: ReviewDetailBasicInfoUiState, source: Source) {
        this.uiState = uiState
        when (uiState) {
            is ReviewDetailBasicInfoUiState.Hidden -> hideReviewDetailBasicInfo()
            is ReviewDetailBasicInfoUiState.Loading -> showReviewDetailBasicInfoLoading()
            is ReviewDetailBasicInfoUiState.Showing -> showReviewDetailBasicInfoData(uiState.data, source)
        }
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
            text = likeCount.toString()
            showWithCondition(likeCount.isMoreThanZero() && source == Source.REVIEW_DETAIL_FRAGMENT)
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupLikeButton(liked: Boolean, source: Source) {
        icReviewDetailLikeButton.showWithCondition(!liked && source == Source.REVIEW_DETAIL_FRAGMENT)
        icReviewDetailLikedButton.showWithCondition(liked && source == Source.REVIEW_DETAIL_FRAGMENT)
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupToggleExpandButton(
        expanded: Boolean,
        source: Source
    ) {
        icReviewDetailToggleExpandButton.run {
            rotationX = if (expanded) 0f else 180f
            showWithCondition(source == Source.REVIEW_DETAIL_FRAGMENT)
        }
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerProfilePicture(profilePicture: String) {
        ivReviewDetailReviewerProfilePicture.setImageUrl(profilePicture)
    }

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerName(
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

    private fun PartialWidgetReviewDetailBasicInfoBinding.setupReviewerStatsSummary(
        reviewerStatsSummary: String,
        source: Source
    ) {
        val colorRes = when(source) {
            Source.REVIEW_DETAIL_FRAGMENT -> com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            Source.EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET -> com.tokopedia.unifyprinciples.R.color.Unify_N700_68
        }
        tvReviewDetailReviewerStatsSummary.run {
            text = reviewerStatsSummary
            setTextColor(ContextCompat.getColor(context, colorRes))
            showWithCondition(reviewerStatsSummary.isNotBlank())
        }
    }

    interface Listener {
        fun onToggleExpandClicked()
        fun onToggleLikeClicked()
        fun onGoToCredibilityClicked(userId: String, reviewerStatsSummary: String)
    }

    enum class Source {
        REVIEW_DETAIL_FRAGMENT, EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET
    }
}