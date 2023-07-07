package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel

import com.tokopedia.kotlin.extensions.view.EMPTY

class BuyerCancellationProductUiModel(
    val shopName: String,
    val orderNumberLabel: String = String.EMPTY,
    val shopIcon: String,
    val invoiceNumber: String,
    val productName: String,
    val productThumbnailUrl: String,
    val moreProductInfo: String = String.EMPTY
)
