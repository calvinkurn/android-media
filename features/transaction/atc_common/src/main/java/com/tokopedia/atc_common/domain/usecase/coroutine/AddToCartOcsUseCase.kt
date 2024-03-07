package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.response.ocs.AddToCartOcsGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.query.ADD_TO_CART_OCS_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper.Companion.KEY_CHOSEN_ADDRESS
import javax.inject.Inject
import kotlin.math.roundToLong

class AddToCartOcsUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val addToCartDataMapper: AddToCartDataMapper,
    private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<AddToCartOcsRequestParams, AddToCartDataModel>(dispatcher.io) {

    companion object {
        const val QUERY_ADD_TO_CART_OCS = "AddToCartOcsQuery"

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

    override fun graphqlQuery(): String = ADD_TO_CART_OCS_QUERY

    @GqlQuery(QUERY_ADD_TO_CART_OCS, ADD_TO_CART_OCS_QUERY)
    override suspend fun execute(params: AddToCartOcsRequestParams): AddToCartDataModel {
        val param = mapOf(
            PARAM_ATC to mapOf(
                PARAM_PRODUCT_ID to params.productId,
                PARAM_SHOP_ID to params.shopId,
                PARAM_QUANTITY to params.quantity,
                PARAM_NOTES to params.notes,
                PARAM_WAREHOUSE_ID to params.warehouseId,
                PARAM_CUSTOMER_ID to params.customerId,
                PARAM_TRACKER_ATTRIBUTION to params.trackerAttribution,
                PARAM_TRACKER_LIST_NAME to params.trackerListName,
                PARAM_UC_UT to params.utParam,
                PARAM_IS_TRADE_IN to params.isTradeIn,
                PARAM_SHIPPING_PRICE to params.shippingPrice.roundToLong(),
                KEY_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress()
            )
        )
        val request = GraphqlRequest(
            AddToCartOcsQuery(),
            AddToCartOcsGqlResponse::class.java,
            param
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<AddToCartOcsGqlResponse>()
        val result = addToCartDataMapper.mapAddToCartOcsResponse(response)
        if (!result.isStatusError()) {
            AddToCartBaseAnalytics.sendAppsFlyerTracking(
                params.productId,
                params.productName,
                params.price,
                params.quantity.toString(),
                params.category
            )
            AddToCartBaseAnalytics.sendBranchIoTracking(
                params.productId, params.productName, params.price,
                params.quantity.toString(), params.category, params.categoryLevel1Id,
                params.categoryLevel1Name, params.categoryLevel2Id, params.categoryLevel2Name,
                params.categoryLevel3Id, params.categoryLevel3Name, params.userId, params.shopId
            )
        }
        return result
    }
}
