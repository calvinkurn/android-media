package com.tokopedia.play.broadcaster.domain.model.imagegenerator

data class ImageGeneratorArgs(private val imagePreviewUrl: String) {

    fun getArg(): List<Map<String, String>> {
        val args = mutableListOf<Map<String, String>>()
        args.add(mapOf(KEY to KEY_BACKGROUND_COLOR, VALUE to getBackgroundColor()))
        args.add(mapOf(KEY to KEY_SHOP_LOGO, VALUE to getLogo()))
        args.add(mapOf(KEY to KEY_PRODUCT_AMOUNT, VALUE to getProductAmount()))
        args.add(mapOf(KEY to KEY_PRODUCT_IMAGE_1, VALUE to getProductImage1()))
        if (getProductAmount() == "2") args.add(mapOf(KEY to KEY_PRODUCT_IMAGE_2, VALUE to getProductImage2()))
        return args
    }

    private fun getBackgroundColor(): String {
        return imagePreviewUrl.substringAfter("$KEY_BACKGROUND_COLOR=").substringBefore("&")
    }

    private fun getLogo(): String {
        return imagePreviewUrl.substringAfter("$KEY_SHOP_LOGO=").substringBefore("&")
    }

    private fun getProductAmount(): String {
        return imagePreviewUrl.substringAfter("$KEY_PRODUCT_AMOUNT=").substringBefore("&")
    }

    private fun getProductImage1(): String {
        return imagePreviewUrl.substringAfter("$KEY_PRODUCT_IMAGE_1=").substringBefore("&")
    }

    private fun getProductImage2(): String {
        return imagePreviewUrl.substringAfter("$KEY_PRODUCT_IMAGE_2=").substringBefore("&")
    }

    companion object {
        private const val KEY = "key"
        private const val VALUE = "value"
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val KEY_SHOP_LOGO = "shop_logo"
        private const val KEY_PRODUCT_IMAGE_1 = "product_image_1"
        private const val KEY_PRODUCT_IMAGE_2 = "product_image_2"
        private const val KEY_PRODUCT_AMOUNT = "product_amount"
    }

}
