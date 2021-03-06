package com.tokopedia.digital_checkout.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

/**
 * @author by jessica on 20/01/21
 */
data class RequestBodyOtpSuccess (
        @SerializedName("type")
        @Expose
        private var type: String,

        @SerializedName("id")
        @Expose
        private val id: String,

        @SerializedName("attributes")
        @Expose
        private val attributes: Attributes
) {
    data class Attributes(
            @SerializedName("ip_address")
            @Expose
            private var ipAddress: String,

            @SerializedName("user_agent")
            @Expose
            private val userAgent: String,

            @SerializedName("identifier")
            @Expose
            private val identifier: RequestBodyIdentifier
    )
}