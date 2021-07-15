package com.tokopedia.sellerorder.list.presentation.models

/**
 * Created By @ilhamsuaib on 14/07/21
 */

data class BundleDetailUiModel(
        val productCount: Int = 0,
        val bundle: List<BundleProductUiModel> = emptyList()
)

data class BundleProductUiModel(
        val bundleId: String = "0",
        val orderDetail: List<SomListOrderUiModel.OrderProduct> = emptyList()
)