package com.tokopedia.reviewseller.feature.reviewreply.view.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.PaddingItemDecoratingReviewSeller
import com.tokopedia.reviewseller.feature.reviewreply.view.adapter.ReviewTemplateListAdapter
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_reply_textbox.view.*

class ReviewReplyTextBox : BaseCustomView {

    private val reviewTemplateListAdapter by lazy {
        ReviewTemplateListAdapter()
    }

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
        View.inflate(context, R.layout.widget_reply_textbox, this)
    }

    fun setReplyAction() {
//        setTemplateList()

        replyEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0) {
                    replySendButton?.isEnabled = true
                    replySendButton?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cta_send_active))
                } else {
                    replySendButton?.isEnabled = false
                    replySendButton?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cta_send))
                }
            }
        })

        replySendButton?.setOnClickListener {

        }
    }

    private fun setTemplateList(data: List<ReplyTemplateUiModel>) {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        list_template?.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecoratingReviewSeller())
            }
            adapter = reviewTemplateListAdapter
        }
        if (data.isEmpty()) {
            list_template?.hide()
        } else {
            reviewTemplateListAdapter.submitList(data)
            list_template?.show()
        }
    }

}