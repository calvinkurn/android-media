package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.R
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

    fun setData(totalQuestion: String, totalAnsweredBySmartReply: String, replySpeed: String) {
        if(totalQuestion.toIntOrZero() > TOTAL_QUESTION_THRESHOLD) {
            talkSmartReplyTotalDiscussionCount.text = "$totalQuestion+"
        } else {
            talkSmartReplyTotalDiscussionCount.text = totalQuestion
        }
        talkSmartReplyAverageTime.text = "${replySpeed}s"
        talkSmartReplyPercentageReplied.text = "${totalAnsweredBySmartReply}%"
    }

}