package com.tokopedia.play.broadcaster.domain.model.imagegenerator

import com.google.gson.annotations.SerializedName

data class GetImageGeneratorGenerateImage(
    @SerializedName("imagenerator_generate_image")
    val imageGeneratorGenerateImage: ImageGeneratorGenerateImage = ImageGeneratorGenerateImage()
) {

    data class ImageGeneratorGenerateImage(
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("source_id")
        val sourceId: String = ""
    )
}
