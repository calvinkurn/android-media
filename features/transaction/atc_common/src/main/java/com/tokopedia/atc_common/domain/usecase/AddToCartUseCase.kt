package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.replaceParam
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
                                           private val graphqlUseCase: GraphqlUseCase) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM_PRODUCT_ID = "#productID"
        private const val PARAM_SHOP_ID = "#shopID"
        private const val PARAM_QUANTITY = "#quantity"
        private const val PARAM_NOTES = "#notes"
        private const val PARAM_LANG = "#lang"
        private const val PARAM_ATTRIBUTION = "#attribution"
        private const val PARAM_LIST_TRACKER = "#listTracker"
        private const val PARAM_UC_PARAMS = "#ucParams"
        private const val PARAM_WAREHOUSE_ID = "#warehouseID"
        private const val PARAM_ATC_FROM_EXTERNAL_SOURCE = "#atcFromExternalSource"
        private const val PARAM_IS_SCP = "#isSCP"
    }

    private fun getParams(addToCartRequestParams: AddToCartRequestParams): String {
        var stringQueryBuilder = StringBuilder(queryString)
        stringQueryBuilder.replaceParam(stringQueryBuilder, PARAM_PRODUCT_ID, addToCartRequestParams.productId.toString())
                .replaceParam(stringQueryBuilder, PARAM_SHOP_ID, addToCartRequestParams.shopId.toString())
                .replaceParam(stringQueryBuilder, PARAM_QUANTITY, addToCartRequestParams.quantity.toString())
                .replaceParam(stringQueryBuilder, PARAM_NOTES, addToCartRequestParams.notes)
                .replaceParam(stringQueryBuilder, PARAM_LANG, addToCartRequestParams.lang)
                .replaceParam(stringQueryBuilder, PARAM_ATTRIBUTION, addToCartRequestParams.attribution)
                .replaceParam(stringQueryBuilder, PARAM_LIST_TRACKER, addToCartRequestParams.listTracker)
                .replaceParam(stringQueryBuilder, PARAM_UC_PARAMS, addToCartRequestParams.ucParams)
                .replaceParam(stringQueryBuilder, PARAM_WAREHOUSE_ID, addToCartRequestParams.warehouseId.toString())
                .replaceParam(stringQueryBuilder, PARAM_ATC_FROM_EXTERNAL_SOURCE, addToCartRequestParams.atcFromExternalSource)
                .replaceParam(stringQueryBuilder, PARAM_IS_SCP, addToCartRequestParams.isSCP.toString())

        return stringQueryBuilder.toString()
    }


    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartRequestParams

        val graphqlRequest = GraphqlRequest(getParams(addToCartRequest), AddToCartGqlResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartGqlResponse = it.getData<AddToCartGqlResponse>(AddToCartGqlResponse::class.java)

            val dataModel = DataModel()
            dataModel.success = addToCartGqlResponse.addToCartResponse.data.success
            dataModel.cartId = addToCartGqlResponse.addToCartResponse.data.cartId
            dataModel.productId = addToCartGqlResponse.addToCartResponse.data.productId
            dataModel.quantity = addToCartGqlResponse.addToCartResponse.data.quantity
            dataModel.notes = addToCartGqlResponse.addToCartResponse.data.notes
            dataModel.shopId = addToCartGqlResponse.addToCartResponse.data.shopId
            dataModel.customerId = addToCartGqlResponse.addToCartResponse.data.customerId
            dataModel.warehouseId = addToCartGqlResponse.addToCartResponse.data.warehouseId
            dataModel.trackerAttribution = addToCartGqlResponse.addToCartResponse.data.trackerAttribution
            dataModel.trackerListName = addToCartGqlResponse.addToCartResponse.data.trackerListName
            dataModel.ucUtParam = addToCartGqlResponse.addToCartResponse.data.ucUtParam
            dataModel.isTradeIn = addToCartGqlResponse.addToCartResponse.data.isTradeIn
            dataModel.message = addToCartGqlResponse.addToCartResponse.data.message

            val addToCartDataModel = AddToCartDataModel()
            addToCartDataModel.status = addToCartGqlResponse.addToCartResponse.status
            addToCartDataModel.errorMessage = addToCartGqlResponse.addToCartResponse.errorMessage
            addToCartDataModel.data = dataModel

            addToCartDataModel
        }

    }

}