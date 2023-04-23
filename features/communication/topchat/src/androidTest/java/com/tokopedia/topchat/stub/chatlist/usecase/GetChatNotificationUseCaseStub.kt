package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import javax.inject.Inject

class GetChatNotificationUseCaseStub @Inject constructor(
    @ApplicationContext gqlRepo: GraphqlRepository
) : GetChatNotificationUseCase(gqlRepo, CoroutineTestDispatchersProvider) {
    var response = NotificationsPojo()
    override suspend fun execute(params: String): NotificationsPojo {
        return response
    }
}
