package com.tokopedia.universal_sharing.model

import com.google.gson.annotations.SerializedName

data class ImageGeneratorModel(
    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("source_id")
    val sourceId: String = ""
) {
    data class Response(
        @SerializedName("imagenerator_generate_image")
        val imageGeneratorModel: ImageGeneratorModel = ImageGeneratorModel()
    )

}
