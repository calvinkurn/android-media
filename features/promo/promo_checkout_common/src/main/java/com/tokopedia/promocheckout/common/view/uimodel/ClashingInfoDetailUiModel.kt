package com.tokopedia.promocheckout.common.view.uimodel

/**
 * Created by fwidjaja on 17/03/19.
 */
data class ClashingInfoDetailUiModel(
        var clashMessage: String = "",
        var clashReason: String = "",
        var isClashedPromos: Boolean = false,
        var option: List<Any> = emptyList()
)