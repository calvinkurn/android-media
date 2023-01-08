package com.tokopedia.contactus.inboxticket2.view.viewModel

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

class ContactUsRatingViewModel @Inject constructor(
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
    private set
    var csatTitle : String = ""
        private set
    var screenState = MutableLiveData<ScreenState>()
    private set
    var reasonList : ArrayList<BadCsatReasonListItem> = arrayListOf()
    private set

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

    fun setReasonList(itemsReasonList : ArrayList<BadCsatReasonListItem>){
        reasonList = itemsReasonList
    }

    fun setCsatTitle(title: String) {
        csatTitle = title
    }

    private fun determineScreenSate(emoji: Int): ScreenState {
        when (emoji) {
            NO_EMOJI -> return ZeroScreenState()
            FIRST_EMOJI -> {
                return FirstScreenState(captionsList[FIRST_EMOJI - 1], questionList[FIRST_EMOJI - 1])
            }
            SECOND_EMOJI -> {
                return SecondScreenState(captionsList[SECOND_EMOJI - 1], questionList[SECOND_EMOJI - 1])
            }
            THIRD_EMOJI -> {
                return ThirdScreenState(captionsList[THIRD_EMOJI - 1], questionList[THIRD_EMOJI - 1])
            }
            FOURTH_EMOJI -> {
                return FourthScreenState(captionsList[FOURTH_EMOJI - 1], questionList[FOURTH_EMOJI - 1])
            }
            FIFTH_EMOJI -> {
                return FifthScreenState(captionsList[FIFTH_EMOJI - 1], questionList[FIFTH_EMOJI - 1])
            }
            else -> {
                return ZeroScreenState()
            }
        }
    }

}
