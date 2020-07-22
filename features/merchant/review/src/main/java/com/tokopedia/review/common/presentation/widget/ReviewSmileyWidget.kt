package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
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

    fun activate() {

    }

    fun deactivate() {

    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_smiley, this)
    }

    private fun setView(attrs: AttributeSet, defStyleAttr: Int = 0) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.ReviewSmileyWidget, defStyleAttr, 0)
        val isActive = typedArray.getBoolean(R.styleable.ReviewSmileyWidget_isSelected, false)
        val score = typedArray.getInt(R.styleable.ReviewSmileyWidget_score, 0)
        if(isActive) {
            setActiveImage(score)
        } else {
            setInActiveImage(score)
        }
        typedArray.recycle()
    }

    private fun setActiveImage(score: Int) {
        when(score) {
            ReviewConstants.REPUTATION_SCORE_BAD -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_bad_active)
                this.reviewEditableText.apply {
                    text = context.getString(R.string.review_detail_score_bad)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y500))
                }
            }
            ReviewConstants.REPUTATION_SCORE_MEDIOCRE -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_neutral_active)
                this.reviewEditableText.apply {
                    text = context.getString(R.string.review_detail_score_mediocre)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Yellow_Y300))
                }
            }
            else -> {
                this.reviewEditableSmiley.loadImageDrawable(R.drawable.ic_smiley_great_active)
                this.reviewEditableText.apply {
                    text = context.getString(R.string.review_detail_score_excellent)
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500))
                }
            }
        }
        this.reviewEditableText.show()
    }

    private fun setInActiveImage(score: Int) {
        when(score) {
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

    private fun setOnClickListener() {
        // Animation
    }
}