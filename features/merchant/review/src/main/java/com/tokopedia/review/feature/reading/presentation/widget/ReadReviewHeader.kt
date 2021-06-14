package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductRating
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewHeaderListener
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewHeader : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private var rating: Typography? = null
    private var satisfactionRate: Typography? = null
    private var ratingAndReviewCount: Typography? = null
    private var chevron: IconUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_header, this)
        bindViews()
    }

    private fun bindViews() {
        rating = findViewById(R.id.read_review_overall_rating)
        satisfactionRate = findViewById(R.id.read_review_satisfaction_rate)
        ratingAndReviewCount = findViewById(R.id.read_review_rating_and_review_count)
        chevron = findViewById(R.id.read_review_header_chevron_right)
    }

    fun setData(productRating: ProductRating) {
        rating?.text = productRating.ratingScore
        this.satisfactionRate?.text = productRating.satisfactionRate
        this.ratingAndReviewCount?.text = context.getString(R.string.review_reading_rating_and_review_count, productRating.totalRating, productRating.totalRatingTextAndImage)
    }

    fun setListener(readReviewHeaderListener: ReadReviewHeaderListener) {
        chevron?.setOnClickListener {
            readReviewHeaderListener.onChevronClicked()
        }
    }
}