package com.tokopedia.review.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.review.inbox.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ReviewBadRatingDisclaimerWidget : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var disclaimer: Typography? = null

    private fun init() {
        View.inflate(context, R.layout.widget_review_bad_rating_disclaimer, this)
        disclaimer = findViewById(R.id.review_bad_rating_disclaimer_text)
    }

    fun setDisclaimer(disclaimer: String) {
        this.shouldShowWithAction(disclaimer.isNotBlank()) {
            this.disclaimer?.text = disclaimer
        }
    }
}