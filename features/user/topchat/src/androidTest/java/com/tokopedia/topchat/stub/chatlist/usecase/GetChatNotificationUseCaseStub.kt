package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatlist.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.usecase.GetChatNotificationUseCase

class GetChatNotificationUseCaseStub(
        gqlUseCase: GraphqlUseCase<NotificationsPojo> = GraphqlUseCase(GraphqlInteractor.getInstance().graphqlRepository)
) : GetChatNotificationUseCase(gqlUseCase, TopchatAndroidTestCoroutineContextDispatcher()) {

    var response = NotificationsPojo()

    override fun getChatNotification(onSuccess: (NotificationsPojo) -> Unit, onError: (Throwable) -> Unit) {
        onSuccess(response)
    }
}