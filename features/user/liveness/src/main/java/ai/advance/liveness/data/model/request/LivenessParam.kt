package ai.advance.liveness.data.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessParam (
    @SerializedName("kyc_type")
    @Expose
    val kycType: String,

    @SerializedName("param")
    @Expose
    val param: String
)