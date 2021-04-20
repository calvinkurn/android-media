package com.tokopedia.shop.showcase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcase
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcaseResponse
import com.tokopedia.shop.showcase.domain.model.GetFeaturedShowcaseRequestParams
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by Rafli Syam on 16/03/2021
 */
class GetFeaturedShowcaseUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<GetFeaturedShowcase>(graphqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): GetFeaturedShowcase {
        val request = GraphqlRequest(QUERY, GetFeaturedShowcaseResponse::class.java, params.parameters)
        val response = graphqlRepository.getReseponse(listOf(request))
        return response.getData<GetFeaturedShowcaseResponse>(
                GetFeaturedShowcaseResponse::class.java
        ).getFeaturedShowcase ?: throw MessageErrorException()
    }

    companion object {

        private const val INPUT = "input"

        fun createRequestParams(
            getFeaturedShowcaseRequestParams: GetFeaturedShowcaseRequestParams
        ): RequestParams = RequestParams.create().apply {
            putObject(INPUT, getFeaturedShowcaseRequestParams)
        }

        private const val QUERY = "query getFeaturedShowcase(\$input: ParamGetFeaturedShowcase!) {\n" +
                "  getFeaturedShowcase(input: \$input) {\n" +
                "    result {\n" +
                "      id\n" +
                "      name\n" +
                "      alias\n" +
                "      count\n" +
                "      uri\n" +
                "      imageURL\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

}