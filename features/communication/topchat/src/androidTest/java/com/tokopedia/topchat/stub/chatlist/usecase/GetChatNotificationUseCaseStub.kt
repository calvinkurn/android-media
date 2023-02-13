package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import javax.inject.Inject

class GetChatNotificationUseCaseStub @Inject constructor(
        gqlUseCase: GraphqlUseCase<NotificationsPojo>
) : GetChatNotificationUseCase(gqlUseCase, CoroutineTestDispatchersProvider) {

    var response = NotificationsPojo()

    override fun getChatNotification(
        shopId: String,
        onSuccess: (NotificationsPojo) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        onSuccess(response)
    }
}