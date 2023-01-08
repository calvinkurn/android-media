package com.tokopedia.csat_rating.presenter.screenState


import com.tokopedia.csat_rating.R

class ThirdScreenState(private val mCaption: String, private val mQuestion: String) : ScreenState() {

    override fun getFirstEmoji(): Int {
        return getThirdEmoji()
    }

    override fun getSecondEmoji(): Int {
        return getThirdEmoji()
    }

    override fun getThirdEmoji(): Int {
        return R.drawable.emoji_active_3
    }

    override fun getMessage(): String {
        return mCaption
    }

    override fun getQuestion(): String {
        return mQuestion
    }

    override fun getMessageColor(): Int {
        return R.color.csat_dms_rating_active_3
    }
}
