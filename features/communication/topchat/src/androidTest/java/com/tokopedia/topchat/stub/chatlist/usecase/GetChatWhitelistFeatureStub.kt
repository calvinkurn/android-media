package com.tokopedia.topchat.stub.chatlist.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.common.data.TopChatResult
import com.tokopedia.topchat.common.data.asFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChatWhitelistFeatureStub @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository
) : GetChatWhitelistFeature(repository, CoroutineTestDispatchersProvider) {

    var response = ChatWhitelistFeatureResponse()
    var isError = false
    var errorMessage = ""

    override suspend fun execute(params: String): Flow<TopChatResult<ChatWhitelistFeatureResponse>> {
        return flow {
            if (isError) {
                throw Throwable(errorMessage)
            } else {
                emit(response)
            }
        }.asFlowResult()
    }
}
