package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionPointMetadata(
    @SerializedName("collection_point_id")
    @Expose
    val collectionPointId: String = "",
    @SerializedName("collection_point_version")
    @Expose
    val collectionPointVersion: String = "",
    @SerializedName("consent_payload")
    @Expose
    var consentPayload: String = ""
) : Parcelable
