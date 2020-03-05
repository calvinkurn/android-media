package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccUseCase @Inject constructor(@Named("atcOccMutation") private val queryString: String,
                                              private val graphqlUseCase: GraphqlUseCase,
                                              private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_QUANTITY = "quantity"
        private const val PARAM_WAREHOUSE_ID = "warehouse_id"
        private const val PARAM_LANG = "lang"
        private const val PARAM_IS_SCP = "is_scp"
        private const val PARAM_ATTRIBUTION = "attribution"
        private const val PARAM_UC_PARAMS = "uc_params"
        private const val PARAM_LIST_TRACKER = "list_tracker"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOccRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOccGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccGqlResponse>(AddToCartOccGqlResponse::class.java)
            addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
        }

    }

    private fun getParams(addToCartRequest: AddToCartOccRequestParams): Map<String, Any> {
        return mapOf(
                PARAM_PRODUCT_ID to addToCartRequest.productId,
                PARAM_SHOP_ID to addToCartRequest.shopId,
                PARAM_QUANTITY to addToCartRequest.quantity,
                PARAM_WAREHOUSE_ID to addToCartRequest.warehouseId,
                PARAM_LANG to addToCartRequest.lang,
                PARAM_IS_SCP to addToCartRequest.isScp,
                PARAM_ATTRIBUTION to addToCartRequest.attribution,
                PARAM_UC_PARAMS to addToCartRequest.ucParam,
                PARAM_LIST_TRACKER to addToCartRequest.listTracker
        )
    }

}