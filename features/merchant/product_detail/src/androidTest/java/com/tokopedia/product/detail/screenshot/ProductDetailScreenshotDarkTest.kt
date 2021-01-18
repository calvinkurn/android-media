package com.tokopedia.product.detail.screenshot

/**
 * Created by Yehezkiel on 18/01/21
 */
class ProductDetailScreenshotDarkTest : BaseProductDetailScreenShotTest() {

    override fun forceDarkMode(): Boolean = true

    override fun filePrefix(): String = "product_detail-dark"

}