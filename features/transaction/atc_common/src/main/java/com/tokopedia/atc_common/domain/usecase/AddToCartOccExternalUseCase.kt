package com.tokopedia.atc_common.domain.usecase

import android.content.Context
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_OCC_EXTERNAL
import com.tokopedia.atc_common.data.model.response.AddToCartOccExternalGqlResponse
import com.tokopedia.atc_common.data.model.response.DetailOccResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.tracking.AddToCartOccExternalTracking
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSession
import org.json.JSONArray
import org.json.JSONObject
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccExternalUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                                      @Named(MUTATION_ATC_OCC_EXTERNAL) private val query: String,
                                                      private val graphqlUseCase: GraphqlUseCase,
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
    }

    override fun createObservable(requestParams: RequestParams): Observable<AddToCartDataModel> {
        val productId = requestParams.getString(REQUEST_PARAM_KEY_PRODUCT_ID, "")
        val graphqlRequest = GraphqlRequest(query, AddToCartOccExternalGqlResponse::class.java, getParams(productId))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccExternalGqlResponse>(AddToCartOccExternalGqlResponse::class.java)
            val result = addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
            if (addToCartOccGqlResponse.addToCartOccResponse.data.success == 1) {
                AddToCartOccExternalTracking.sendEETracking(addToCartOccGqlResponse.addToCartOccResponse.data.detail)
                AddToCartOccExternalTracking.sendAppsFlyerTracking(addToCartOccGqlResponse.addToCartOccResponse.data.detail)
                AddToCartOccExternalTracking.sendBranchIoTracking(context, addToCartOccGqlResponse.addToCartOccResponse.data.detail)
            }
            result
        }

    }

    private fun getParams(productId: String): Map<String, Any> {
        return mapOf(PARAM to mapOf(PARAM_PRODUCT_ID to productId))
    }

    private fun sendAppsFlyerTracking(response: DetailOccResponse) {
        TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to response.productId,
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to response.productName,
                        AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to response.quantity,
                        AFInAppEventParameterName.PRICE to response.price,
                        AF_PARAM_CATEGORY to response.category,
                        AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, response.productId.toString()).put(AF_PARAM_CONTENT_QUANTITY, response.quantity))
                )
        )
    }

    private fun sendEETracking(response: DetailOccResponse) {
        //same as atc occ in pdp
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                mutableMapOf<String, Any>(
                        EVENT to "addToCart",
                        EVENT_CATEGORY to "product detail page",
                        EVENT_ACTION to "click - Beli Langsung on pdp",
                        EVENT_LABEL to "fitur : Beli Langsung occ",
                        "layout" to "layout:${""};catName:${response.category.substringAfterLast("/")};catId:${response.categoryId.substringAfterLast("/")};",
                        "component" to "",
                        "productId" to response.productId,
                        "shopId" to response.shopId,
                        "shopType" to setDefaultIfEmpty(response.shopType),
                        "ecommerce" to mutableMapOf(
                                "currencyCode" to "IDR",
                                "add" to mutableMapOf(
                                        "products" to arrayListOf(mutableMapOf(
                                                "name" to response.productName,
                                                "id" to response.productId,
                                                "price" to response.price,
                                                "brand" to setDefaultIfEmpty(response.brand),
                                                "category" to setDefaultIfEmpty(response.category),
                                                "variant" to setDefaultIfEmpty(response.variant),
                                                "quantity" to response.quantity,
                                                "dimension38" to setDefaultIfEmpty(response.trackerAttribution),
                                                "dimension40" to "",
                                                "dimension45" to response.cartId,
                                                "dimension54" to getMultiOriginAttribution(response.isMultiOrigin),
                                                "dimension79" to response.shopId,
                                                "shop_id" to response.shopId,
                                                "dimension80" to setDefaultIfEmpty(response.shopName),
                                                "shop_name" to setDefaultIfEmpty(response.shopName),
                                                "dimension81" to response.shopType,
                                                "shop_type" to response.shopType,
                                                "picture" to setDefaultIfEmpty(response.picture),
                                                "url" to response.url,
                                                "category_id" to response.categoryId,
                                                "dimension82" to response.categoryId,
                                                "dimension83" to if (response.isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                                        ))
                                )
                        )
                ))
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

    private fun sendBranchIoTracking(response: DetailOccResponse) {
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_CART, createLinkerData(response)))
    }

    private fun createLinkerData(response: DetailOccResponse): LinkerData {
        return LinkerData().apply {
            id = response.productId.toString()
            price = response.price.toInt().toString()
            description = ""
            shopId = response.shopId.toString()
            catLvl1 = response.category
            userId = UserSession(context).userId
            currency = "IDR"
            quantity = response.quantity.toString()
        }
    }
}