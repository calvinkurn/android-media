package ai.advance.liveness.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessAppsModel(
        @SerializedName("title")
        @Expose
        val title: String? = null,

        @SerializedName("subtitle")
        @Expose
        val subtitle: String? = null,

        @SerializedName("button")
        @Expose
        val button: String? = null
)