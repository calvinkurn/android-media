package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.data.model.params.add.ProductAddParam
import com.tokopedia.product.addedit.preview.data.model.responses.ProductAddEditV3Response
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ProductAddUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) :
        UseCase<ProductAddEditV3Response>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductAddEditV3Response {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = params.getObject(PARAM_INPUT)
        val gqlRequest = GraphqlRequest(getQuery(), ProductAddEditV3Response::class.java, variables)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))
        val gqlErrors = gqlResponse.getError(GraphqlError::class.java) ?: listOf()
        if (gqlErrors.isNullOrEmpty()) {
            val data: ProductAddEditV3Response =
                    gqlResponse.getData<ProductAddEditV3Response>(ProductAddEditV3Response::class.java)
            if (data.productAddEditV3Data.isSuccess) {
                return data
            } else {
                val exceptionMessages = data.productAddEditV3Data.header.errorMessage
                throw MessageErrorException(exceptionMessages.joinToString(STRING_JOIN_SEPARATOR))
            }
        } else {
            throw MessageErrorException(gqlErrors.first().message)
        }
    }

    companion object {
        const val PARAM_INPUT = "input"
        const val STRING_JOIN_SEPARATOR = "\n"
        const val QUERY_NAME = "ProductAddV3" // for tracking purpose

        @JvmStatic
        fun createRequestParams(param: ProductAddParam): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_INPUT, param)
            return requestParams
        }

        fun getQuery() = """
                    mutation ProductAddV3(${'$'}input: ProductInputV3!) {
                      ProductAddV3(input: ${'$'}input) {
                        header {
                          messages
                          reason
                          errorCode
                        }
                        isSuccess
                      }
                    }
                    """.trimIndent()
    }

}