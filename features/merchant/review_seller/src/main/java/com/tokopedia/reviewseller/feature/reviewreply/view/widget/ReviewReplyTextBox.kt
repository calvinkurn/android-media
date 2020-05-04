package com.tokopedia.reviewseller.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.reviewseller.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_reply_textbox.view.*

class ReviewReplyTextBox : BaseCustomView {

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
        View.inflate(context, R.layout.widget_reply_textbox, null)
    }

    fun setReplyAction() {
        replySendButton?.setOnClickListener {

        }
    }

}