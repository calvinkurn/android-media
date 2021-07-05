package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostPaidPopup(
        @SerializedName("title")
        @Expose
        var title: String? = null,

        @SerializedName("content")
        @Expose
        var content: String? = null,

        @SerializedName("image_url")
        @Expose
        var imageUrl: String? = null,

        @SerializedName("actions")
        @Expose
        var action: ActionPostPaidPopup? = null
) {
    data class ActionPostPaidPopup(
            @SerializedName("YES")
            @Expose
            var confirmAction: ActionConfirmPostPaidPopup? = null
    ) {
        data class ActionConfirmPostPaidPopup(
                @SerializedName("title")
                @Expose
                var title: String? = null
        )
    }

}