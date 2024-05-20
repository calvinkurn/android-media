package com.tokopedia.analytics.btm

object Tokopedia : Site("a87943") {
    object HomePage : Page("b83792") {
        object Banners : Block("c0652") {
            object Banner: Position("d4396")
        }
    }

    object Feed : Page("b3679")

    object Wishlist: Page("b0751")

    object OfficialStore: Page("b9256")

    object Transaction: Page("b0775")

    // SRP

    object ProductSearchResult: Page("b4850")

    object ShopSearchResult: Page("b98217")

    // Transaction

    object OrderList: Page("b1605")

    object Pdp : Page("b2815")

    object Sku : Page("b8098")

    object Cart : Page("b2906")

    object PgCheckout : Page("b69823")

    object PgThankYou : Page("b4596")

    object DgThankYou : Page("b2444")

    object DgCheckout : Page("b1181")
}

open class Site(val code: String) {
    open inner class Page(protected val code: String) {
        val str: String
            get() = "${this@Site.code}.$code"

        open inner class Block(protected val code: String) {
            val str: String
                get() = "${this@Page.str}.$code"

            open inner class Position(protected val code: String) {
                val str: String
                    get() = "${this@Block.str}.$code"
            }
        }
    }
}
