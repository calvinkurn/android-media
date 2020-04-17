package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.talk.feature.reply.presentation.uimodel.TalkReplyHeaderModel
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.widget_talk_reply_header.view.*

class TalkReplyHeader : BaseCustomView{

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
             onTermsAndConditionsClickedListener: OnTermsAndConditionsClickedListener,
             onKebabClickedListener: OnKebabClickedListener) {
        this.replyHeaderDate.text = talkReplyHeaderModel.date
        this.replyHeaderMessage.text = talkReplyHeaderModel.question
        this.replyHeaderTNC.setOnClickListener {
            onTermsAndConditionsClickedListener.onTermsAndConditionsClicked()
        }
        this.replyHeaderKebab.setOnClickListener {
            onKebabClickedListener.onKebabClicked()
        }
        if(talkReplyHeaderModel.isFollowed) {
            setButtonToFollowed()
            return
        }
        setButtonToUnfollowed()
    }

    fun setButtonToFollowed() {
        this.replyHeaderFollowButton.text = FOLLOWING_TEXT
        this.replyHeaderFollowButton.buttonType = UnifyButton.Type.ALTERNATE
    }

    fun setButtonToUnfollowed() {
        this.replyHeaderFollowButton.text = UNFOLLOWED_TEXT
        this.replyHeaderFollowButton.buttonType = UnifyButton.Type.MAIN
    }


}