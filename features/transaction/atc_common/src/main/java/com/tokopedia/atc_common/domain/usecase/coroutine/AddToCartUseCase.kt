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
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                           private val addToCartDataMapper: AddToCartDataMapper,
                                           private val chosenAddressAddToCartRequestHelper: ChosenAddressAddToCartRequestHelper) : UseCase<AddToCartDataModel>() {

    var addToCartRequestParams: AddToCartRequestParams? = null

    fun setParams(addToCartRequestParams: AddToCartRequestParams) {
        this.addToCartRequestParams = addToCartRequestParams
    }

    private fun getParams(): Map<String, Any?> {
        return mapOf(
                PARAM_ATC to mapOf(
                        PARAM_PRODUCT_ID to addToCartRequestParams?.productId,
                        PARAM_SHOP_ID to addToCartRequestParams?.shopId,
                        PARAM_QUANTITY to addToCartRequestParams?.quantity,
                        PARAM_NOTES to addToCartRequestParams?.notes,
                        PARAM_LANG to addToCartRequestParams?.lang,
                        PARAM_ATTRIBUTION to addToCartRequestParams?.attribution,
                        PARAM_LIST_TRACKER to addToCartRequestParams?.listTracker,
                        PARAM_UC_PARAMS to addToCartRequestParams?.ucParams,
                        PARAM_WAREHOUSE_ID to addToCartRequestParams?.warehouseId,
                        PARAM_ATC_FROM_EXTERNAL_SOURCE to addToCartRequestParams?.atcFromExternalSource,
                        PARAM_IS_SCP to addToCartRequestParams?.isSCP,
                        ChosenAddressAddToCartRequestHelper.PARAM_KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
                )
        )
    }

    override suspend fun executeOnBackground(): AddToCartDataModel {
        if (addToCartRequestParams == null) {
            throw RuntimeException("Parameters has not been initialized!")
        }

        val request = GraphqlRequest(QUERY, AddToCartGqlResponse::class.java, getParams())
        val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<AddToCartGqlResponse>()

        val result = addToCartDataMapper.mapAddToCartResponse(response)
        if (!result.isStatusError()) {
            addToCartRequestParams?.let {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(
                        it.productId.toString(), it.productName, it.price,
                        it.quantity.toString(), it.category
                )
                AddToCartBaseAnalytics.sendBranchIoTracking(
                        it.productId.toString(), it.productName, it.price,
                        it.quantity.toString(), it.category, it.categoryLevel1Id,
                        it.categoryLevel1Name, it.categoryLevel2Id, it.categoryLevel2Name,
                        it.categoryLevel3Id, it.categoryLevel3Name, it.userId
                )
            }
            return result
        } else {
            throw ResponseErrorException(result.errorMessage.joinToString(", "))
        }
    }

    companion object {
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
                /*tracking data*/ productName: String = "", category: String = "", price: String = "", userId: String = ""): AddToCartRequestParams {
            return AddToCartRequestParams(
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
        }

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