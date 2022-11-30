package com.tokopedia.privacycenter.dsar.model.uimodel

data class GlobalErrorCustomUiModel(
    var isShow: Boolean = false,
    var action: () -> Unit = { }
)
