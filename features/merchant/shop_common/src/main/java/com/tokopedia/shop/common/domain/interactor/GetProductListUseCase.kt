package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.GQL_PRODUCT_LIST
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListFilterParam
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

open class GetProductListUseCase @Inject constructor(@Named(GQL_PRODUCT_LIST) private val gqlQuery: String,
                                                private val graphqlUseCase: GraphqlUseCase) : UseCase<ProductListResponse>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_PAGE = "page"
        private const val PARAM_FKEYWORD = "fkeyword"
        private const val PARAM_FMENU = "fmenu"
        private const val PARAM_FCATALOG = "fcatalog"
        private const val PARAM_FCONDITION = "fcondition"
        private const val PARAM_FPICTURE = "fpicture"
        private const val PARAM_SORT = "sort"
        private const val PARAM_FCATEGORY = "fcategory"
        private const val PARAM_FILTER = "filter"

        @JvmStatic
        fun createRequestParams(shopId: String?, page: Int?, fKeyword: String?,
                                fCatalog: Int?, fCondition: Int?, fMenu: String?, fPicture: Int?,
                                sort: Int?, fCategory: Int?): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_SHOP_ID, shopId)
            requestParams.putString(PARAM_FKEYWORD, fKeyword)
            requestParams.putString(PARAM_FMENU, fMenu)
            requestParams.putInt(PARAM_PAGE, page ?: 1)
            requestParams.putInt(PARAM_FCATALOG, fCatalog ?: 0)
            requestParams.putInt(PARAM_FCONDITION, fCondition ?: 0)
            requestParams.putInt(PARAM_FPICTURE, fPicture ?: 0)
            requestParams.putInt(PARAM_SORT, sort ?: 0)
            requestParams.putInt(PARAM_FCATEGORY, fCategory ?: 0)

            return requestParams
        }
    }


    override fun createObservable(requestParams: RequestParams): Observable<ProductListResponse> {

        val variables = HashMap<String, Any>()
        variables[PARAM_SHOP_ID] = requestParams.getString(PARAM_SHOP_ID, "")
        variables[PARAM_FILTER] = getFilterInputParam(requestParams)

        val graphqlRequest = GraphqlRequest(gqlQuery, ProductListResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ProductListResponse? = it.getData(ProductListResponse::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.firstOrNull()?.message)
            }
            data
        }
    }

    private fun getFilterInputParam(requestParams: RequestParams): ProductListFilterParam {
        return ProductListFilterParam(
                page = requestParams.getInt(PARAM_PAGE, 0),
                fKeyword = requestParams.getString(PARAM_FKEYWORD, ""),
                fMenu = requestParams.getString(PARAM_FMENU, ""),
                fCategory = requestParams.getInt(PARAM_FCATEGORY, 0),
                fCondition = requestParams.getInt(PARAM_FCONDITION, 0),
                fCatalog = requestParams.getInt(PARAM_FCATALOG, 0),
                fPicture = requestParams.getInt(PARAM_FPICTURE, 0),
                sort = requestParams.getInt(PARAM_SORT, 0)

        )
    }


}