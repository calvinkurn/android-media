package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.data.model.request.chosenaddress.ChosenAddressAddToCartRequestHelper
import com.tokopedia.atc_common.data.model.response.AddToCartGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.RuntimeException
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                           private val addToCartDataMapper: AddToCartDataMapper,
                                           private val chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper) : UseCase<AddToCartDataModel>() {

    // Todo : set params
    var requestParams: RequestParams? = null

    private fun getParams(addToCartRequestParams: AddToCartRequestParams): Map<String, Any?> {
        return mapOf(
                PARAM_ATC to mapOf(
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
                        PARAM_IS_SCP to addToCartRequestParams.isSCP,
                        ChosenAddressAddToCartRequestHelper.PARAM_KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): AddToCartDataModel {
        if (requestParams == null) {
            throw RuntimeException("Request Params has not been initialized!")
        }

        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartRequestParams

        val request = GraphqlRequest(QUERY, AddToCartGqlResponse::class.java, getParams(addToCartRequest))
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<AddToCartGqlResponse>()

        val result = addToCartDataMapper.mapAddToCartResponse(response)
        if (!result.isStatusError()) {
            AddToCartBaseAnalytics.sendAppsFlyerTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                    addToCartRequest.quantity.toString(), addToCartRequest.category)
            AddToCartBaseAnalytics.sendBranchIoTracking(addToCartRequest.productId.toString(), addToCartRequest.productName, addToCartRequest.price,
                    addToCartRequest.quantity.toString(), addToCartRequest.category, addToCartRequest.categoryLevel1Id,
                    addToCartRequest.categoryLevel1Name, addToCartRequest.categoryLevel2Id, addToCartRequest.categoryLevel2Name,
                    addToCartRequest.categoryLevel3Id, addToCartRequest.categoryLevel3Name, addToCartRequest.userId)
            return result
        } else {
            throw ResponseErrorException(result.errorMessage.joinToString(", "))
        }
    }

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM_ATC = "param"
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
                /*tracking data*/ productName: String = "", category: String = "", price: String = "", userId: String = ""): RequestParams {
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
                                        price = price,
                                        userId = userId
                                )
                        )
                    }
        }

        // Todo : set query
        val QUERY = """
            mutation add_to_cart_v2(${'$'}param: ATCV2Params) {
              add_to_cart_v2(
                param:${'$'}param) {
                    error_message
                    status
                    data {
                      success
                      cart_id
                      product_id
                      quantity
                      notes
                      shop_id
                      customer_id
                      warehouse_id
                      tracker_attribution
                      tracker_list_name
                      uc_ut_param
                      is_trade_in
                      message
                    }
                    error_reporter {
                      eligible
                      texts {
                        submit_title
                        submit_description
                        submit_button
                        cancel_button
                      }
                    }
                }
            }
        """.trimIndent()

    }

}