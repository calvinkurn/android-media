package com.tokopedia.product.manage.list.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.list.constant.GQL_UPDATE_PRODUCT
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Param
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class BulkUpdateProductUseCase @Inject constructor(@Named(GQL_UPDATE_PRODUCT) private val gqlQuery: String,
                                                   private val graphqlUseCase: GraphqlUseCase) : UseCase<List<ProductUpdateV3Response>>() {

    companion object {
        private const val PARAM_BULK_EDIT = "bulk_edit_param"
        private const val PARAM_INPUT = "input"

        @JvmStatic
        fun createRequestParams(paramData: List<ProductUpdateV3Param>): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_BULK_EDIT, paramData)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<ProductUpdateV3Response>> {
        val listResponse = requestParams.getObject(PARAM_BULK_EDIT) as List<ProductUpdateV3Param>

        return Observable.from(listResponse)
                .flatMap {
                    val variables = HashMap<String, Any>()
                    variables[PARAM_INPUT] = it
                    val graphqlRequest = GraphqlRequest(gqlQuery, ProductUpdateV3Response::class.java, variables)
                    graphqlUseCase.clearRequest()
                    graphqlUseCase.addRequest(graphqlRequest)

                    graphqlUseCase.createObservable(requestParams).flatMap { response ->
                        val data: ProductUpdateV3Response = response.getData(ProductUpdateV3Response::class.java)
                        Observable.just(data)
                    }.onErrorResumeNext { throwable ->
                        throwable.printStackTrace()
                        val data = ProductUpdateV3Response()
                        Observable.just(data)
                    }
                }.toList()
    }

}