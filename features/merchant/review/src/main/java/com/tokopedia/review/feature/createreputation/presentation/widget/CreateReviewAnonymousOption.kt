package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify


class CreateReviewAnonymousOption : BaseCustomView {

    private var checkbox: CheckboxUnify? = null

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
        View.inflate(context, R.layout.widget_create_review_anonymous_option, this)
        checkbox = findViewById(R.id.review_form_anonymous_checkbox)
    }

    fun isChecked(): Boolean {
        return checkbox?.isChecked ?: false
    }
}