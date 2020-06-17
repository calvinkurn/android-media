package com.tokopedia.atc_common.domain.usecase

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.data.model.response.DataOccResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject

class AddToCartOccExternalUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                      private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_PRODUCT_ID = "REQUEST_PARAM_KEY_PRODUCT_ID"

        private const val PARAM = "param"
        private const val PARAM_PRODUCT_ID = "product_id"

        private const val AF_PARAM_CATEGORY = "category"
        private const val AF_PARAM_CONTENT_ID = "id"
        private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
        private const val AF_VALUE_CONTENT_TYPE = "product"
        private const val AF_VALUE_CURRENCY = "IDR"

        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_NONE_OTHER = "none / other"

        private val QUERY =
                """mutation add_to_cart_occ_external(${'$'}param: OneClickCheckoutATCParam) {
                    add_to_cart_occ_external(param: ${'$'}param) {
                        error_message
                        status
                        data {
                            message
                            success
                            data {
                                product_id
                                product_name
                                quantity
                                price
                                category
                                shop_id
                                shop_type
                                shop_name
                                picture
                                url
                                cart_id
                                brand
                                category_id
                                variant
                                tracker_attribution
                                is_multi_origin
                                is_free_ongkir
                            }
                        }
                    }
                }""".trimIndent()
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val productId = requestParams?.getString(REQUEST_PARAM_KEY_PRODUCT_ID, "") ?: ""
        val graphqlRequest = GraphqlRequest(QUERY, AddToCartOccExternalGqlResponse::class.java, getParams(productId))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccExternalGqlResponse>(AddToCartOccExternalGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
            sendEETracking(addToCartOccGqlResponse.addToCartOccResponse.data)
            sendAppsFlyerTracking(addToCartOccGqlResponse.addToCartOccResponse.data)
            result
        }

    }

    private fun getParams(productId: String): Map<String, Any> {
        return mapOf(PARAM to mapOf(PARAM_PRODUCT_ID to productId))
    }

    private fun sendAppsFlyerTracking(response: DataOccResponse) {
        if (response.success == 1) {
            TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                    mutableMapOf(
                            AFInAppEventParameterName.CONTENT_ID to response.detail.productId,
                            AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                            AFInAppEventParameterName.DESCRIPTION to response.detail.productName,
                            AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                            AFInAppEventParameterName.QUANTITY to response.detail.quantity,
                            AFInAppEventParameterName.PRICE to response.detail.price,
                            AF_PARAM_CATEGORY to response.detail.category,
                            AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, response.detail.productId.toString()).put(AF_PARAM_CONTENT_QUANTITY, response.detail.quantity))
                    )
            )
        }
    }

    private fun sendEETracking(response: DataOccResponse) {
        if (response.success == 1) {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    mutableMapOf<String, Any>(
                            "event" to "addToCart",
                            "eventCategory" to "product detail page",
                            "eventAction" to "click - Beli Langsung on pdp",
                            "eventLabel" to "fitur : Beli Langsung occ",
                            "layout" to "layout:${""};catName:${response.detail.category.substringAfterLast("/")};catId:${response.detail.categoryId.substringAfterLast("/")};",
                            "component" to "",
                            "productId" to response.detail.productId,
                            "shopId" to response.detail.shopId,
                            "shopType" to setDefaultIfEmpty(response.detail.shopType),
                            "ecommerce" to mutableMapOf(
                                    "currencyCode" to "IDR",
                                    "add" to mutableMapOf(
                                            "products" to arrayListOf(mutableMapOf(
                                                    "name" to response.detail.productName,
                                                    "id" to response.detail.productId,
                                                    "price" to response.detail.price,
                                                    "brand" to setDefaultIfEmpty(response.detail.brand),
                                                    "category" to setDefaultIfEmpty(response.detail.category),
                                                    "variant" to setDefaultIfEmpty(response.detail.variant),
                                                    "quantity" to response.detail.quantity,
                                                    "dimension38" to setDefaultIfEmpty(response.detail.trackerAttribution),
                                                    "dimension40" to "",
                                                    "dimension45" to response.detail.cartId,
                                                    "dimension54" to getMultiOriginAttribution(response.detail.isMultiOrigin),
                                                    "dimension79" to response.detail.shopId,
                                                    "shop_id" to response.detail.shopId,
                                                    "dimension80" to setDefaultIfEmpty(response.detail.shopName),
                                                    "shop_name" to setDefaultIfEmpty(response.detail.shopName),
                                                    "dimension81" to response.detail.shopType,
                                                    "shop_type" to response.detail.shopType,
                                                    "picture" to setDefaultIfEmpty(response.detail.picture),
                                                    "url" to response.detail.url,
                                                    "category_id" to response.detail.categoryId,
                                                    "dimension82" to response.detail.categoryId,
                                                    "dimension83" to if (response.detail.isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                                            ))
                                    )
                            )
                    ))
        }
    }

    private fun setDefaultIfEmpty(value: String?): String {
        if (value.isNullOrBlank()) {
            return VALUE_NONE_OTHER
        }
        return value
    }

    private fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }

}