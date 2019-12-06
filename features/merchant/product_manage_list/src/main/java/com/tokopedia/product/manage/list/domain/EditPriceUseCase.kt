package com.tokopedia.product.manage.list.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.list.constant.GQL_UPDATE_PRODUCT
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductEditPriceParam
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class EditPriceUseCase @Inject constructor(@Named(GQL_UPDATE_PRODUCT) private val gqlQuery: String,
                                           private val graphqlUseCase: GraphqlUseCase) : UseCase<ProductUpdateV3Response>() {
    companion object {
        const val PARAM_EDIT = "param_edit"
        const val PARAM_INPUT = "input"
        @JvmStatic
        fun createRequestParams(param: ProductEditPriceParam): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_EDIT, param)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ProductUpdateV3Response> {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = requestParams.getObject(PARAM_EDIT)
        val graphqlRequest = GraphqlRequest(gqlQuery, ProductUpdateV3Response::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ProductUpdateV3Response? = it.getData(ProductUpdateV3Response::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            } else if (data.productUpdateV3Data.header.errorMessage.isNotEmpty()) {
                throw  MessageErrorException(data.productUpdateV3Data.header.errorMessage.first())
            }

            data
        }
    }

}