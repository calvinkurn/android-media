package com.tokopedia.reviewcommon.feature.reviewer.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.reviewcommon.R

class ShopReviewBasicInfoWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ProductReviewBasicInfoWidget(context, attrs, defStyleAttr) {
    override fun inflateView() {
        View.inflate(context, R.layout.widget_shop_review_basic_info, this)
    }
}