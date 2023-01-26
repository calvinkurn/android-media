package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewSubmitLoader(
    context: Context,
    attrs: AttributeSet?
) : BaseCustomView(context, attrs) {

    init {
        inflateWidget()
    }

    private fun inflateWidget() {
        View.inflate(context, R.layout.widget_bulk_review_submit_loader, this)
    }
}
