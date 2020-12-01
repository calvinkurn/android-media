package com.tokopedia.talk.feature.smartreply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_talk_smart_reply_statistics.view.*

class TalkSmartReplyStatisticsWidget : BaseCustomView {

    companion object {
        const val TOTAL_QUESTION_THRESHOLD = 999
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
        View.inflate(context, R.layout.widget_talk_smart_reply_statistics, this)
    }

    fun setData(totalQuestion: Int, totalAnsweredBySmartReply: Int, replySpeed: Int) {
        if(totalQuestion > TOTAL_QUESTION_THRESHOLD) {
            talkSmartReplyTotalDiscussionCount.text = context.getString(R.string.smart_reply_total_discussions_format, totalQuestion)
        } else {
            talkSmartReplyTotalDiscussionCount.text = totalQuestion.toString()
        }
        talkSmartReplyAverageTime.text = context.getString(R.string.smart_reply_speed_format, replySpeed)
        talkSmartReplyPercentageReplied.text = context.getString(R.string.smart_reply_percentage_replied_format, totalAnsweredBySmartReply)
    }

}