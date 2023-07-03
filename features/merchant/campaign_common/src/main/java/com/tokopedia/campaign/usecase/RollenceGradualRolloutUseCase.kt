package com.tokopedia.campaign.usecase

import com.tokopedia.campaign.data.response.RollenceGradualRollout
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/*
Use this usecase if you want to use Rollence Gradual Rollout by using spesific ShopId as your input
The result of this UseCase is not saved into cache/ SharedPreferences
Sample case: There is a feature that needs a validation from Rollence Gradual Rollout Engine to
    decide whether the Seller/ Spesific Shop Id can access the feature or not
 */
class RollenceGradualRolloutUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<RollenceGradualRollout>() {

    companion object {
        val ID = "id"
        val REVISION = "rev"
        val CLIENT_ID = "client_id"
        val IRIS_SESSION_ID = "iris_session_id"
        val ANDROID_CLIENT_ID = 1

        @JvmStatic
        fun createParams(
            shopId: String,
            revision: Int? = 0,
            irisSessionId: String
        ) = mapOf(
            ID to shopId,
            REVISION to revision,
            CLIENT_ID to ANDROID_CLIENT_ID,
            IRIS_SESSION_ID to irisSessionId
        )
    }

    var params = mapOf<String, Any>()

    private val query = """query RolloutFeatureVariants(${'$'}rev: Int!, ${'$'}client_id: Int!, ${'$'}id: String, ${'$'}iris_session_id: String){
        RolloutFeatureVariants(rev:${'$'}rev, client_id:${'$'}client_id, id:${'$'}id, iris_session_id: ${'$'}iris_session_id){
         featureVariants {
           feature
           variant
         }
         globalRev
         status
        }
    }
    """.trimIndent()

    override suspend fun executeOnBackground(): RollenceGradualRollout {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        val gqlRequest = GraphqlRequest(query, RollenceGradualRollout::class.java)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(RollenceGradualRollout::class.java)
        } else {
            throw MessageErrorException(
                error.joinToString(", ") {
                    it.message
                }
            )
        }
    }
}
