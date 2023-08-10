package com.tokopedia.inbox.universalinbox.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetWrapperResponse
import javax.inject.Inject

class UniversalInboxGetWidgetMetaUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, UniversalInboxWidgetWrapperResponse>(dispatcher.io) {
    override fun graphqlQuery(): String = """
        query GetChatInboxWidgetMeta {
          chatInboxWidgetMeta {
            metadata {
              icon
              title
              subtext
              androidApplink
              type
              isDynamic
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): UniversalInboxWidgetWrapperResponse {
        return repository.request(graphqlQuery(), params)
    }
}
