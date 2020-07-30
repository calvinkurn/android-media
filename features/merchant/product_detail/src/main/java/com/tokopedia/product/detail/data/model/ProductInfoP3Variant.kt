package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.common.data.model.carttype.CartRedirectionResponse
import com.tokopedia.variant_common.model.VariantMultiOriginResponse

/**
 * Created by Yehezkiel on 2020-03-17
 */
data class ProductInfoP3Variant(
        var cartRedirectionResponse: CartRedirectionResponse? = null
)