package com.tokopedia.play.broadcaster.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class AddProductTagChannelResponse(
    @SerializedName("broadcasterSetActiveProductTags")
    val productId: GetProductId
) {
    data class GetProductId(
        @SuppressLint("Invalid Data Type")
        @SerializedName("productIDs")
        val productIds: List<String>
    )
}
