package ai.advance.liveness.data.model.response

import com.google.gson.annotations.SerializedName

data class LivenessResponse (
    @SerializedName("data")
    val data: LivenessData? = null
)