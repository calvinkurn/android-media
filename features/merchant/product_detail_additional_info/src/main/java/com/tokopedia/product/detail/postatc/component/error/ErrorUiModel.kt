package com.tokopedia.product.detail.postatc.component.error

import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

data class ErrorUiModel(
    val errorType: Int,
    override val name: String = ""
) : PostAtcUiModel
