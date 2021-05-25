package com.tokopedia.atc_common.domain.usecase

import com.google.gson.JsonArray
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import timber.log.Timber
import javax.inject.Inject

class AddToCartMultiLegacyUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                      private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AtcMultiData>() {

    companion object {
        private const val PARAM = "param"

        private const val PRODUCT_ID_KEY = "product_id"
        private const val PRODUCT_NAME_KEY = "product_name"
        private const val QUANTITY_KEY = "quantity"
        private const val PRODUCT_PRICE_KEY = "product_price"
        private const val CATEGORY_KEY = "category"
    }

    private var query: String = ""
    private var params: MutableMap<String, Any?> = mutableMapOf()
    private var userId: String = ""

    fun setup(query: String, params: MutableMap<String, Any?>, userId: String) {
        params[KEY_CHOSEN_ADDRESS] = chosenAddressAddToCartRequestHelper.getChosenAddress()
        this.params = params

        this.query = query
        this.userId = userId
    }

    override fun createObservable(requestParams: RequestParams): Observable<AtcMultiData> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(GraphqlRequest(query, AtcMultiData::class.java, params, false))
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val result = it.getData<AtcMultiData>(AtcMultiData::class.java)
            if (result.atcMulti.buyAgainData.success == 1) {
                try {
                    val jsonArray = params[PARAM] as? JsonArray ?: return@map result
                    for (jsonElement in jsonArray) {
                        try {
                            val product = jsonElement.asJsonObject
                            val productId = product[PRODUCT_ID_KEY].asLong
                            val productName = product[PRODUCT_NAME_KEY].asString
                            val quantity = product[QUANTITY_KEY].asInt
                            val productPrice = product[PRODUCT_PRICE_KEY].asString
                            val category = product[CATEGORY_KEY].asString
                            AddToCartBaseAnalytics.sendAppsFlyerTracking(productId.toString(), productName, productPrice,
                                    quantity.toString(), category)
                            AddToCartBaseAnalytics.sendBranchIoTracking(productId.toString(), productName, productPrice,
                                    quantity.toString(), category, "",
                                    "", "", "",
                                    "", "", userId)
                        } catch (t: Throwable) {
                            // failed parse json
                            Timber.d(t)
                        }
                    }
                } catch (t: Throwable) {
                    // failed parse param
                    Timber.d(t)
                }
            }
            result
        }
    }
}