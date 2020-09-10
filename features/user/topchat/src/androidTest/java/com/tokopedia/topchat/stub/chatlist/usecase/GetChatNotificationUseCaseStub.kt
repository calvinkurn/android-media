package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatNotificationUseCase
import io.mockk.mockk

class GetChatNotificationUseCaseStub(
        gqlUseCase: GraphqlUseCase<NotificationsPojo> = mockk()
) : GetChatNotificationUseCase(gqlUseCase, TopchatAndroidTestCoroutineContextDispatcher()) {

    var response = NotificationsPojo()

    override fun getChatNotification(onSuccess: (NotificationsPojo) -> Unit, onError: (Throwable) -> Unit) {
        onSuccess(response)
    }
}