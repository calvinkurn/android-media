package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.databinding.WidgetReviewDetailScoreBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewScoreWidget : BaseCustomView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var currentScore = 0

    private val binding = WidgetReviewDetailScoreBinding.inflate(LayoutInflater.from(context), this, true)

    fun setEditableScore(score: Int) {
        binding.apply {
            currentScore = score
            when (score) {
                ReviewConstants.REPUTATION_SCORE_BAD -> {
                    reviewEditableBadSmiley.apply {
                        showActiveBad()
                    }
                    setDeadline()
                }
                ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                    reviewEditableMediocreSmiley.apply {
                        showActiveMediocre()
                    }
                    setDeadline()
                }
                ReviewConstants.REPUTATION_SCORE_EXCELLENT -> {
                    reviewEditableExcellentSmiley.apply {
                        showActiveExcellent()
                    }
                    setDeadline()
                }
                else -> {
                    setEmptyScore()
                }
            }
            reviewEditableBadSmiley.show()
            reviewEditableMediocreSmiley.show()
            reviewEditableExcellentSmiley.show()
        }
    }

    fun setReviewScoreClickListener(reviewScoreClickListener: ReviewScoreClickListener) {
        binding.apply {
            reviewEditableBadSmiley.setSmileyClickListener(reviewScoreClickListener)
            reviewEditableMediocreSmiley.setSmileyClickListener(reviewScoreClickListener)
            reviewEditableExcellentSmiley.setSmileyClickListener(reviewScoreClickListener)
        }
    }

    fun setShopName(shopName: String) {
        binding.reviewDetailScoreShopName.apply {
            text = MethodChecker.fromHtml(shopName)
            show()
        }
    }

    fun setExpired() {
        binding.reviewDetailScoreTitle.text = context.getString(R.string.review_detail_score_expired)
    }

    fun setFinalScore(score: Int) {
        binding.apply {
            when (score) {
                ReviewConstants.REPUTATION_SCORE_BAD -> {
                    reviewDetailScoreSmiley.loadImage(R.drawable.ic_smiley_bad_active)
                    reviewDetailScoreText.apply {
                        text = context.getString(R.string.review_detail_score_bad)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500))
                    }
                }
                ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                    reviewDetailScoreSmiley.loadImage(R.drawable.ic_smiley_neutral_active)
                    reviewDetailScoreText.apply {
                        text = context.getString(R.string.review_detail_score_mediocre)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300))
                    }
                }
                // ReviewConstants.REPUTATION_SCORE_EXCELLENT
                else -> {
                    reviewDetailScoreSmiley.loadImage(R.drawable.ic_smiley_great_active)
                    reviewDetailScoreText.apply {
                        text = context.getString(R.string.review_detail_score_excellent)
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                    }
                }
            }
            reviewDetailScoreSmiley.show()
            reviewDetailScoreText.show()
            reviewEditableBadSmiley.hide()
            reviewEditableMediocreSmiley.hide()
            reviewEditableExcellentSmiley.hide()
        }
    }

    fun getScore(): Int {
        return currentScore
    }

    fun onScoreSelected(score: Int) {
        val hideText = score == currentScore
        currentScore = if(hideText) {
            0
        } else {
            score
        }
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                binding.reviewEditableMediocreSmiley.deactivateMediocre(hideText)
                binding.reviewEditableExcellentSmiley.deactivateExcellent(hideText)
                if(hideText) {
                    binding.reviewEditableBadSmiley.hideSmileyText(true)
                }
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                binding.reviewEditableBadSmiley.deactivateBad(hideText)
                binding.reviewEditableExcellentSmiley.deactivateExcellent(hideText)
                if(hideText) {
                    binding.reviewEditableMediocreSmiley.hideSmileyText(true)
                }
            }
            // ReviewConstants.REPUTATION_SCORE_EXCELLENT
            else -> {
                binding.reviewEditableBadSmiley.deactivateBad(hideText)
                binding.reviewEditableMediocreSmiley.deactivateMediocre(hideText)
                if(hideText) {
                    binding.reviewEditableExcellentSmiley.hideSmileyText(true)
                }
            }
        }
    }

    fun showLoading() {
        binding.apply {
            reviewScoreLoadingSmiley.show()
            reviewScoreLoadingText.show()
            reviewScoreDeadlineLabel.hide()
            reviewEditableExcellentSmiley.hide()
            reviewEditableMediocreSmiley.hide()
            reviewEditableBadSmiley.hide()
        }
    }

    fun hideLoading() {
        binding.apply {
            reviewScoreLoadingSmiley.hide()
            reviewScoreLoadingText.hide()
        }
    }

    fun resetState() {
        binding.apply {
            reviewEditableBadSmiley.deactivateBad(true)
            reviewEditableMediocreSmiley.deactivateMediocre(true)
            reviewEditableExcellentSmiley.deactivateExcellent(true)
        }
    }

    private fun setDeadline() {
        binding.reviewScoreDeadlineLabel.show()
    }

    private fun setEmptyScore() {
        binding.reviewDetailScoreTitle.text = context.getString(R.string.review_history_details_score_empty)
    }

}