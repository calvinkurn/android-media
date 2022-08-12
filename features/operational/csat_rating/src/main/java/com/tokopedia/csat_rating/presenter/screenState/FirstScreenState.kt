package com.tokopedia.csat_rating.presenter.screenState

import com.tokopedia.csat_rating.R

class FirstScreenState(private val mCaption: String, private val mQuestion: String) : ScreenState() {


    override fun getFirstEmoji(): Int {
        return R.drawable.rating_active_1
    }

    override fun getMessageColor(): Int {
        return R.color.csat_dms_rating_active_1
    }

    override fun getMessage(): String {
        return mCaption
    }

    override fun getQuestion(): String {
        return mQuestion
    }
}
