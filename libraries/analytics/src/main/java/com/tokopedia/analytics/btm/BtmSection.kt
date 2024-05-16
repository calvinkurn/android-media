package com.tokopedia.analytics.btm

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


object Tokopedia : Site("a87943") {
    object Home : Page("b83792") {
        object Banners : Block("c0652") {
            object Banner: Position("d4396")
        }
    }

    object Feed : Page("b3679")
    object Pdp : Page("b2815")
}


fun main() {
    println("PDP: " + Tokopedia.Pdp.str)
    println("Banners: " + Tokopedia.Home.Banners.str)
    println("Banner's Banner: " + Tokopedia.Home.Banners.Banner.str)
}
