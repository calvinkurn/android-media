package com.tokopedia.vouchergame.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.product.CatalogData

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameDetailData: CatalogData() {
        override var product: VoucherGameProductData = VoucherGameProductData()

        class Response {
                @SerializedName("rechargeCatalogProductInput")
                @Expose
                val response: VoucherGameDetailData = VoucherGameDetailData()
        }
}