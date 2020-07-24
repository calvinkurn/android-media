package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_review_detail_score.view.*

class ReviewScoreWidget : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private var currentScore = 0

    fun setEditableScore(score: Int, lockTime: String) {
        currentScore = score
        when(score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableBadSmiley.apply {
                    show()
                }
                setDeadline(lockTime)
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableMediocreSmiley.apply {
                    show()
                }
                setDeadline(lockTime)
            }
            else -> {
                setEmptyScore()
            }
        }
        this.reviewEditableBadSmiley.show()
        this.reviewEditableMediocreSmiley.show()
        this.reviewEditableGreatSmiley.show()
    }

    fun setShopName(shopName: String) {
        this.reviewDetailScoreShopName.setTextAndCheckShow(shopName)
    }

    fun setExpired() {
        this.reviewDetailScoreTitle.text = context.getString(R.string.review_detail_score_expired)
    }

    fun setFinalScore(score: Int) {
        when(score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewDetailScoreSmiley.loadImageDrawable(R.drawable.ic_smiley_bad_active)
                this.reviewDetailScoreText.apply {
                    text = context.getString(R.string.review_detail_score_bad)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y500))
                }
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewDetailScoreSmiley.loadImageDrawable(R.drawable.ic_smiley_neutral_active)
                this.reviewDetailScoreText.apply {
                    text = context.getString(R.string.review_detail_score_mediocre)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y300))
                }
            }
            // ReviewConstants.REPUTATION_SCORE_EXCELLENT
            else -> {
                this.reviewDetailScoreSmiley.loadImageDrawable(R.drawable.ic_smiley_great_active)
                this.reviewDetailScoreText.apply {
                    text = context.getString(R.string.review_detail_score_excellent)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500))
                }
            }
        }
        reviewDetailScoreSmiley.show()
        reviewDetailScoreText.show()
    }

    fun getScore(): Int {
        return currentScore
    }

    private fun setDeadline(deadline: String) {
        if(deadline.isNotBlank()) {
            this.reviewScoreDeadline.apply {
                text = deadline
                show()
            }
            this.reviewScoreDeadlineLabel.show()
        }
    }

    private fun setEmptyScore() {
        this.reviewDetailScoreTitle.text = context.getString(R.string.review_history_details_score_empty)
    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_detail_score, this)
    }

}