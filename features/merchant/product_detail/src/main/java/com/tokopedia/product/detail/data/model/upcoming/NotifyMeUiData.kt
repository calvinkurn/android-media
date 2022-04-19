package com.tokopedia.product.detail.data.model.upcoming

import com.tokopedia.product.detail.common.ProductDetailCommonConstant

/**
 * Created by Yehezkiel on 29/09/21
 */
data class NotifyMeUiData(
        val notifyMeAction: String = ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER,
        val isSuccess: Boolean = false,
        val successMessage: String = ""
)