package com.tokopedia.flight.cancellationV2.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 16/06/2020
 */
data class CancellationAttachmentUploadEntity(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attribute = Attribute()
) {
    class Attribute(@SerializedName("is_uploaded")
                    @Expose
                    val isUploaded: Boolean = false)
}