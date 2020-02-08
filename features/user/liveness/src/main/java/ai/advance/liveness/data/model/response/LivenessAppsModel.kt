package ai.advance.liveness.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LivenessAppsModel(
        @SerializedName("title")
        @Expose
        var title: String? = null,

        @SerializedName("subtitle")
        @Expose
        var subtitle: String? = null,

        @SerializedName("button")
        @Expose
        var button: String? = null
)