package com.tokopedia.play.broadcaster.domain.model.imagegenerator

data class ImageGeneratorArgs(
    val backgroundColor: String,
    val logo: String,
    val productUrl1: String,
    val productUrl2: String,
    val productAmount: String,
) {

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
