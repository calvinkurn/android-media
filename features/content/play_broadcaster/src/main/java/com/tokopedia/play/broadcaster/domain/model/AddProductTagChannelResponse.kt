package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class AddProductTagChannelResponse(
        @SerializedName("broadcasterSetActiveProductTags")
        val productId: GetProductId
) {
    data class AddProductTagChannelData(

            @SerializedName("data")
            val data: AddProductTagChannelResponse
    )
    data class GetProductId(
            @SerializedName("productIDs")
            val productIds: List<String>
    )
}