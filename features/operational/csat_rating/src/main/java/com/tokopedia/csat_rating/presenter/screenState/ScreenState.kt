package com.tokopedia.csat_rating.presenter.screenState

import com.tokopedia.csat_rating.R

abstract class ScreenState {

    open fun getFirstEmoji(): Int {
        return  R.drawable.emoji_inactive_1
    }

    open fun getSecondEmoji(): Int {
        return R.drawable.emoji_inactive_2
    }

    open fun getThirdEmoji(): Int {
        return R.drawable.emoji_inactive_3
    }

    open fun getFourthEmoji(): Int {
        return R.drawable.emoji_inactive_4
    }

    open fun getFifthEmoji(): Int {
        return R.drawable.emoji_inactive_5
    }

    open fun getMessage(): String {
        return ""
    }

    open fun getMessageColor(): Int {
        return R.color.csat_dms_message_color_bad
    }

    open fun getQuestion(): String {
        return ""
    }

}
