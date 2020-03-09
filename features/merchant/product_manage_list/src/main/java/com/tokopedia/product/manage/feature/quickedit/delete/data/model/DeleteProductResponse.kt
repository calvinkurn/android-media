package com.tokopedia.product.manage.feature.quickedit.delete.data.model

import com.google.gson.annotations.SerializedName

data class DeleteProductResponse(
        @SerializedName("RemoveProductMenu")
        val productMenuResponse: ProductMenuResponse
)