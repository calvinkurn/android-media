package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.response.occ.AddToCartOccMultiGqlResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.query.QUERY_ADD_TO_CART_OCC_MULTI
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class AddToCartOccMultiUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository,
                                                   private val addToCartDataMapper: AddToCartDataMapper,
                                                   private val chosenAddressAddToCartRequestHelper: ChosenAddressRequestHelper) : UseCase<AddToCartOccMultiDataModel>() {

    private var requestParams: AddToCartOccMultiRequestParams? = null

    fun setParams(addToCartOccMultiRequestParams: AddToCartOccMultiRequestParams): AddToCartOccMultiUseCase {
        requestParams = addToCartOccMultiRequestParams
        return this
    }

    /**
     * Execute ATC OCC with previously set params
     *
     * @return AddToCartDataModel when BE responds, however client need to check whether ATC is success
     * @see AddToCartDataModel.isStatusError
     *
     * @throws RuntimeException("Parameters has not been initialized!") when params is null
     * @see AddToCartOccMultiUseCase.setParams
     */
    override suspend fun executeOnBackground(): AddToCartOccMultiDataModel {
        val sentParams = requestParams?.copy() ?: throw RuntimeException(AtcConstant.ERROR_PARAMETER_NOT_INITIALIZED)

        val graphqlRequest = GraphqlRequest(QUERY_ADD_TO_CART_OCC_MULTI, AddToCartOccMultiGqlResponse::class.java, getParams(sentParams))
        val addToCartOccGqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest)).getSuccessData<AddToCartOccMultiGqlResponse>()

        val result = addToCartDataMapper.mapAddToCartOccMultiResponse(addToCartOccGqlResponse)
        if (!result.isStatusError()) {
            sentParams.carts.forEach {
                AddToCartBaseAnalytics.sendAppsFlyerTracking(it.productId, it.productName, it.price,
                        it.quantity, it.category)
                AddToCartBaseAnalytics.sendBranchIoTracking(it.productId, it.productName, it.price,
                        it.quantity, it.category, it.categoryLevel1Id,
                        it.categoryLevel1Name, it.categoryLevel2Id, it.categoryLevel2Name,
                        it.categoryLevel3Id, it.categoryLevel3Name, it.userId)
            }
        }
        return result
    }

    private fun getParams(addToCartRequest: AddToCartOccMultiRequestParams): Map<String, Any?> {
        return mapOf(PARAM to mapOf(
                PARAM_CARTS to addToCartRequest.carts.map {
                    mapOf<String, Any?>(
                            PARAM_CART_ID to it.cartId,
                            PARAM_PRODUCT_ID to it.productId,
                            PARAM_SHOP_ID to it.shopId,
                            PARAM_QUANTITY to it.quantity,
                            PARAM_NOTES to it.notes,
                            PARAM_WAREHOUSE_ID to it.warehouseId,
                            PARAM_ATTRIBUTION to it.attribution,
                            PARAM_LIST_TRACKER to it.listTracker,
                            PARAM_UC_PARAMS to it.ucParam
                    )
                },
                PARAM_CHOSEN_ADDRESS to chosenAddressAddToCartRequestHelper.getChosenAddress(),
                PARAM_LANG to addToCartRequest.lang,
                PARAM_SOURCE to addToCartRequest.source
        ))
    }

    companion object {
        private const val PARAM = "param"
        private const val PARAM_CARTS = "carts"
        private const val PARAM_CART_ID = "cart_id"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_QUANTITY = "quantity"
        private const val PARAM_NOTES = "notes"
        private const val PARAM_WAREHOUSE_ID = "warehouseID"
        private const val PARAM_ATTRIBUTION = "attribution"
        private const val PARAM_LIST_TRACKER = "list_tracker"
        private const val PARAM_UC_PARAMS = "uc_params"
        private const val PARAM_CHOSEN_ADDRESS = "chosen_address"
        private const val PARAM_LANG = "lang"
        private const val PARAM_SOURCE = "source"
    }
}