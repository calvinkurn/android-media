package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GenerateImageResponse(
    @SerializedName("imagenerator_generate_image")
    val imageneratorGenerateImage: ImageneratorGenerateImage = ImageneratorGenerateImage()
) {
    data class ImageneratorGenerateImage(
        @SerializedName("image_url")
        val imageUrl: String = ""
    )
}