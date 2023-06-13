package com.tokopedia.chatbot.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import javax.inject.Inject

class ChatbotViewModel @Inject constructor(
    private val ticketListContactUsUseCase: TicketListContactUsUsecase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _ticketList = MutableLiveData<TicketListState>()
    val ticketList: LiveData<TicketListState>
        get() = _ticketList

    fun getTicketList() {
        ticketListContactUsUseCase.cancelJobs()
        ticketListContactUsUseCase.getTicketList(
            ::onTicketListDataSuccess,
            ::onTicketListDataFail
        )
    }

    private fun onTicketListDataSuccess(inboxTicketListResponse: InboxTicketListResponse) {
        inboxTicketListResponse.ticket?.TicketData?.notice?.let {
            if (it.isActive) {
                _ticketList.postValue(TicketListState.BottomSheetData(it))
            } else {
                _ticketList.postValue(TicketListState.ShowContactUs)
            }
        } ?: kotlin.run {
            _ticketList.postValue(TicketListState.ShowContactUs)
        }
    }

    private fun onTicketListDataFail(throwable: Throwable) {
        _ticketList.postValue(TicketListState.ShowContactUs)
    }

    override fun onCleared() {
        ticketListContactUsUseCase.cancelJobs()
        super.onCleared()
    }
}

