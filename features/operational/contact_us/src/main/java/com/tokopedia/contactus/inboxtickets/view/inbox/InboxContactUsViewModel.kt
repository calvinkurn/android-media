package com.tokopedia.contactus.inboxtickets.view.inbox

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.ChipTopBotStatusUseCase
import com.tokopedia.contactus.inboxtickets.domain.usecase.GetTicketListUseCase
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxConstanta.SUCCESS_HIT_API
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxFilterSelection
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxState
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxUiEffect
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class InboxContactUsViewModel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val topBotStatusUseCase: ChipTopBotStatusUseCase,
    private val getTickets: GetTicketListUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        const val ALL = 0
        const val IN_PROGRESS = 1
        const val NEED_RATING = 2
        const val CLOSED = 3
        const val TICKET_ON_GIVE_RATE = 1
        const val TICKET_ON_CLOSE = 2
        const val ALL_OPTION_POSITION = 0
    }

    private var optionsFilter: List<InboxFilterSelection> = arrayListOf()

    private val _uiState = MutableStateFlow(InboxState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<InboxUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: InboxState
        get() = _uiState.value

    fun setOptionsFilter(options: List<InboxFilterSelection>) {
        optionsFilter = options
    }

    fun getOptionsFilter(): List<InboxFilterSelection> {
        return optionsFilter
    }

    fun getItemTicketOnPosition(position: Int): InboxTicketListResponse.Ticket.Data.TicketItem {
        return currentState.ticketItems[position]
    }

    fun markOptionsFilterWithSelected(options: List<InboxFilterSelection>) {
        optionsFilter = options
        restartStateTickets(true)
    }

    fun findMarkOptions(): InboxFilterSelection {
        return optionsFilter.first { it.isSelected }
    }

    fun autoPickShowAllOptionsFilter() {
        val currentSelected = findMarkOptions()
        optionsFilter[ALL_OPTION_POSITION].isSelected = true
        optionsFilter[currentSelected.id].isSelected = false
    }

    fun getTopBotStatus() {
        launchCatchError(
            block = {
                val topBotStatusResponse = topBotStatusUseCase.getChipTopBotStatus()
                if (topBotStatusResponse.getTopBotStatusInbox().getTopBotStatusData().isSuccess == SUCCESS_HIT_API && topBotStatusResponse.getTopBotStatusInbox().getTopBotStatusData().isActive) {
                    val messageId =
                        topBotStatusResponse.getTopBotStatusInbox().getTopBotStatusData().getMessageID()
                    val welcomeMessage =
                        topBotStatusResponse.getTopBotStatusInbox().getTopBotStatusData().getMessageWelcome()
                    val isHasUnreadNotif =
                        topBotStatusResponse.getTopBotStatusInbox().getTopBotStatusData().unreadNotif
                    _uiState.update {
                        it.copy(
                            idMessage = messageId,
                            welcomeMessage = welcomeMessage,
                            unReadNotification = isHasUnreadNotif,
                            showChatBotWidget = true
                        )
                    }
                } else {
                    InboxState(
                        showChatBotWidget = false
                    )
                }
            },
            onError = {
                InboxState(
                    showChatBotWidget = false
                )
                it.printStackTrace()
            }
        )
    }

    fun restartPageOfList() {
        _uiState.update {
            it.copy(
                ticketItems = arrayListOf(),
                offset = 0
            )
        }
    }

    fun getTicketItems() {
        val filterSelected = findMarkOptions()
        when (filterSelected.id) {
            ALL -> {
                val requestParams = getTickets.getRequestParams(
                    currentState.offset,
                    ALL
                )
                getTicketList(requestParams)
            }
            IN_PROGRESS -> {
                val requestParams = getTickets.getRequestParams(
                    currentState.offset,
                    IN_PROGRESS
                )
                getTicketList(requestParams)
            }
            NEED_RATING -> {
                val requestParams = getTickets.getRequestParams(
                    currentState.offset,
                    NEED_RATING, TICKET_ON_GIVE_RATE
                )
                getTicketList(requestParams)
            }
            else -> {
                val requestParams = getTickets.getRequestParams(
                    currentState.offset,
                    NEED_RATING, TICKET_ON_CLOSE
                )

                getTicketList(requestParams)
            }
        }
    }

    private fun getTicketList(requestParams: RequestParams) {
        launchCatchError(
            block = {
                val ticketListResponse = getTickets.getTicketListResponse(requestParams)
                val ticketData = ticketListResponse.getTicketOnInbox().getTicket()
                when {
                    ticketData.getTicketList().isNotEmpty() -> {
                        val isHasNextPage = ticketData.getNextUrl().isNotEmpty()
                        val isFirstPage = ticketData.getPrevUrl().isEmpty()
                        val ticketItems =
                            currentState.ticketItems + ticketData.getTicketList()
                        val currentOffset = currentState.offset + 1
                        _uiEffect.emit(
                            InboxUiEffect.LoadNextPageSuccess(
                                isFirstPage = isFirstPage,
                                isHasNext = isHasNextPage,
                                allItems = ticketItems,
                                currentPageItems = ticketData.getTicketList()
                            )
                        )
                        _uiState.update {
                            it.copy(
                                offset = currentOffset,
                                ticketItems = ticketItems
                            )
                        }
                    }
                    else -> {
                        _uiEffect.emit(
                            InboxUiEffect.EmptyTicket(userSession.name, currentState.isFilteredData)
                        )
                        restartStateTickets()
                    }
                }
            },
            onError = {
                _uiEffect.emit(
                    InboxUiEffect.FetchInboxError(it)
                )
            }
        )
    }

    private fun restartStateTickets(isFilteredData: Boolean = false) {
        val ticketItems = arrayListOf<InboxTicketListResponse.Ticket.Data.TicketItem>()
        val currentOffset = 0
        _uiState.update {
            it.copy(
                offset = currentOffset,
                ticketItems = ticketItems,
                isFilteredData = isFilteredData
            )
        }
    }

}
