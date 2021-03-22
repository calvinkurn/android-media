package com.tokopedia.play.view.type

/**
 * Created by jegul on 19/02/21
 */
sealed class PlaySource {

    abstract val key: String

    data class Shop(val sourceId: String) : PlaySource() {
        override val key: String = SOURCE_TYPE_SHOP
    }
    object Home : PlaySource() {
        override val key: String = SOURCE_TYPE_HOME
    }
    object Feeds : PlaySource() {
        override val key: String = SOURCE_TYPE_FEEDS
    }
    object DiscoveryPage : PlaySource() {
        override val key: String = SOURCE_TYPE_DISCOVERY_PAGE
    }
    object CLP : PlaySource() {
        override val key: String = SOURCE_TYPE_CLP
    }
    object PlayHome : PlaySource() {
        override val key: String = SOURCE_TYPE_PLAY_HOME
    }
    object Unknown : PlaySource() {
        override val key: String = ""
    }

    companion object {

        private const val SOURCE_TYPE_SHOP = "SHOP"
        private const val SOURCE_TYPE_HOME = "HOME"
        private const val SOURCE_TYPE_FEEDS = "FEEDS"
        private const val SOURCE_TYPE_DISCOVERY_PAGE = "DISCOPAGE"
        private const val SOURCE_TYPE_CLP = "CLP"
        private const val SOURCE_TYPE_PLAY_HOME = "PLAY_HOME"

        fun getBySource(sourceType: String, sourceId: String? = null): PlaySource {
            return when (sourceType) {
                SOURCE_TYPE_SHOP -> {
                    Shop(sourceId.orEmpty())
                }
                SOURCE_TYPE_HOME -> Home
                SOURCE_TYPE_FEEDS -> Feeds
                SOURCE_TYPE_DISCOVERY_PAGE -> DiscoveryPage
                SOURCE_TYPE_CLP -> CLP
                SOURCE_TYPE_PLAY_HOME -> PlayHome
                else -> Unknown
            }
        }
    }
}