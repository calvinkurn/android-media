package com.tokopedia.tokopedianow.home.presentation.model

data class HomeClaimCouponDataModel(
    val appLink: String = "",
    val code: String = "",
    val toasterDescription: String = "",
    val isError: Boolean = false
)
