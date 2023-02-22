package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.usecase.coroutines.UseCase

class MPSFirstPageUseCase(
    private val graphqlUseCase: MultiRequestGraphqlUseCase,
): UseCase<MPSModel>() {

    override suspend fun executeOnBackground(): MPSModel = graphqlUseCase.run {
        clearRequest()
        addRequests(listOf(
            mpsRequest()
        ))

        val graphqlResponse = executeOnBackground()

        MPSModel(
            graphqlResponse.getData(MPSModel.AceSearchShop::class.java),
        )
    }

    @GqlQuery("MPSFirstPageQuery", MPS_GQL_QUERY)
    private fun mpsRequest(): GraphqlRequest {
        val requestParams = useCaseRequestParams

        return GraphqlRequest(
            MPSFirstPageQuery(),
            MPSModel.AceSearchShop::class.java,
            requestParams.parameters
        )
    }

    companion object {
        private const val MPS_GQL_QUERY = """
            query MPS(${'$'}params: String!) {
                aceSearchShop(params: ${'$'}params) {
                    shops {
                        shop_id
                    }
                }
            }
        """
    }
}
