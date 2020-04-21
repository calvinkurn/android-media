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

    companion object {
        const val FOLLOWING_TEXT = "Following"
        const val UNFOLLOWED_TEXT = "Follow"
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
        View.inflate(context, R.layout.widget_talk_reply_header, this)
    }

    fun bind(talkReplyHeaderModel: TalkReplyHeaderModel,
             talkReplyHeaderListener: TalkReplyHeaderListener,
             onKebabClickedListener: OnKebabClickedListener) {
        replyHeaderDate.text = talkReplyHeaderModel.date
        replyHeaderMessage.text = talkReplyHeaderModel.question
        replyHeaderTNC.setOnClickListener {
            talkReplyHeaderListener.onTermsAndConditionsClicked()
        }
        replyHeaderKebab.setOnClickListener {
            onKebabClickedListener.onKebabClicked()
        }
        replyHeaderFollowButton.setOnClickListener {
            talkReplyHeaderListener.onFollowUnfollowButtonClicked()
        }
        if(talkReplyHeaderModel.isFollowed) {
            setButtonToFollowed()
            return
        }
        setButtonToUnfollowed()
    }

    fun setButtonToFollowed() {
        replyHeaderFollowButton.text = FOLLOWING_TEXT
        replyHeaderFollowButton.buttonType = UnifyButton.Type.ALTERNATE
    }

    fun setButtonToUnfollowed() {
        replyHeaderFollowButton.text = UNFOLLOWED_TEXT
        replyHeaderFollowButton.buttonType = UnifyButton.Type.MAIN
    }


}