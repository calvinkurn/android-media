package com.tokopedia.play.broadcaster.domain.model.imagegenerator

data class ImageGeneratorArgs(
    val backgroundColor: String,
    val logo: String,
    val productUrl1: String,
    val productUrl2: String,
    val productAmount: String,
) {

    fun generateImage(): String {
//        val host = "https://imagenerator-staging.tokopedia.com" //staging
        val host = "https://imagenerator.tokopedia.com" //prod
        val sourceId = "LScDrk"
        val path = "v2/preview"
        val previewImage =
            "$host/$path/$sourceId?background_color=$backgroundColor&shop_logo=$logo&" +
                "product_amount=$productAmount&product_image_1=$productUrl1"
        return if (productAmount == "2") "$previewImage&product_image_2=$productUrl2" else previewImage
    }

    fun getArg(): List<Map<String, String>> {
        val args = mutableListOf<Map<String, String>>()
        args.add(mapOf("key" to "background_color", "value" to backgroundColor))
        args.add(mapOf("key" to "shop_logo", "value" to logo))
        args.add(mapOf("key" to "product_image_1", "value" to productUrl1))
        args.add(mapOf("key" to "product_image_2", "value" to productUrl2))
        args.add(mapOf("key" to "product_amount", "value" to productAmount))
        return args
    }

}
