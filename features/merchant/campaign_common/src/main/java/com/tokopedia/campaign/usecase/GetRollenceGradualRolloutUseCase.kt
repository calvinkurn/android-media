package com.tokopedia.campaign.usecase

import com.tokopedia.campaign.data.request.GetRollenceGradualRolloutRequest
import com.tokopedia.campaign.data.response.RollenceGradualRollout
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/*
Use this usecase if you want to use Rollence Gradual Rollout by using spesific ShopId as your input
The result of this UseCase is not saved into cache/ SharedPreferences
Sample case: There is a feature that needs a validation from Rollence Gradual Rollout Engine to
    decide whether the Seller/ Spesific Shop Id can access the feature or not
 */
class GetRollenceGradualRolloutUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<RollenceGradualRollout>(repository) {

    companion object {
        val ID = "id"
        val REVISION = "rev"
        val CLIENT_ID = "client_id"
        val IRIS_SESSION_ID = "iris_session_id"
        val ANDROID_CLIENT_ID = 1
        const val QUERY_NAME = "RolloutFeatureVariants"
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY = """query RolloutFeatureVariants(${'$'}rev: Int!, ${'$'}client_id: Int!, ${'$'}id: String, ${'$'}iris_session_id: String){
        RolloutFeatureVariants(rev:${'$'}rev, client_id:${'$'}client_id, id:${'$'}id, iris_session_id: ${'$'}iris_session_id){
            featureVariants {
                feature
                variant
            }
            globalRev
            status
        }
        }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(params: Param): RollenceGradualRollout {
        val request = buildRequest(params)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<RollenceGradualRollout>()
        return data
    }

    private fun buildRequest(
        params: Param
    ): GraphqlRequest {
        val payload = GetRollenceGradualRolloutRequest(
            id = params.id,
            rev = params.rev,
            client_id = params.client_id,
            iris_session_id = params.iris_session_id
        )

        val requestParams = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            RolloutFeatureVariants(),
            RollenceGradualRollout::class.java,
            requestParams
        )
    }

    data class Param(
        val iris_session_id: String,
        val id: String,
        val client_id: Int = ANDROID_CLIENT_ID,
        val rev: Int? = 0
    )
}
