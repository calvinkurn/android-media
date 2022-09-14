package com.tokopedia.csat_rating.presenter

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
import com.tokopedia.csat_rating.presenter.screenState.*
import javax.inject.Inject

class BaseProvideRatingFragmentViewModel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatchers
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        const val FIRST_EMOJI = 1
        const val SECOND_EMOJI = 2
        const val THIRD_EMOJI = 3
        const val FOURTH_EMOJI = 4
        const val FIFTH_EMOJI = 5
    }
    
    private var captionsList = ArrayList<String>()
    private var questionList = ArrayList<String>()
    var emojiState : Int = 0
    var screenState = MutableLiveData<ScreenState>()
    var reasonList : ArrayList<BadCsatReasonListItem> = arrayListOf()

    fun setCaption(caption : ArrayList<String>){
        captionsList = caption
    }

    fun setQuestion(question : ArrayList<String>){
        questionList = question
    }

    fun setSelectedEmoji(selectedEmoji : Int){
        emojiState = selectedEmoji
        screenState.value = determineScreenSate(selectedEmoji)
    }

    fun setFilterList(itemsReasonList : ArrayList<BadCsatReasonListItem>){
        reasonList = itemsReasonList
    }

    private fun determineScreenSate(emoji: Int): ScreenState? {
        var screenState: ScreenState? = null
        when (emoji) {
            NO_EMOJI -> screenState = ZeroScreenState()
            FIRST_EMOJI -> {
                screenState = FirstScreenState(captionsList[FIRST_EMOJI - 1], questionList[FIRST_EMOJI - 1])
            }
            SECOND_EMOJI -> {
                screenState = SecondScreenState(captionsList[SECOND_EMOJI - 1], questionList[SECOND_EMOJI - 1])
            }
            THIRD_EMOJI -> {
                screenState = ThirdScreenState(captionsList[THIRD_EMOJI - 1], questionList[THIRD_EMOJI - 1])
            }
            FOURTH_EMOJI -> {
                screenState = FourthScreenState(captionsList[FOURTH_EMOJI - 1], questionList[FOURTH_EMOJI - 1])
            }
            FIFTH_EMOJI -> {
                screenState = FifthScreenState(captionsList[FIFTH_EMOJI - 1], questionList[FIFTH_EMOJI - 1])
            }
        }
        return screenState
    }

}