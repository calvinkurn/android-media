package com.tokopedia.contactus.inboxtickets.view.viewModel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIFTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIRST_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIRST_INDEX
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FOURTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FOURTH_INDEX
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.SECOND_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.SECOND_INDEX
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.THIRD_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.THIRD_INDEX
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.ZEROTH_INDEX
import com.tokopedia.csat_rating.presenter.screenState.FifthScreenState
import com.tokopedia.csat_rating.presenter.screenState.FirstScreenState
import com.tokopedia.csat_rating.presenter.screenState.FourthScreenState
import com.tokopedia.csat_rating.presenter.screenState.ScreenState
import com.tokopedia.csat_rating.presenter.screenState.SecondScreenState
import com.tokopedia.csat_rating.presenter.screenState.ThirdScreenState
import com.tokopedia.csat_rating.presenter.screenState.ZeroScreenState
import javax.inject.Inject

class ContactUsRatingViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatchers
) : BaseViewModel(coroutineDispatcherProvider.main) {

    private var captionsList = emptyList<String>()
    private var questionList = emptyList<String>()
    var emojiState: Long = 0
        private set
    var csatTitle: String = ""
        private set
    var screenState = MutableLiveData<ScreenState>()
        private set
    var reasonList: ArrayList<BadCsatReasonListItem> = arrayListOf()
        private set

    fun setCaption(caption: List<String>) {
        captionsList = caption
    }

    fun setQuestion(question: List<String>) {
        questionList = question
    }

    fun setSelectedEmoji(selectedEmoji: Long) {
        emojiState = selectedEmoji
        screenState.value = determineScreenSate(selectedEmoji)
    }

    fun setReasonList(itemsReasonList: ArrayList<BadCsatReasonListItem>) {
        reasonList = itemsReasonList
    }

    fun setCsatTitle(title: String) {
        csatTitle = title
    }

    private fun determineScreenSate(emoji: Long): ScreenState {
        when (emoji) {
            NO_EMOJI -> return ZeroScreenState()
            FIRST_EMOJI -> {
                return FirstScreenState(
                    captionsList[ZEROTH_INDEX],
                    questionList[ZEROTH_INDEX]
                )
            }
            SECOND_EMOJI -> {
                return SecondScreenState(
                    captionsList[FIRST_INDEX],
                    questionList[FIRST_INDEX]
                )
            }
            THIRD_EMOJI -> {
                return ThirdScreenState(
                    captionsList[SECOND_INDEX],
                    questionList[SECOND_INDEX]
                )
            }
            FOURTH_EMOJI -> {
                return FourthScreenState(
                    captionsList[THIRD_INDEX],
                    questionList[THIRD_INDEX]
                )
            }
            FIFTH_EMOJI -> {
                return FifthScreenState(
                    captionsList[FOURTH_INDEX],
                    questionList[FOURTH_INDEX]
                )
            }
            else -> {
                return ZeroScreenState()
            }
        }
    }
}
