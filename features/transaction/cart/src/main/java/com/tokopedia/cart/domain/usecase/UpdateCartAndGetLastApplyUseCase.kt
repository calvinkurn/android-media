package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndValidateUseData
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.GetLastApplyPromoUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by fwidjaja on 2020-03-04.
 */
class UpdateCartAndGetLastApplyUseCase @Inject constructor(
    private val updateCartUseCase: UpdateCartUseCase,
    private val getLastApplyPromoUseCase: GetLastApplyPromoUseCase,
) : UseCase<UpdateAndValidateUseData>() {

    companion object {
        const val PARAM_VALUE_SOURCE_UPDATE_QTY_NOTES = "update_qty_notes"
    }

    private var params: UpdateCartWrapperRequest = UpdateCartWrapperRequest()

    fun setParams(request: UpdateCartWrapperRequest): UpdateCartAndGetLastApplyUseCase {
        this.params = request
        return this
    }

    override suspend fun executeOnBackground(): UpdateAndValidateUseData {
        val updateAndValidateUseData = UpdateAndValidateUseData()
        val updateCartData = updateCartUseCase.setParams(params).executeOnBackground()
        updateAndValidateUseData.updateCartData = updateCartData
        if (!updateCartData.isSuccess) {
            throw CartResponseErrorException(updateCartData.message)
        }
        val getLastApplyData = getLastApplyPromoUseCase.setParam(params.getLastApplyPromoRequest).executeOnBackground()
        updateAndValidateUseData.promoUiModel = getLastApplyData.promoUiModel
        return updateAndValidateUseData
    }
}
