package com.tokopedia.review.common.presentation.widget

import com.tokopedia.imageassets.TokopediaImageUrl

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.util.ReviewScoreClickListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.databinding.WidgetReviewSmileyBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewSmileyWidget : BaseCustomView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setView(attrs, defStyleAttr)
    }

    companion object {
        private const val BAD_SMILEY_ANIMATION = TokopediaImageUrl.BAD_SMILEY_ANIMATION
        private const val MEDIOCRE_SMILEY_ANIMATION = TokopediaImageUrl.MEDIOCRE_SMILEY_ANIMATION
        private const val EXCELLENT_SMILEY_ANIMATION = TokopediaImageUrl.EXCELLENT_SMILEY_ANIMATION
        private const val BAD_SMILEY_ANIMATION_REVERSE = TokopediaImageUrl.BAD_SMILEY_ANIMATION_REVERSE
        private const val MEDIOCRE_SMILEY_ANIMATION_REVERSE = TokopediaImageUrl.MEDIOCRE_SMILEY_ANIMATION_REVERSE
        private const val EXCELLENT_SMILEY_ANIMATION_REVERSE = TokopediaImageUrl.EXCELLENT_SMILEY_ANIMATION_REVERSE
    }

    private var isActive = false
    private var score = 0
    
    private val binding = WidgetReviewSmileyBinding.inflate(LayoutInflater.from(context), this, true)

    fun showActiveBad() {
        this.isActive = true
        binding.reviewEditableImageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_active))
        }
        showSmileyText()
    }

    fun showActiveMediocre() {
        this.isActive = true
        binding.reviewEditableImageView.apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_active))
        }
        showSmileyText()
    }

    fun showActiveExcellent() {
        this.isActive = true
        binding.reviewEditableImageView.apply {
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
        binding.reviewEditableText.apply {
            show()
            animate().alpha(1f)
        }
    }

    fun setSmileyClickListener(reviewScoreClickListener: ReviewScoreClickListener) {
        setAnimations(reviewScoreClickListener)
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
        binding.reviewEditableSmiley.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
                // No Op
            }

            override fun onAnimationEnd(animation: Animator) {
                if (isActive) {
                    setActiveImage(score)
                } else {
                    setInactiveImage(score)
                }
                binding.reviewEditableImageView.show()
                binding.reviewEditableSmiley.hide()
            }

            override fun onAnimationCancel(animation: Animator) {
                // No Op
            }

            override fun onAnimationStart(animation: Animator) {
                binding.reviewEditableImageView.hide()
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
                binding.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_bad_inactive))
                setBadSmileyText()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                binding.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_neutral_inactive))
                setMediocreSmileyText()
            }
            else -> {
                binding.reviewEditableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_smiley_great_inactive))
                setExcellentSmileyText()
            }
        }
    }

    fun hideSmileyText(hideText: Boolean) {
        binding.reviewEditableText.apply {
            animate().alpha(0f)
            if(hideText) {
                hide()
            }
        }
    }

    private fun setAnimations(reviewScoreClickListener: ReviewScoreClickListener) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                binding.reviewEditableImageView.apply {
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
                binding.reviewEditableImageView.apply {
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
                binding.reviewEditableImageView.apply {
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
                binding.reviewEditableSmiley.apply {
                    binding.reviewEditableSmiley.setComposition(result)
                    show()
                    binding.reviewEditableSmiley.playAnimation()
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
        binding.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_bad)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
        }
    }

    private fun setMediocreSmileyText() {
        binding.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_mediocre)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300))
        }
    }

    private fun setExcellentSmileyText() {
        binding.reviewEditableText.apply {
            text = context.getString(R.string.review_detail_score_excellent)
            setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        }
    }
}
