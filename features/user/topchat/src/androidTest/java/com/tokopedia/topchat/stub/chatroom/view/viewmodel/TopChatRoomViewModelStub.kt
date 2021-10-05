package com.tokopedia.topchat.stub.chatroom.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.stub.chatroom.usecase.GetExistingMessageIdUseCaseNewStub
import javax.inject.Inject

class TopChatRoomViewModelStub @Inject constructor(
    getExistingMessageIdUseCase: GetExistingMessageIdUseCaseNewStub,
    dispatcher: CoroutineDispatchers,
    remoteConfig: RemoteConfig
): TopChatViewModel(
    getExistingMessageIdUseCase,
    dispatcher,
    remoteConfig
)