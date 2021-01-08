package com.tokopedia.digital_checkout.data.response.getcart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/01/21
 */

data class PopUpData(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("content")
        @Expose
        val content: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("action")
        @Expose
        val action: PopUpAction = PopUpAction()
) {
    data class PopUpAction(
            @SerializedName("yes_button_title")
            @Expose
            val yesButtonTitle: String = ""
    )
}