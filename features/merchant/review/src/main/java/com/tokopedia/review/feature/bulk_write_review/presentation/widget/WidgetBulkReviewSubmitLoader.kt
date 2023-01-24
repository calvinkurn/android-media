package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewSubmitLoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    init {
        inflateWidget()
    }

    private fun inflateWidget() {
        View.inflate(context, R.layout.widget_bulk_review_submit_loader, this)
    }
}
