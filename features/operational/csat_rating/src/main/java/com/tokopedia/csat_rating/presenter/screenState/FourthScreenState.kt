package com.tokopedia.csat_rating.presenter.screenState

import com.tokopedia.csat_rating.R

class FourthScreenState(private val mCaption: String, private val mQuestion: String) : ScreenState() {

    override fun getFirstEmoji(): Int {
        return getFourthEmoji()
    }

    override fun getSecondEmoji(): Int {
        return getFourthEmoji()
    }

    override fun getThirdEmoji(): Int {
        return getFourthEmoji()
    }

    override fun getFourthEmoji(): Int {
        return R.drawable.emoji_active_4
    }

    override fun getMessageColor(): Int {
        return R.color.csat_dms_message_color_good
    }

    override fun getMessage(): String {
        return mCaption
    }

    override fun getQuestion(): String {
        return mQuestion
    }
}
