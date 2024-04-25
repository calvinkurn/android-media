package com.tokopedia.navigation_common.ui

data class BottomNavBarUiModel(
    val id: Int,
    val title: String,
    val type: BottomNavBarItemType,
    val jumper: BottomNavBarJumper?,
    val assets: Map<String, BottomNavBarAsset>,
)

data class BottomNavBarJumper(
    val id: Int,
    val title: String,
    val toJumperAsset: BottomNavBarAsset,
    val idleAsset: BottomNavBarAsset,
    val toInitialAsset: BottomNavBarAsset,
)

@JvmInline
value class BottomNavBarItemType(val value: String) {

    companion object {
        val Home = BottomNavBarItemType("home")
        val Feed = BottomNavBarItemType("feed")
        val DiscoPage = BottomNavBarItemType("discopage")
        val Wishlist = BottomNavBarItemType("wishlist")
    }
}

sealed interface BottomNavBarAsset {

    val url: String
    @JvmInline
    value class Image(override val url: String) : BottomNavBarAsset

    @JvmInline
    value class Lottie(override val url: String) : BottomNavBarAsset {

        companion object {
            private val regex = Regex.fromLiteral("https://.*.json")
        }
    }
}
