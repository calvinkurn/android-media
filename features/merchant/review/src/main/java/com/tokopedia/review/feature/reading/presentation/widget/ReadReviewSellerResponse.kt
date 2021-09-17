package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReadReviewSellerResponse : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var shopName: Typography? = null
    private var timeStamp: Typography? = null
    private var response: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_seller_response, this)
        bindViews()
    }

    private fun bindViews() {
        shopName = findViewById(R.id.review_reading_respondent_name)
        timeStamp = findViewById(R.id.review_reading_response_timestamp)
        response = findViewById(R.id.review_reading_respondent_answer)
    }

    fun setResponseData(shopName: String, timeStamp: String, response: String) {
        this.shopName?.text = shopName
        this.timeStamp?.text = timeStamp
        this.response?.text = response
    }

}