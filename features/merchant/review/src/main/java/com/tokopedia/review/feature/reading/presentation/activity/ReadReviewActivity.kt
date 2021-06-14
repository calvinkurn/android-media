package com.tokopedia.review.feature.reading.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment

class ReadReviewActivity : BaseSimpleActivity() {

    private var productId: String = ""

    override fun getNewFragment(): Fragment? {
        val uri = intent.data
        productId = if (uri != null) {
            val segments = uri.pathSegments
            segments.getOrNull(segments.size - 2) ?: "0"
        } else ""
        return ReadReviewFragment.createNewInstance(productId)
    }

}