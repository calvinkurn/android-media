package com.tokopedia.csat_rating.presenter

import android.content.Intent
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.csat_rating.ProvideRatingContract
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
import com.tokopedia.csat_rating.presenter.screenState.*
import java.util.*

class BaseProvideRatingFragmentPresenter : BaseDaggerPresenter<ProvideRatingContract.ProvideRatingView>(), ProvideRatingContract.ProvideRatingPresenter {
    private var captionsList = ArrayList<String>()
    private var questionList = ArrayList<String>()
    private var emojiState = 0

    companion object {
        const val EMOJI_STATE = "emoji_state"
        const val SELECTED_ITEM = "selected_items"
        const val FIRST_EMOJI = 1
        const val SECOND_EMOJI = 2
        const val THIRD_EMOJI = 3
        const val FOURTH_EMOJI = 4
        const val FIFTH_EMOJI = 5
    }

    override fun attachView(view: ProvideRatingContract.ProvideRatingView) {
        super.attachView(view)
        captionsList = getView().getCaption()
        questionList = getView().getQuestion()
        emojiState = getView().getSelectedEmoji()
        updateScreenState()
        if (emojiState == 0) {
            getView().setFilterList(ArrayList())
            getView().hideSubmitButton()
        } else {
            setFilterList()
        }
    }

    private fun setFilterList() {
        view.setFilterList(view.getReasonList())
        view.showSubmitButton()
    }

    fun getScreenState(emoji: Int): ScreenState? {
        var screenState: ScreenState? = null
        when (emoji) {
            NO_EMOJI -> screenState = ZeroScreenState()
            FIRST_EMOJI -> {
                screenState = FirstScreenState(captionsList[FIRST_EMOJI - 1], questionList[FIRST_EMOJI - 1])
                setFilterList()
            }
            SECOND_EMOJI -> {
                screenState = SecondScreenState(captionsList[SECOND_EMOJI - 1], questionList[SECOND_EMOJI - 1])
                setFilterList()
            }
            THIRD_EMOJI -> {
                screenState = ThirdScreenState(captionsList[THIRD_EMOJI - 1], questionList[THIRD_EMOJI - 1])
                setFilterList()
            }
            FOURTH_EMOJI -> {
                screenState = FourthScreenState(captionsList[FOURTH_EMOJI - 1], questionList[FOURTH_EMOJI - 1])
                setFilterList()
            }
            FIFTH_EMOJI -> {
                screenState = FifthScreenState(captionsList[FIFTH_EMOJI - 1], questionList[FIFTH_EMOJI - 1])
                setFilterList()
            }
        }
        return screenState

    }

    fun updateScreenState() {
        view.clearEmoji()
        val screenState = getScreenState(emojiState)
        screenState?.let {
            view.setFirstEmoji(it.getFirstEmoji())
            view.setSecondEmoji(it.getSecondEmoji())
            view.setThirdEmoji(it.getThirdEmoji())
            view.setFourthEmoji(it.getFourthEmoji())
            view.setFifthEmoji(it.getFifthEmoji())
            view.setMessage(it.getMessage())
            view.setMessageColor(it.getMessageColor())
            view.setQuestion(it.getQuestion())
        }

    }


    override fun onFirstEmojiClick() {
        emojiState = FIRST_EMOJI
        updateScreenState()

    }

    override fun onSecondEmojiClick() {
        emojiState = SECOND_EMOJI
        updateScreenState()
    }

    override fun onThirdEmojiClick() {
        emojiState = THIRD_EMOJI
        updateScreenState()
    }

    override fun onFourthEmojiClick() {
        emojiState = FOURTH_EMOJI
        updateScreenState()
    }

    override fun onFifthEmojiClick() {
        emojiState = FIFTH_EMOJI
        updateScreenState()
    }

    override fun onSubmitClick() {
        val intent = Intent()
        intent.putExtra(EMOJI_STATE, emojiState)
        intent.putExtra(SELECTED_ITEM, view.getSelectedItem())
        view.onSuccessSubmit(intent)
    }

    override fun detachView() {
        super.detachView()
    }

}
