package com.tokopedia.minicart.ui.widget.uimodel

data class MiniCartWidgetUiModel(
        var state: Int = 0,
        var totalProductCount: Int = 0,
        var totalProductError: Int = 0,
        var totalProductPrice: Long = 0
) {
    companion object {
        const val STATE_NORMAL = 0
        const val STATE_LOADING = 1
    }
}