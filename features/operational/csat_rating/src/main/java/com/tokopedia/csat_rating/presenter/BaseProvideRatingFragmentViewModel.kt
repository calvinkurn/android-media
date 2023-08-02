package com.tokopedia.csat_rating.presenter

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
import com.tokopedia.csat_rating.presenter.screenState.FifthScreenState
import com.tokopedia.csat_rating.presenter.screenState.FirstScreenState
import com.tokopedia.csat_rating.presenter.screenState.FourthScreenState
import com.tokopedia.csat_rating.presenter.screenState.ScreenState
import com.tokopedia.csat_rating.presenter.screenState.SecondScreenState
import com.tokopedia.csat_rating.presenter.screenState.ThirdScreenState
import com.tokopedia.csat_rating.presenter.screenState.ZeroScreenState
import javax.inject.Inject

class BaseProvideRatingFragmentViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatchers
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        const val FIRST_EMOJI = 1L
        const val SECOND_EMOJI = 2L
        const val THIRD_EMOJI = 3L
        const val FOURTH_EMOJI = 4L
        const val FIFTH_EMOJI = 5L

        const val ZEROTH_INDEX = 0
        const val FIRST_INDEX = 1
        const val SECOND_INDEX = 2
        const val THIRD_INDEX = 3
        const val FOURTH_INDEX = 4
    }

    private var captionsList = ArrayList<String>()
    private var questionList = ArrayList<String>()
    var emojiState: Long = 0
        private set
    var screenState = MutableLiveData<ScreenState>()
        private set
    var reasonList: ArrayList<BadCsatReasonListItem> = arrayListOf()
        private set
    var csatTitle: String = ""
        private set

    fun setCsatTitle(title: String) {
        csatTitle = title
    }

    fun setCaption(caption: ArrayList<String>) {
        captionsList = caption
    }

    fun setQuestion(question: ArrayList<String>) {
        questionList = question
    }

    fun setSelectedEmoji(selectedEmoji: Long) {
        emojiState = selectedEmoji
        screenState.value = determineScreenSate(selectedEmoji)
    }

    fun setReasonList(itemsReasonList: ArrayList<BadCsatReasonListItem>) {
        reasonList = itemsReasonList
    }

    private fun determineScreenSate(emoji: Long): ScreenState {
        when (emoji) {
            NO_EMOJI -> return ZeroScreenState()
            FIRST_EMOJI -> {
                return FirstScreenState(captionsList[ZEROTH_INDEX], questionList[ZEROTH_INDEX])
            }
            SECOND_EMOJI -> {
                return SecondScreenState(captionsList[FIRST_INDEX], questionList[FIRST_INDEX])
            }
            THIRD_EMOJI -> {
                return ThirdScreenState(captionsList[SECOND_INDEX], questionList[SECOND_INDEX])
            }
            FOURTH_EMOJI -> {
                return FourthScreenState(captionsList[THIRD_INDEX], questionList[THIRD_INDEX])
            }
            FIFTH_EMOJI -> {
                return FifthScreenState(captionsList[FOURTH_INDEX], questionList[FOURTH_INDEX])
            }
            else -> {
                return ZeroScreenState()
            }
        }
    }
}
