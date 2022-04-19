package com.tokopedia.product.detail.screenshot

/**
 * Created by Yehezkiel on 10/01/21
 */
class ProductDetailScreenshotLightTest : BaseProductDetailScreenShotTest() {

    override fun forceDarkMode(): Boolean = false

    override fun filePrefix(): String = "product_detail-light"

}