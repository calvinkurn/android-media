package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TalkSmartReplyStatisticsWidget : BaseCustomView {

    companion object {
        const val TOTAL_QUESTION_THRESHOLD = 999
    }

    private var talkSmartReplyTotalDiscussionCount: Typography? = null
    private var talkSmartReplyAverageTime: Typography? = null
    private var talkSmartReplyPercentageReplied: Typography? = null

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
        View.inflate(context, R.layout.widget_talk_smart_reply_statistics, this)
        bindViews()
    }

    private fun bindViews() {
        talkSmartReplyTotalDiscussionCount = findViewById(R.id.talkSmartReplyTotalDiscussionCount)
        talkSmartReplyAverageTime = findViewById(R.id.talkSmartReplyAverageTime)
        talkSmartReplyPercentageReplied = findViewById(R.id.talkSmartReplyPercentageReplied)
    }

    fun setData(totalQuestion: String, totalAnsweredBySmartReply: String, replySpeed: String) {
        if(totalQuestion.toIntOrZero() > TOTAL_QUESTION_THRESHOLD) {
            talkSmartReplyTotalDiscussionCount?.text = "$totalQuestion+"
        } else {
            talkSmartReplyTotalDiscussionCount?.text = totalQuestion
        }
        talkSmartReplyAverageTime?.text = "${replySpeed}s"
        talkSmartReplyPercentageReplied?.text = "${totalAnsweredBySmartReply}%"
    }

}