package com.tokopedia.tokofood.feature.purchase

import android.view.View
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.BaseTokoFoodPurchaseUiModel

const val ENABLED_ALPHA = 1.0f
const val DISABLED_ALPHA = 0.5f

fun String.removeDecimalSuffix(): String = this.removeSuffix(".00")

fun View.renderAlpha(element: BaseTokoFoodPurchaseUiModel) {
    alpha = if (element.isEnabled) {
        ENABLED_ALPHA
    } else {
        DISABLED_ALPHA
    }
}



