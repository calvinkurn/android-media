package com.tokopedia.play.view.type

/**
 * Created by jegul on 12/03/20
 */
sealed class ProductStock

object OutOfStock : ProductStock()
data class StockAvailable(val stock: Int) : ProductStock()
object ComingSoon: ProductStock()