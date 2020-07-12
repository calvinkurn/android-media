package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.AddToCartAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

class AddToCartUseCase @Inject constructor(@Named("atcMutation") private val queryString: String,
                                           private val graphqlUseCase: GraphqlUseCase,
                                           private val addToCartDataMapper: AddToCartDataMapper,
                                           private val analytics: AddToCartAnalytics) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_QUANTITY = "quantity"
        private const val PARAM_NOTES = "notes"
        private const val PARAM_LANG = "lang"
        private const val PARAM_ATTRIBUTION = "attribution"
        private const val PARAM_LIST_TRACKER = "listTracker"
        private const val PARAM_UC_PARAMS = "ucParams"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_ATC_FROM_EXTERNAL_SOURCE = "atcFromExternalSource"
        private const val PARAM_IS_SCP = "isSCP"

        @JvmStatic
        @JvmOverloads
        fun getMinimumParams(productId: String, shopId: String, quantity: Int = 1, notes: String = "", atcExternalSource: String = AddToCartRequestParams.ATC_FROM_OTHERS,
                /*tracking data*/ productName: String = "", category: String = "", price: String = ""): RequestParams {
            return RequestParams.create()
                    .apply {
                        putObject(
                                REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                                AddToCartRequestParams(
                                        productId = productId.toLong(),
                                        shopId = shopId.toInt(),
                                        quantity = quantity,
                                        notes = notes,
                                        atcFromExternalSource = atcExternalSource,
                                        productName = productName,
                                        category = category,
                                        price = price
                                )
                        )
                    }
        }
    }

    private fun getParams(addToCartRequestParams: AddToCartRequestParams): Map<String, Any> {
        return mapOf(
                PARAM_PRODUCT_ID to addToCartRequestParams.productId,
                PARAM_SHOP_ID to addToCartRequestParams.shopId,
                PARAM_QUANTITY to addToCartRequestParams.quantity,
                PARAM_NOTES to addToCartRequestParams.notes,
                PARAM_LANG to addToCartRequestParams.lang,
                PARAM_ATTRIBUTION to addToCartRequestParams.attribution,
                PARAM_LIST_TRACKER to addToCartRequestParams.listTracker,
                PARAM_UC_PARAMS to addToCartRequestParams.ucParams,
                PARAM_WAREHOUSE_ID to addToCartRequestParams.warehouseId,
                PARAM_ATC_FROM_EXTERNAL_SOURCE to addToCartRequestParams.atcFromExternalSource,
                PARAM_IS_SCP to addToCartRequestParams.isSCP
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartRequestParams

        val graphqlRequest = GraphqlRequest(queryString, AddToCartGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartGqlResponse = it.getData<AddToCartGqlResponse>(AddToCartGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartResponse(addToCartGqlResponse)
            analytics.sendAppsFlyerTracking(result, addToCartRequest)
            result
        }

    }

}