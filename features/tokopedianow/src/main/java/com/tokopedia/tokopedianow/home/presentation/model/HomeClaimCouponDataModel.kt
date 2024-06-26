package com.tokopedia.tokopedianow.home.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class HomeClaimCouponDataModel(
    val appLink: String = String.EMPTY,
    val code: String = String.EMPTY,
    val toasterDescription: String = String.EMPTY,
    val isError: Boolean = false,
    val widgetId: String = String.EMPTY,
    val catalogId: String = String.EMPTY,
    val couponStatus: String = String.EMPTY,
    val position: Int = Int.ZERO,
    val slugText: String = String.EMPTY,
    val couponName: String = String.EMPTY,
    val warehouseId: String = String.EMPTY
)
