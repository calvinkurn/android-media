package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.play.data.CartFeedbackResponseModel
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 05, 2021
 */

@PlayScope
class PlayCartMapper @Inject constructor() {

    fun mapCartFeedbackResponse(response: AddToCartDataModel) = CartFeedbackResponseModel(
        isSuccess = response.data.success == 1,
        errorMessage = if (response.errorMessage.size > 0)
            response.errorMessage.joinToString { "$it " } else "",
        cartId = response.data.cartId
    )
}