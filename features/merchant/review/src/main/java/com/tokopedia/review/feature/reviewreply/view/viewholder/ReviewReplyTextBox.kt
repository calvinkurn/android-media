package com.tokopedia.review.feature.reviewreply.view.viewholder

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_reply_textbox.view.*

class ReviewReplyTextBox : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_reply_textbox, this)
    }

    fun setReplyAction() {
        replyEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    replySendButton?.isEnabled = true
                    replySendButton?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cta_send_active))
                } else {
                    replySendButton?.isEnabled = false
                    replySendButton?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cta_send))
                }
            }
        })
    }

    fun clickAddTemplate(listener: () -> Unit) {
        btnAddTemplate?.setOnClickListener {
            listener.invoke()
        }
    }
}