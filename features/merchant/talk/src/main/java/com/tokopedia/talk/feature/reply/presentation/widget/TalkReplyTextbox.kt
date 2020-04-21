package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyTextboxModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_reply_textbox.view.*

class TalkReplyTextbox : BaseCustomView {

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
        View.inflate(context, R.layout.widget_talk_reply_textbox, this)
    }

    fun bind(talkReplyTextboxModel: TalkReplyTextboxModel) {
        replyTextBox.textFieldIcon1.loadImage(talkReplyTextboxModel.userThumbNail)
    }
}