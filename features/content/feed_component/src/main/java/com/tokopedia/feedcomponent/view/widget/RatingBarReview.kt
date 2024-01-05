package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import androidx.annotation.AttrRes
import android.util.AttributeSet
import android.view.View
import android.widget.RatingBar

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R

/**
 * Created by zulfikarrahman on 1/16/18.
 */

class RatingBarReview : BaseCustomView {
    private var numstars: Int = 0
    private var rating: Int = 0

    private var ratingBar: RatingBar? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        val styledAttributes = context.obtainStyledAttributes(attrs, com.tokopedia.content.common.R.styleable.RatingBarReview)
        try {
            numstars = styledAttributes.getInt(com.tokopedia.content.common.R.styleable.RatingBarReview_numstars, DEFAULT_INPUT_VALUE_NUM_STARS)
            rating = styledAttributes.getInt(com.tokopedia.content.common.R.styleable.RatingBarReview_rating, DEF_VALUE_EMPTY)
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setNumstars(numstars)
        updateRating(rating)
    }

    private fun init() {
        val view = View.inflate(context, R.layout.widget_rating_bar_review, this)
        ratingBar = view.findViewById(R.id.product_rating)
    }

    fun getNumstars(): Int {
        return numstars
    }

    fun setNumstars(numstars: Int) {
        this.numstars = numstars
        ratingBar?.numStars = numstars
    }

    fun updateRating(rating: Int) {
        this.rating = rating
        ratingBar?.rating = rating.toFloat()
    }

    companion object {
        private const val DEFAULT_INPUT_VALUE_NUM_STARS = 5
        private const val DEF_VALUE_EMPTY = 0
    }
}
