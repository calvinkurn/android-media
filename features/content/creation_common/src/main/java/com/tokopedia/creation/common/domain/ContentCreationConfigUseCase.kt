package com.tokopedia.creation.common.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.data.ContentCreationResponse
import com.tokopedia.creation.common.presentation.model.ContentCreationConfigModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 08/09/23
 */
class ContentCreationConfigUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, ContentCreationConfigModel>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query FeedXHeader(${'$'}$PARAM_FIELDS: [String!]!) {
          feedXHeader(req: {fields: ${'$'}$PARAM_FIELDS}) {
            data {
              ...FeedXHeaderData
            }
            error
          }
        }
        
        fragment FeedXHeaderData on FeedXHeaderData {
          creation {
            ...FeedXHeaderCreation
          }
        }
        
        fragment FeedXHeaderCreation on FeedXHeaderCreation {
          isActive
          image
          statistic {
            applink
          }
          authors {
            id
            name
            type
            image
            hasUsername
            hasAcceptTnC
            items {
              isActive
              type
              title
              image
              weblink
              applink
              media{
                type
                id
                coverURL
                mediaURL
              }
              descriptionCTA{
                applink
              }
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(params: Unit): ContentCreationConfigModel {
        val response =
            graphqlRepository.request<Map<String, Any>, ContentCreationResponse.Response>(
                graphqlQuery(),
                mapOf(
                    PARAM_FIELDS to listOf(PARAM_CREATION)
                )
            ).response

        if (response.error.isNotEmpty()) {
            throw MessageErrorException(response.error)
        }

        return response.data.creation.toConfigModel()
    }

    companion object {
        private const val PARAM_FIELDS = "fields"

        private const val PARAM_CREATION = "creation"
    }
}
