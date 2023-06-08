package com.tokopedia.feedplus.presentation.model.type

/**
 * Created by meyta.taliti on 24/05/23.
 */
enum class AuthorType(val value: Int) {
    Tokopedia(1),
    Shop(2),
    User(3),
    Unknown(0);

    val isShop: Boolean
        get() = this == Shop

    val isUser: Boolean
        get() = this == User

    companion object {
        infix fun from(value: Int): AuthorType {
            return AuthorType.values()
                .firstOrNull {
                    it.value == value
                } ?: Unknown
        }
    }
}
