package com.tokopedia.csat_rating

import android.content.Intent
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

interface ProvideRatingContract {
    interface ProvideRatingView : CustomerView {

        fun setFirstEmoji(drawable: Int)
        fun setSecondEmoji(drawable: Int)
        fun setThirdEmoji(drawable: Int)
        fun setFourthEmoji(drawable: Int)
        fun setFifthEmoji(drawable: Int)
        fun setMessage(message: String)
        fun setMessageColor(color: Int)
        fun setQuestion(question: String)
        fun getSelectedEmoji(): Int
        fun clearEmoji()
        fun showErrorMessage(errorMessage: String)
        fun setFilterList(filterList: List<BadCsatReasonListItem>)
        fun getSelectedItem(): String
        fun getReasonList(): ArrayList<BadCsatReasonListItem>
        fun onSuccessSubmit(intent: Intent)
        fun getCaption(): ArrayList<String>
        fun getQuestion(): ArrayList<String>
        fun showProgress()
        fun hideProgress()
        fun hideSubmitButton()
        fun showSubmitButton()
        fun disableSubmitButton()
        fun enableSubmitButton()
    }

    interface ProvideRatingPresenter : CustomerPresenter<ProvideRatingView> {

        fun onFirstEmojiClick()
        fun onSecondEmojiClick()
        fun onThirdEmojiClick()
        fun onFourthEmojiClick()
        fun onFifthEmojiClick()
        fun onSubmitClick()

    }
}
