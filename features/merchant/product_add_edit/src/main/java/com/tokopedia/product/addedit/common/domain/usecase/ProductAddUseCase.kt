package com.tokopedia.product.addedit.common.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.domain.model.params.add.ProductAddParam
import com.tokopedia.product.addedit.common.domain.model.params.edit.ProductEditPriceParam
import com.tokopedia.product.addedit.common.domain.model.responses.ProductAddEditV3Response
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

        val errors: List<GraphqlError>? = gqlResponse.getError(ProductAddEditV3Response::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ProductAddEditV3Response::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val PARAM_INPUT = "input"
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