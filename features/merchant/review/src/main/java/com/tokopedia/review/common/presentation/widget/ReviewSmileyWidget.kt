package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_review_smiley.view.*

class ReviewSmileyWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        setView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setView(attrs, defStyleAttr)
    }

    companion object {
        private const val BAD_SMILEY_ANIMATION = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_bad.json"
        private const val MEDIOCRE_SMILEY_ANIMATION = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_mediocre.json"
        private const val EXCELLENT_SMILEY_ANIMATION = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_excellent.json"
        private const val BAD_SMILEY_ANIMATION_REVERSE = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_bad_reverse.json"
        private const val MEDIOCRE_SMILEY_ANIMATION_REVERSE = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_mediocre_reverse.json"
        private const val EXCELLENT_SMILEY_ANIMATION_REVERSE = "https://ecs7.tokopedia.net/android/reputation/reputation_smiley_excellent_reverse.json"
    }

    private var isActive = false
    private var score = 0

    fun setActiveBad() {
        this.isActive = true
        this.reviewEditableSmiley.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_active))
        }
        setBadSmileyText()
    }

    fun setActiveMediocre() {
        this.isActive = true
        this.reviewEditableSmiley.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_active))
        }
        setMediocreSmileyText()
    }

    fun deactivateBad() {
        if(isActive) {
            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION_REVERSE)
            this.reviewEditableSmiley.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_inactive))
            }
            hideSmileyText()
            isActive = false
        }
    }

    fun deactivateMediocre() {
        if(isActive) {
            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION_REVERSE)
            this.reviewEditableSmiley.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_inactive))
            }
            hideSmileyText()
            isActive = false
        }
    }

    fun deactivateExcellent() {
        if(isActive) {
            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION_REVERSE)
            this.reviewEditableSmiley.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_inactive))
            }
            hideSmileyText()
            isActive = false
        }
    }

    fun setSmileyClickListener(reviewScoreClickListener: ReviewScoreClickListener) {
        setAnimations(reviewScoreClickListener)
    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_smiley, this)
    }

    private fun setView(attrs: AttributeSet, defStyleAttr: Int = 0) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ReviewSmileyWidget, defStyleAttr, 0)
        isActive = typedArray.getBoolean(R.styleable.ReviewSmileyWidget_isSelected, false)
        score = typedArray.getInt(R.styleable.ReviewSmileyWidget_score, 0)
        if (isActive) {
            setActiveImage(score)
        } else {
            setInActiveImage(score)
        }
        typedArray.recycle()
    }

    private fun setActiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                setActiveBad()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                setActiveMediocre()
            }
            else -> {
                this.reviewEditableSmiley.apply {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_active))
                }
                setExcellentSmileyText()
            }
        }
    }

    private fun setInActiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableSmiley.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_inactive))
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableSmiley.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_inactive))
            }
            else -> {
                this.reviewEditableSmiley.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_inactive))
            }
        }
    }

    private fun setBadSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_bad)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y500))
            animate().alpha(1.0f)
        }
    }

    private fun setMediocreSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_mediocre)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y300))
            animate().alpha(1.0f)
        }
    }

    private fun setExcellentSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_excellent)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500))
            animate().alpha(1.0f)
        }
    }

    private fun hideSmileyText() {
        this.reviewEditableText.apply {
            animate().alpha(0.0f)
        }
    }

    private fun setAnimations(reviewScoreClickListener: ReviewScoreClickListener) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableSmiley.apply {
                    setOnClickListener {
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION)
                            setBadSmileyText()
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION_REVERSE)
                            hideSmileyText()
                        }
                        reviewScoreClickListener.onReviewScoreClicked(score)
                    }
                }
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableSmiley.apply {
                    setOnClickListener {
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION)
                            setMediocreSmileyText()
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION_REVERSE)
                            hideSmileyText()
                        }
                        reviewScoreClickListener.onReviewScoreClicked(score)
                    }
                }
            }
            else -> {
                this.reviewEditableSmiley.apply {
                    setOnClickListener {
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION)
                            setExcellentSmileyText()
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION_REVERSE)
                            hideSmileyText()
                        }
                        reviewScoreClickListener.onReviewScoreClicked(score)
                    }
                }
            }
        }
    }

    private fun setLottieAnimationFromUrl(animationUrl: String) {
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)

            lottieCompositionLottieTask.addListener { result ->
                this.reviewEditableSmiley.setComposition(result)
                this.reviewEditableSmiley.playAnimation()
            }

            lottieCompositionLottieTask.addFailureListener { throwable -> }
        }
    }

}