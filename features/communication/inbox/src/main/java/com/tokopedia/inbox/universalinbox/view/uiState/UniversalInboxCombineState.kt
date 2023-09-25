package com.tokopedia.inbox.universalinbox.view.uiState

import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import com.tokopedia.inbox.universalinbox.util.Result

data class UniversalInboxCombineState(
    val menu: Result<UniversalInboxWrapperResponse?>,
    val counter: Result<UniversalInboxAllCounterResponse>,
    val driverChannel: Result<List<ConversationsChannel>>
)
