package com.tokopedia.csat_rating.presenter.screenState

import com.tokopedia.csat_rating.R

class FifthScreenState(private val mCaption: String, private val mQuestion: String) : ScreenState() {

    override fun getFirstEmoji(): Int {
        return getFifthEmoji()
    }

    override fun getSecondEmoji(): Int {
        return getFifthEmoji()
    }

    override fun getThirdEmoji(): Int {
        return getFifthEmoji()
    }

    override fun getFourthEmoji(): Int {
        return getFifthEmoji()
    }

    override fun getFifthEmoji(): Int {
        return R.drawable.emoji_active_5
    }

    override fun getMessageColor(): Int {
        return R.color.csat_dms_rating_active_5
    }

    override fun getMessage(): String {
        return mCaption
    }

    override fun getQuestion(): String {
        return mQuestion
    }
}
