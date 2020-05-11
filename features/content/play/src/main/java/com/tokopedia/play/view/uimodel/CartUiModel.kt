package com.tokopedia.play.view.uimodel

import com.tokopedia.play.analytic.TrackingField
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction


/**
 * Created by mzennis on 2020-03-05.
 */
data class CartUiModel(
        val isShow: Boolean = false,
        val count: Int = 0
)

data class CartFeedbackUiModel(
        val isSuccess: Boolean = false,
        val errorMessage: String = "",
        val action: ProductAction,

        @TrackingField
        val cartId: String,

        @TrackingField
        val product: ProductLineUiModel,

        @TrackingField
        val bottomInsetsType: BottomInsetsType
)