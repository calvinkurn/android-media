package com.tokopedia.variant_common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

/**
 * Created by Yehezkiel on 08/03/20
 */

data class GetProductVariantResponse(
        @SerializedName("getProductVariant")
        @Expose
        val data: ProductVariant = ProductVariant()
)
