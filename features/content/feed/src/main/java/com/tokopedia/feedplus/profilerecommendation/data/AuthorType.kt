package com.tokopedia.feedplus.profilerecommendation.data

/**
 * Created by jegul on 2019-09-19.
 */
enum class AuthorType(val typeString: String) {

    SHOP("shop"),
    USER("user");

    companion object {
        val values = values()

        fun findTypeByString(typeString: String): AuthorType? {
            return values.firstOrNull { it.typeString == typeString }
        }
    }
}