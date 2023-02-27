package com.tokopedia.campaign.data.response

import com.google.gson.annotations.SerializedName

data class ImageGeneratorResponse(
    @SerializedName("imagenerator_generate_image")
    val image: Image = Image()
) {
    data class Image(
        @SerializedName("image_url")
        val imageUrl: String = ""
    )
}
