package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.domain.mapper.mapUpdateCartData
import com.tokopedia.cart.domain.model.updatecart.UpdateAndGetLastApplyData
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.GetLastApplyPromoUseCase
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-04.
 */
class UpdateCartAndGetLastApplyUseCase @Inject constructor(
    private val updateCartUseCase: UpdateCartUseCase,
    private val getLastApplyPromoUseCase: GetLastApplyPromoUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdateCartWrapperRequest, UpdateAndGetLastApplyData>(dispatcher.io) {

    companion object {
        const val PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES = "update_qty_notes"
    }

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: UpdateCartWrapperRequest): UpdateAndGetLastApplyData {
        val updateAndValidateUseData = UpdateAndGetLastApplyData()
        updateCartUseCase.setParams(params.updateCartRequestList, params.source)
        val updateCartDataResponse = updateCartUseCase.executeOnBackground()
        val updateCartData = mapUpdateCartData(updateCartDataResponse)
        updateAndValidateUseData.updateCartData = updateCartData
        if (!updateCartData.isSuccess) {
            throw CartResponseErrorException(updateCartData.message)
        }
        val getLastApplyData = getLastApplyPromoUseCase(params.getLastApplyPromoRequest)
        updateAndValidateUseData.promoUiModel = getLastApplyData.promoUiModel
        return updateAndValidateUseData
    }
}
