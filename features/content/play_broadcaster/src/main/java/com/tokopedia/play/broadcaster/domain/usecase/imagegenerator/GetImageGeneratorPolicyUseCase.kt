package com.tokopedia.play.broadcaster.domain.usecase.imagegenerator

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.imagegenerator.GetImageGeneratorPolicyResponse
import com.tokopedia.play.broadcaster.domain.usecase.imagegenerator.GetImageGeneratorPolicyUseCase.Companion.QUERY_GET_IMAGE_GENERATOR_POLICY
import com.tokopedia.play.broadcaster.domain.usecase.imagegenerator.GetImageGeneratorPolicyUseCase.Companion.QUERY_NAME
import javax.inject.Inject

@GqlQuery(QUERY_NAME, QUERY_GET_IMAGE_GENERATOR_POLICY)
class GetImageGeneratorPolicyUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetImageGeneratorPolicyResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetImageGeneratorPolicyUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetImageGeneratorPolicyResponse::class.java)
    }

    suspend fun execute(): GetImageGeneratorPolicyResponse {
        this.createRequestParams()
        return executeOnBackground()
    }

    private fun createRequestParams(sourceId: String = VALUE_SOURCE_ID) {
        val request = mapOf(
            PARAMS_SOURCE_ID to sourceId,
        )
        setRequestParams(request)
    }

    companion object {
        private const val PARAMS_SOURCE_ID = "sourceID"
        private const val VALUE_SOURCE_ID = "LScDrk"
        const val QUERY_NAME = "GetImageGeneratorPolicyUseCaseQuery"
        const val QUERY_GET_IMAGE_GENERATOR_POLICY = """
            query ImageGeneratorPolicy(${"$${PARAMS_SOURCE_ID}"}: String!) {
              imagenerator_policy($PARAMS_SOURCE_ID: ${"$${PARAMS_SOURCE_ID}"}) {
                args {
                  key
                  type
                  required
                }
              }
            }
        """
    }
}
