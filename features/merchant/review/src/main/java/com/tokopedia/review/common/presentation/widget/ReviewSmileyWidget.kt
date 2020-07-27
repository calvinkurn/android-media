package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
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

    private fun isActive(): Boolean {
        return isActive
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
        setAnimations()
        typedArray.recycle()
    }

    private fun setActiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableSmiley.apply {
                    loadImageDrawable(R.drawable.ic_smiley_bad_active)
                }
                setBadSmileyText()
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableSmiley.apply {
                    loadImageDrawable(R.drawable.ic_smiley_neutral_active)
                }
                setMediocreSmileyText()
            }
            else -> {
                this.reviewEditableSmiley.apply {
                    loadImageDrawable(R.drawable.ic_smiley_great_active)
                }
                setExcellentSmileyText()
            }
        }
    }

    private fun setInActiveImage(score: Int) {
        when (score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_bad_inactive)
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_neutral_inactive)
            }
            else -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_great_inactive)
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
        this.reviewEditableText.animate().alpha(0.0f)
    }

    private fun setAnimations() {
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