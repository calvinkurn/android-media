package com.tokopedia.tokofood.purchase

import android.view.View
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.BaseUiModel

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun View.renderAlpha(element: BaseUiModel) {
    alpha = if (element.isDisabled) {
        0.5f
    } else {
        1.0f
    }
}

