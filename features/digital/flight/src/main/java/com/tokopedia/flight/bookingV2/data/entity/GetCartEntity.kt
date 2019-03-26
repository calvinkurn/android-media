package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.search.data.api.single.response.Meta

/**
 * @author by furqan on 04/03/19
 */
class GetCartEntity (
        @SerializedName("type")
        @Expose
        var type: String = "",
        @SerializedName("id")
        @Expose
        var cartId: String = "",
        @SerializedName("attributes")
        @Expose
        var attributes: BookingAttributeEntity = BookingAttributeEntity(),
        @SerializedName("meta")
        @Expose
        var meta: Meta = Meta()
)