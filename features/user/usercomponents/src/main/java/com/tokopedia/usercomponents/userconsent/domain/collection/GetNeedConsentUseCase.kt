package com.tokopedia.usercomponents.userconsent.domain.collection

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usercomponents.userconsent.common.ConsentCollectionResponse
import javax.inject.Inject

/**
 * Use this class to show/hide consent view before widget creation
 * For example: Show/hide bottom sheet with consent widget
 */
class GetNeedConsentUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<ConsentCollectionParam, Boolean>(dispatchers.io) {

    override suspend fun execute(params: ConsentCollectionParam): Boolean {
        val result = repository.request<ConsentCollectionParam, ConsentCollectionResponse>(
            graphqlQuery(),
            params
        )
        return if (result.data.success) {
            // If error, should not show the consent widget
            result.data.collectionPoints.firstOrNull()?.needConsent ?: false
        } else {
            throw MessageErrorException(result.data.errorMessages.joinToString())
        }
    }

    override fun graphqlQuery(): String = """
            query GetCollectionPointWithConsent(${'$'}collectionID: String!, ${'$'}version: String) {
              GetCollectionPointWithConsent(collectionID: ${'$'}collectionID, version: ${'$'}version) {
                success
                errorMessages
                collectionPoints {
                  needConsent
                }  
              }
            }
    """.trimIndent()
}
