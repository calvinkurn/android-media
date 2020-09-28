package com.tokopedia.play.broadcaster.type

/**
 * Created by jegul on 10/07/20
 */
sealed class ProductStock

object OutOfStock : ProductStock()
data class StockAvailable(val stock: Int) : ProductStock()