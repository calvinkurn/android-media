package com.tokopedia.csat_rating.presenter.screenState


import com.tokopedia.csat_rating.R

class SecondScreenState(private val mCaption: String, private val mQuestion: String) : ScreenState() {

    override fun getFirstEmoji(): Int {
        return getSecondEmoji()

    }

    override fun getSecondEmoji(): Int {
        return R.drawable.emoji_active_2
    }

    override fun getMessage(): String {
        return mCaption
    }

    override fun getQuestion(): String {
        return mQuestion
    }
}
