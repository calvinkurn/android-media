package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class GetTickerDataState {
    data class HandleTickerDataFailure(val error: Throwable) : GetTickerDataState()
    data class SuccessTickerData(val data: com.tokopedia.chatbot.chatbot2.data.TickerData.TickerData) : GetTickerDataState()
}
