package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.AddressModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var ratesModel: RatesModel? = null,
        var userCod: Boolean = false,
        var addressModel: AddressModel? = null,
        var multiOrigin: MultiOriginWarehouse? = null
)