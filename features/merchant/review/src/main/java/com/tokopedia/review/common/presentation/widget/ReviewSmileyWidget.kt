package com.tokopedia.review.common.presentation.widget

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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

    fun showActiveBad() {
        this.isActive = true
        this.reviewEditableImageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_active))
        }
        showSmileyText()
    }

    fun showActiveMediocre() {
        this.isActive = true
        this.reviewEditableImageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_active))
        }
        showSmileyText()
    }

    fun showActiveExcellent() {
        this.isActive = true
        this.reviewEditableImageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_active))
        }
        showSmileyText()
    }

    fun deactivateBad(hideText: Boolean) {
        if(isActive) {
            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION_REVERSE, true)
            hideSmileyText(hideText)
            isActive = false
        }
    }

    fun deactivateMediocre(hideText: Boolean) {
        if(isActive) {
            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION_REVERSE, true)
            hideSmileyText(hideText)
            isActive = false
        }
    }

    fun deactivateExcellent(hideText: Boolean) {
        if(isActive) {
            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION_REVERSE, true)
            hideSmileyText(hideText)
            isActive = false
        }
    }

    fun showSmileyText() {
        this.reviewEditableText.apply {
            show()
            animate().alpha(1f)
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
            initActiveState(score)
            showSmileyText()
        } else {
            setInactiveImage(score)
        }
        this.reviewEditableSmiley.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // No Op
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (isActive) {
                    setActiveImage(score)
                } else {
                    setInactiveImage(score)
                }
                reviewEditableImageView.show()
                reviewEditableSmiley.hide()
            }

            override fun onAnimationCancel(animation: Animator?) {
                // No Op
            }

            override fun onAnimationStart(animation: Animator?) {
                reviewEditableImageView.hide()
                if (isActive) {
                    showSmileyText()
                }
            }
        })
        typedArray.recycle()
    }

    private fun setActiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                showActiveBad()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                showActiveMediocre()
            }
            else -> {
                showActiveExcellent()
            }
        }
    }

    private fun setInactiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_inactive))
                setBadSmileyText()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_inactive))
                setMediocreSmileyText()
            }
            else -> {
                this.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_inactive))
                setExcellentSmileyText()
            }
        }
    }

    fun hideSmileyText(hideText: Boolean) {
        this.reviewEditableText.apply {
            animate().alpha(0f)
            if(hideText) {
                hide()
            }
        }
    }

    private fun setAnimations(reviewScoreClickListener: ReviewScoreClickListener) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableImageView.apply {
                    setOnClickListener {
                        val shouldAnimate = reviewScoreClickListener.onReviewScoreClicked(score)
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION, shouldAnimate)
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(BAD_SMILEY_ANIMATION_REVERSE, shouldAnimate)
                        }
                    }
                }
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableImageView.apply {
                    setOnClickListener {
                        val shouldAnimate = reviewScoreClickListener.onReviewScoreClicked(score)
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION, shouldAnimate)
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(MEDIOCRE_SMILEY_ANIMATION_REVERSE, shouldAnimate)
                        }
                    }
                }
            }
            else -> {
                this.reviewEditableImageView.apply {
                    setOnClickListener {
                        val shouldAnimate = reviewScoreClickListener.onReviewScoreClicked(score)
                        if(!isActive) {
                            isActive = true
                            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION, shouldAnimate)
                        } else {
                            isActive = false
                            setLottieAnimationFromUrl(EXCELLENT_SMILEY_ANIMATION_REVERSE, shouldAnimate)
                        }
                    }
                }
            }
        }
    }

    private fun setLottieAnimationFromUrl(animationUrl: String, shouldAnimate: Boolean) {
        if(!shouldAnimate) {
            return
        }
        context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)

            lottieCompositionLottieTask.addListener { result ->
                this.reviewEditableSmiley.apply {
                    this.reviewEditableSmiley.setComposition(result)
                    show()
                    this.reviewEditableSmiley.playAnimation()
                }
            }

            lottieCompositionLottieTask.addFailureListener { throwable -> }
        }
    }

    private fun initActiveState(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                showActiveBad()
                setBadSmileyText()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                showActiveMediocre()
                setMediocreSmileyText()
            }
            else -> {
                showActiveExcellent()
                setExcellentSmileyText()
            }
        }
    }

    private fun setBadSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_bad)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }
    }

    private fun setMediocreSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_mediocre)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300))
        }
    }

    private fun setExcellentSmileyText() {
        this.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_excellent)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        }
    }
}