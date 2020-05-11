package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyProductHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyProductHeaderListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_reply_product_header.view.*

class TalkReplyProductHeader : BaseCustomView {

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
        View.inflate(context, R.layout.widget_talk_reply_product_header, this)
    }

    fun bind(talkReplyProductHeaderModel: TalkReplyProductHeaderModel, talkReplyProductHeaderListener: TalkReplyProductHeaderListener) {
        with(talkReplyProductHeaderModel) {
            replyProductHeaderImage.apply {
                loadImage(thumbnail)
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductClicked()
                }
            }
            replyProductHeaderName.apply {
                text = productName
                setOnClickListener {
                    talkReplyProductHeaderListener.onProductClicked()
                }
            }
        }
    }

}