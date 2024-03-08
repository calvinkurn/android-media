package com.tokopedia.cart.view.uimodel

data class DisabledReasonHolderData(
    var title: String = "",
    var subTitle: String = "",
    var productsCount: Long = 0,
    var showOutOfCoverageTitle: String = "",
    var isShowOutOfCoverageAction: Boolean = false
)
