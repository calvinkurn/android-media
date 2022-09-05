package com.tokopedia.tokofood.common.util

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel


private const val MINI_CART_STATE_KEY = "mini_cart_state_key"

fun SavedStateHandle.setMiniCartState(miniCartUiModel: MiniCartUiModel) {
    this[MINI_CART_STATE_KEY] = miniCartUiModel
}

fun SavedStateHandle.getMiniCartState(): MiniCartUiModel {
    return get(MINI_CART_STATE_KEY) as? MiniCartUiModel ?: MiniCartUiModel()
}