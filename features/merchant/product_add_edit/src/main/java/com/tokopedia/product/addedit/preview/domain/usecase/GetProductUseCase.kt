package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.data.source.api.param.GetProductV3Param
import com.tokopedia.product.addedit.preview.data.source.api.param.OptionV3
import com.tokopedia.product.addedit.preview.data.source.api.response.GetProductV3Response
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.manage.common.constant.GetProductV3QueryConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<Product>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Product {

        val graphqlRequest = GraphqlRequest(
                GetProductV3QueryConstant.getProductUseCaseQuery(),
                GetProductV3Response::class.java, params.parameters
        )
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors: List<GraphqlError>? = graphqlResponse.getError(GetProductV3Response::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<GetProductV3Response>(GetProductV3Response::class.java)
            return data.product
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_OPTIONS = "options"

        fun createRequestParams(productId: String): RequestParams {
            val options = OptionV3()
            val getProductV3Param = GetProductV3Param(productId, options)
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_PRODUCT_ID, getProductV3Param.productID)
            requestParams.putObject(PARAM_OPTIONS, getProductV3Param.options)
            return requestParams
        }
    }
}