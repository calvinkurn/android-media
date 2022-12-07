package com.tokopedia.epharmacy.network.request

import com.google.gson.annotations.SerializedName

data class PrescriptionRequest(
    @SerializedName("prescription_id")
    val prescriptionId: Long?
)