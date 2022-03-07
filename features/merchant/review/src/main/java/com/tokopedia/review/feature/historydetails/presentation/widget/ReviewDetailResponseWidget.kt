package com.tokopedia.review.feature.historydetails.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.data.ProductrevGetReviewDetailResponse
import com.tokopedia.review.databinding.WidgetReviewDetailResponseBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.HtmlLinkHelper

class ReviewDetailResponseWidget : BaseCustomView {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private val binding = WidgetReviewDetailResponseBinding.inflate(LayoutInflater.from(context), this, true)

    fun setContent(response: ProductrevGetReviewDetailResponse) {
        binding.reviewDetailResponseTab.background = ContextCompat.getDrawable(context, R.drawable.rectangle_8)
        with(response) {
            if(shopName.isNotEmpty()) {
                binding.reviewDetailResponderName.apply {
                    text = HtmlLinkHelper(context, context.getString(R.string.review_history_details_by, shopName)).spannedString
                    show()
                }
            }
            if(responseTimeFormatted.isNotEmpty()) {
                binding.reviewDetailResponseDate.apply {
                    text = (context.getString(R.string.review_date, responseTimeFormatted))
                    show()
                }
            }
            binding.reviewDetailResponseContent.apply {
                text = responseText
                show()
            }
        }
    }
}