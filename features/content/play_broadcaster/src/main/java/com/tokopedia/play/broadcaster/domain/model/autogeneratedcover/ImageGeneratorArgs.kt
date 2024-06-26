package com.tokopedia.play.broadcaster.domain.model.autogeneratedcover

data class ImageGeneratorArgs(private val imagePreviewUrl: String) {

    // SAMPLE
    //
    // imagePreviewUrl with 2 product (currently max product is 2):
    // -> https://imagenerator.tokopedia.com/v2/preview/LScDrk?
    //       background_color=blue&
    //       shop_logo=https://images.tokopedia.net/imglogo.png&
    //       product_amount=2&
    //       product_image_1=https://images.tokopedia.net/img1.jpg&
    //       product_image_2=https://images.tokopedia.net/img2.jpg
    //
    // imagePreviewUrl with 1 product:
    // -> https://imagenerator.tokopedia.com/v2/preview/LScDrk?
    //       background_color=blue&
    //       shop_logo=https://images.tokopedia.net/imglogo.png&
    //       product_amount=1&
    //       product_image_1=https://images.tokopedia.net/img1.jpg

    @OptIn(ExperimentalStdlibApi::class)
    fun getArgs(): List<Map<String, String>> {
        return buildList {
            add(mapOf(KEY to KEY_BACKGROUND_COLOR, VALUE to getBackgroundColor()))
            add(mapOf(KEY to KEY_SHOP_LOGO, VALUE to getLogo()))
            add(mapOf(KEY to KEY_PRODUCT_AMOUNT, VALUE to getProductAmount()))
            add(mapOf(KEY to KEY_PRODUCT_IMAGE_1, VALUE to getProductImage1()))
            if (getProductAmount() == MAX_SHOWING_PRODUCT) add(mapOf(KEY to KEY_PRODUCT_IMAGE_2, VALUE to getProductImage2()))
        }
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

    fun getCoverID(): String {
        return imagePreviewUrl.substringAfter("$SOURCE_ID/")
    }

    companion object {
        private const val KEY = "key"
        private const val VALUE = "value"
        private const val SOURCE_ID = "LScDrk"
        private const val MAX_SHOWING_PRODUCT = "2"
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val KEY_SHOP_LOGO = "shop_logo"
        private const val KEY_PRODUCT_IMAGE_1 = "product_image_1"
        private const val KEY_PRODUCT_IMAGE_2 = "product_image_2"
        private const val KEY_PRODUCT_AMOUNT = "product_amount"
    }

}
