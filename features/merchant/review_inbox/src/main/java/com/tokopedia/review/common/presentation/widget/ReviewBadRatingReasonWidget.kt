package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReviewBadRatingReasonWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var badRatingReason: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_bad_rating_reason, this)
        badRatingReason = findViewById(R.id.review_bad_rating_reason)
    }

    fun showBadRatingReason(reason: String) {
        badRatingReason?.shouldShowWithAction(reason.isNotBlank()) {
            badRatingReason?.text = reason
        }
    }

    fun setTextToWhite() {
        badRatingReason?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White_96))
    }
}