package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.MerchantVoucherType

/**
 * Created by jegul on 03/03/20
 */
data class MerchantVoucherUiModel(
        val type: MerchantVoucherType,
        val title: String,
        val description: String
)