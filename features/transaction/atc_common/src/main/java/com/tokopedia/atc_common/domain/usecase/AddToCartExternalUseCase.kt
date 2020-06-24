package com.tokopedia.atc_common.domain.usecase

import android.os.Bundle
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.atc_common.AtcConstant.ATC_ERROR_GLOBAL
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_EXTERNAL
import com.tokopedia.atc_common.data.model.response.atcexternal.AddToCartExternalGqlResponse
import com.tokopedia.atc_common.domain.AddToCartResponseErrorException
import com.tokopedia.atc_common.domain.mapper.AddToCartExternalDataMapper
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartExternalUseCase @Inject constructor(@Named(MUTATION_ATC_EXTERNAL) private val queryString: String,
                                                   private val graphqlUseCase: GraphqlUseCase,
                                                   private val addToCartDataMapper: AddToCartExternalDataMapper) : UseCase<AddToCartExternalModel>() {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"

        private const val AF_PARAM_CATEGORY = "category"
        private const val AF_PARAM_CONTENT_ID = "id"
        private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
        private const val AF_VALUE_CONTENT_TYPE = "product"
        private const val AF_VALUE_CURRENCY = "IDR"
    }

    override fun createObservable(requestParams: RequestParams): Observable<AddToCartExternalModel> {
        val params = requestParams.parameters
        val graphqlRequest = GraphqlRequest(queryString, AddToCartExternalGqlResponse::class.java, params)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val response = it.getData<AddToCartExternalGqlResponse>(AddToCartExternalGqlResponse::class.java)
            if (response != null) {
                val result = addToCartDataMapper.map(response)
                if (result.success == 1) {
                    // Todo : send EE tracking
                    // Todo : send appsflyer tracking
                    sendAppsFlyerTracking(result.data)
                    // Todo : send branch io tracking
                    result
                } else {
                    var message = ATC_ERROR_GLOBAL
                    if (response.response.data.message.isNotEmpty()) {
                        message = response.response.data.message.joinToString { ". " }
                    }
                    throw AddToCartResponseErrorException(message)
                }
            } else {
                throw AddToCartResponseErrorException(ATC_ERROR_GLOBAL)
            }
        }
    }

    private fun sendEnhancedEcommerceTracking(data: AddToCartExternalDataModel) {
        val itemBundle = Bundle()
        itemBundle.putString("item_id", setValueOrDefault(data.productId.toString()))
        itemBundle.putString("item_name", setValueOrDefault(data.productName))
        itemBundle.putString("item_brand", setValueOrDefault(data.brand))
        itemBundle.putString("item_category", setValueOrDefault(data.category))
        itemBundle.putString("item_variant", setValueOrDefault(data.variant))
        itemBundle.putString("shop_id", setValueOrDefault(data.shopId.toString()))
        itemBundle.putString("shop_name", setValueOrDefault(data.shopName))
        itemBundle.putString("shop_type", setValueOrDefault(data.shopType))
        itemBundle.putString("category_id", setValueOrDefault(data.categoryId))
        itemBundle.putInt("quantity", data.quantity.coerceAtLeast(1))
        itemBundle.putInt("price", data.price)
//        itemBundle.putString("dimension38", dsfsd)
//        itemBundle.putString("dimension40", creative)
//        itemBundle.putString("dimension45", creative)
//        itemBundle.putString("dimension53", creative)
//        itemBundle.putString("dimension54", hk)
//        itemBundle.putString("dimension79", dsfsd)
//        itemBundle.putString("dimension80", dsfsd)
//        itemBundle.putString("dimension81", dsfsd)
//        itemBundle.putString("dimension82", dsfsd)
//        itemBundle.putString("dimension83", creative)

        val eventDataLayer = Bundle()
        eventDataLayer.putString(EVENT, "add_to_cart")
        eventDataLayer.putString(EVENT_CATEGORY, "cart")
        eventDataLayer.putString(EVENT_ACTION, "click add to cart")
        eventDataLayer.putString(EVENT_LABEL, "")
        eventDataLayer.putParcelableArrayList("items", arrayListOf(itemBundle))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent("view_item", eventDataLayer)
    }

    private fun setValueOrDefault(value: String): String {
        if (value.isBlank()) {
            return "none/other"
        }
        return value
    }

    private fun sendAppsFlyerTracking(data: AddToCartExternalDataModel) {
        TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to data.productId.toString(),
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to data.productName,
                        AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to data.quantity,
                        AFInAppEventParameterName.PRICE to data.price,
                        AF_PARAM_CATEGORY to data.category,
                        AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject()
                                .put(AF_PARAM_CONTENT_ID, data.productId.toString())
                                .put(AF_PARAM_CONTENT_QUANTITY, data.quantity))
                )
        )
    }

}