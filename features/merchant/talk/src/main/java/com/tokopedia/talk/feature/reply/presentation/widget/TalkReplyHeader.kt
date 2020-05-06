package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.OnKebabClickedListener
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyHeaderListener
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.widget_talk_reply_header.view.*

class TalkReplyHeader : BaseCustomView {

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
        View.inflate(context, R.layout.widget_talk_reply_header, this)
    }

    fun bind(talkReplyHeaderModel: TalkReplyHeaderModel,
             talkReplyHeaderListener: TalkReplyHeaderListener,
             onKebabClickedListener: OnKebabClickedListener) {
        talkReplyHeaderModel.apply {
            showQuestionWithCondition(isMasked, question, maskedContent)
            showKebabWithConditions(allowReport, allowDelete, onKebabClickedListener)
            showFollowWithCondition(allowFollow, isFollowed, talkReplyHeaderListener)
            replyHeaderDate.text = date
        }
        replyHeaderTNC.setOnClickListener {
            talkReplyHeaderListener.onTermsAndConditionsClicked()
        }

    }

    fun setButtonToFollowed() {
        replyHeaderFollowButton.text = context.getString(R.string.reply_header_following_chip)
        replyHeaderFollowButton.buttonType = UnifyButton.Type.ALTERNATE
    }

    fun setButtonToUnfollowed() {
        replyHeaderFollowButton.text = context.getString(R.string.reply_header_follow_chip)
        replyHeaderFollowButton.buttonType = UnifyButton.Type.MAIN
    }

    private fun showQuestionWithCondition(isMasked: Boolean, question: String, maskedContent: String) {
        replyHeaderMessage.apply {
            if(isMasked) {
                text = maskedContent
                isEnabled = false
                return
            }
            text = question
        }
    }

    private fun showKebabWithConditions(allowReport: Boolean, allowDelete: Boolean, onKebabClickedListener: OnKebabClickedListener) {
        if(allowReport || allowDelete){
            replyHeaderKebab.setOnClickListener {
                onKebabClickedListener.onKebabClicked("", allowReport, allowDelete)
            }
            replyHeaderKebab.visibility = View.VISIBLE
        }
    }

    private fun showFollowWithCondition(allowFollow: Boolean, isFollowed: Boolean, talkReplyHeaderListener: TalkReplyHeaderListener) {
        if(allowFollow) {
            replyHeaderFollowButton.setOnClickListener {
                talkReplyHeaderListener.onFollowUnfollowButtonClicked()
            }
            if(isFollowed) {
                setButtonToFollowed()
            } else {
                setButtonToUnfollowed()
            }
            replyHeaderFollowButton.visibility = View.VISIBLE
        }
    }


}