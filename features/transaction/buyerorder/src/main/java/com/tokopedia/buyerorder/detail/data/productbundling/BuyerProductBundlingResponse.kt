package com.tokopedia.buyerorder.detail.data.productbundling

import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerProductBundlingUiModel

data class BuyerProductBundlingResponse(
        @SerializedName("bundle_detail")
        val bundleDetail: BuyerProductBundlingDetail = BuyerProductBundlingDetail()
)

data class BuyerProductBundlingDetail(
        @SerializedName("bundle")
        val bundleList: List<BuyerProductBundlingUiModel> = listOf()
)
