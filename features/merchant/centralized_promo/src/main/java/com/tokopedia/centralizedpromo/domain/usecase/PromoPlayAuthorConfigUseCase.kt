package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.model.PromoPlayAuthorConfigResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import timber.log.Timber
import javax.inject.Inject

private const val QUERY = """
    query PromoPlayAuthorConfigQuery(
        ${'$'}authorID: Int64!
    ) {
      broadcasterGetAuthorConfig(
         authorID: ${'$'}authorID, 
         authorType: 2, 
         withChannelState: true
      ) {
        hasContent
      }
    }
"""

@GqlQuery("PromoPlayAuthorConfigQuery", QUERY)
class PromoPlayAuthorConfigUseCase @Inject constructor(
    repository: GraphqlRepository
): GraphqlUseCase<PromoPlayAuthorConfigResponse>(repository) {

    init {
        setGraphqlQuery(PromoPlayAuthorConfigQuery())
        setTypeClass(PromoPlayAuthorConfigResponse::class.java)
    }

    suspend fun execute(shopId: String): Boolean {
        return try {
            setRequestParams(
                createRequestParams(shopId).parameters
            )
            executeOnBackground().authorConfig.hasContent
        } catch (ex: Exception) {
            Timber.e(ex)
            false
        }
    }

    companion object {

        private const val AUTHOR_ID_KEY = "authorID"

        @JvmStatic
        fun createRequestParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putString(AUTHOR_ID_KEY, shopId)
            }
        }

    }

}
