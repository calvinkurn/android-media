package com.tokopedia.content.common.types

object ContentCommonUserType {
    const val KEY_AUTHOR_TYPE = "author_type"
    const val TYPE_NAME_SELLER = "seller"
    const val TYPE_NAME_USER = "user"
    const val TYPE_USER = "content-user"
    const val TYPE_SHOP = "content-shop"
    const val TYPE_UNKNOWN = "content-unknown"
    const val VALUE_TYPE_ID_SHOP = 2
    const val VALUE_TYPE_ID_USER = 3

    fun getUserType(type: String): Int {
        return when (type) {
            TYPE_USER -> VALUE_TYPE_ID_USER
            TYPE_SHOP -> VALUE_TYPE_ID_SHOP
            else -> 0
        }
    }

    fun getUserType(type: Int): String {
        return when (type) {
            VALUE_TYPE_ID_USER -> TYPE_USER
            VALUE_TYPE_ID_SHOP -> TYPE_SHOP
            else -> ""
        }
    }
}
