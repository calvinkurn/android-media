package com.tokopedia.explore.view.type

/**
 * Created by jegul on 2019-11-13
 */
enum class ExploreCardType(val typeString: String) {

    Multi("multi"),
    Video("video"),
    Youtube("youtube"),
    Unknown("");

    companion object {
        private val values = values()

        @JvmStatic
        fun getCardTypeByString(typeString: String): ExploreCardType {
            for (type in values) {
                if (type.typeString == typeString) return type
            }
            return Unknown
        }
    }
}