package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.databinding.WidgetBulkReviewSubmitLoaderBinding
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewSubmitLoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val IMAGE_URL = "https://images.tokopedia.net/img/android/review/ic_bulk_review_submit_loader_illustration.png"
    }

    init {
        WidgetBulkReviewSubmitLoaderBinding.inflate(
            LayoutInflater.from(context), this, true
        ).initViews()
    }

    private fun WidgetBulkReviewSubmitLoaderBinding.initViews() {
        ivBulkReviewSubmitLoader.loadImage(IMAGE_URL)
    }
}
