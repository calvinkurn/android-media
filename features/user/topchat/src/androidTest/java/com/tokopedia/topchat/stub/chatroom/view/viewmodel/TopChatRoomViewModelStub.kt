package com.tokopedia.topchat.stub.chatroom.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.stub.chatroom.usecase.GetExistingMessageIdUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetShopFollowingUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.ToggleFavouriteShopUseCaseStub
import javax.inject.Inject

class TopChatRoomViewModelStub @Inject constructor(
    getExistingMessageIdUseCase: GetExistingMessageIdUseCaseStub,
    getShopFollowingUseCase: GetShopFollowingUseCaseStub,
    toggleFavouriteShopUseCase: ToggleFavouriteShopUseCaseStub,
    dispatcher: CoroutineDispatchers,
    remoteConfig: RemoteConfig
): TopChatViewModel(
    getExistingMessageIdUseCase,
    getShopFollowingUseCase,
    toggleFavouriteShopUseCase,
    dispatcher,
    remoteConfig
)