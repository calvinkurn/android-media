package com.tokopedia.atc_common.domain.usecase

import com.google.gson.Gson
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.data.model.response.ocs.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
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

open class AddToCartOcsUseCase @Inject constructor(@Named("atcOcsMutation") private val queryString: String,
                                                   private val gson: Gson,
                                                   private val graphqlUseCase: GraphqlUseCase,
                                                   private val addToCartDataMapper: AddToCartDataMapper,
                                                   private val chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM_ATC = "params"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_QUANTITY = "quantity"
        private const val PARAM_NOTES = "notes"
        private const val PARAM_WAREHOUSE_ID = "warehouse_id"
        private const val PARAM_CUSTOMER_ID = "customer_id"
        private const val PARAM_TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val PARAM_TRACKER_LIST_NAME = "tracker_list_name"
        private const val PARAM_UC_UT = "uc_ut_param"
        private const val PARAM_IS_TRADE_IN = "is_trade_in"
        private const val PARAM_SHIPPING_PRICE = "shipping_price"
    }

    private fun getParams(ocsRequestParams: AddToCartOcsRequestParams): Map<String, Any?> {
        return mapOf(
                PARAM_ATC to mapOf(
                        PARAM_PRODUCT_ID to ocsRequestParams.productId,
                        PARAM_SHOP_ID to ocsRequestParams.shopId,
                        PARAM_QUANTITY to ocsRequestParams.quantity,
                        PARAM_NOTES to ocsRequestParams.notes,
                        PARAM_WAREHOUSE_ID to ocsRequestParams.warehouseId,
                        PARAM_CUSTOMER_ID to ocsRequestParams.customerId,
                        PARAM_TRACKER_ATTRIBUTION to ocsRequestParams.trackerAttribution,
                        PARAM_TRACKER_LIST_NAME to ocsRequestParams.trackerListName,
                        PARAM_UC_UT to ocsRequestParams.utParam,
                        PARAM_IS_TRADE_IN to ocsRequestParams.isTradeIn,
                        PARAM_SHIPPING_PRICE to ocsRequestParams.shippingPrice,
                        ChosenAddressAddToCartRequestHelper.PARAM_KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOcsRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOcsGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOcsGqlResponse = it.getData<AddToCartOcsGqlResponse>(AddToCartOcsGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOcsResponse(addToCartOcsGqlResponse)
            if (!result.isStatusError()) {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                        addToCartRequest.quantity.toString(), addToCartRequest.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                        addToCartRequest.quantity.toString(), addToCartRequest.category, addToCartRequest.categoryLevel1Id,
                        addToCartRequest.categoryLevel1Name, addToCartRequest.categoryLevel2Id, addToCartRequest.categoryLevel2Name,
                        addToCartRequest.categoryLevel3Id, addToCartRequest.categoryLevel3Name, addToCartRequest.userId)
            }
            result
        }
    }
}