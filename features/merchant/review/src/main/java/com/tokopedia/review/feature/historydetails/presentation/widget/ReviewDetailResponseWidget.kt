package com.tokopedia.review.feature.historydetails.presentation.widget

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponse
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.widget_review_detail_response.view.*

class ReviewDetailResponseWidget : BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_review_detail_response, this)
    }

    fun setContent(response: ProductrevGetReviewDetailResponse) {
        with(response) {
            if(shopName.isNotEmpty()) {
                reviewDetailResponderName.apply {
                    text = HtmlLinkHelper(context, context.getString(R.string.review_history_details_by, shopName)).spannedString
                    show()
                }
            }
            if(responseTimeFormatted.isNotEmpty()) {
                reviewDetailResponseDate.apply {
                    text = (context.getString(R.string.review_date, responseTimeFormatted))
                    show()
                }
            }
            reviewDetailResponseContent.apply {
                text = responseText
                show()
            }
        }
    }
}