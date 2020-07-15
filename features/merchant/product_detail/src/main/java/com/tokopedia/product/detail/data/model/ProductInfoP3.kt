package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.AddressModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText
import com.tokopedia.variant_common.model.VariantMultiOriginWarehouse

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var ratesModel: RatesModel? = null,
        var userCod: Boolean = false,
        var addressModel: AddressModel? = null,
        var multiOrigin: VariantMultiOriginWarehouse? = null
)