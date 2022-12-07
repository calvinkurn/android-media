package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.databinding.WidgetBulkReviewSubmitLoaderBinding
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewSubmitLoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
    init {
        WidgetBulkReviewSubmitLoaderBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }
}
