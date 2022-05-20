package com.tokopedia.chatbot.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class ChatbotViewModel @Inject constructor(
    private val ticketListContactUsUseCase : TicketListContactUsUsecase,
    private val dispatcher: CoroutineDispatchers,
) : BaseViewModel(dispatcher.main) {

    private val _ticketList = MutableLiveData<Result<InboxTicketListResponse>>()
    val ticketList : LiveData<Result<InboxTicketListResponse>>
        get() = _ticketList

    fun getTicketList(){
        launchCatchError(
            block = {
                val response = ticketListContactUsUseCase.getChatbotUploadPolicy()
                val ticketList = response.getData<InboxTicketListResponse>(InboxTicketListResponse::class.java)
                _ticketList.value = Success(ticketList)
            },
            onError = {
                _ticketList.value = Fail(it)
            }
        )
    }


}