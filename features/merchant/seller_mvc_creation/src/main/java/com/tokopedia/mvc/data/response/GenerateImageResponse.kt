package com.tokopedia.mvc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenerateImageResponse(
    @SerializedName("imagenerator_generate_image")
    @Expose
    val imageGeneratorModel: ImageGeneratorModel = ImageGeneratorModel()
) {
    data class ImageGeneratorModel(
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
    )
}
