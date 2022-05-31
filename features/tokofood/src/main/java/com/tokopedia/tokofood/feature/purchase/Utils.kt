package com.tokopedia.tokofood.feature.purchase

import android.view.View
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.BaseTokoFoodPurchaseUiModel

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun View.renderAlpha(element: BaseTokoFoodPurchaseUiModel) {
    alpha = if (element.isEnabled) {
        1.0f
    } else {
        0.5f
    }
}

