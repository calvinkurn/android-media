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
        private const val PARAM_LIST_ID = "list_product_id"
        private const val PARAM_INPUT = "input"
        private const val PRODUCT_ID = "input"

        @JvmStatic
        fun createRequestParams(paramData: ProductUpdateV3Param, listProductId: List<String>): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_BULK_EDIT, paramData)
            requestParams.putObject(PARAM_LIST_ID, listProductId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<ProductUpdateV3Response>> {
        val listProductId = requestParams.getObject(PARAM_LIST_ID) as List<String>

        return Observable.from(listProductId)
                .flatMap {
                    val variables = HashMap<String, Any>()
                    val bulkEditQuery = requestParams.getObject(PARAM_BULK_EDIT) as ProductUpdateV3Param
                    bulkEditQuery.productId = it
                    variables[PARAM_INPUT] = bulkEditQuery
                    val graphqlRequest = GraphqlRequest(gqlQuery, ProductUpdateV3Param::class.java, variables)
                    graphqlUseCase.clearRequest()
                    graphqlUseCase.addRequest(graphqlRequest)

                    graphqlUseCase.createObservable(requestParams).flatMap { response ->
                        val data: ProductUpdateV3Response = response.getData(ProductUpdateV3Response::class.java)
                        Observable.just(data)
                    }.onErrorResumeNext {
                        val data = ProductUpdateV3Response()
                        Observable.just(data)
                    }.toList().flatMap {
                        Observable.just(it)
                    }
                }
    }

}