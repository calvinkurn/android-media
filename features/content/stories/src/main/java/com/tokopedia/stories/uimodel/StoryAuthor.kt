package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 08/08/23
 */
sealed class StoryAuthor {
    abstract val id: String
    abstract val type: AuthorType //TODO() check if its available in common

    data class Shop(val shopId: String) : StoryAuthor() {
        override val id: String
            get() = shopId
        override val type: AuthorType
            get() = AuthorType.Seller
    }

    data class Buyer(val userId: String) : StoryAuthor() {
        override val id: String
            get() = userId
        override val type: AuthorType
            get() = AuthorType.User
    }
}

enum class AuthorType(val value: Int) {
    Tokopedia(1),
    Seller(2),
    User(3),
    Unknown(-1);

    companion object {
        fun convertValue(value: Int): AuthorType {
            return AuthorType.values()
                .firstOrNull { it.value == value } ?: Unknown
        }
    }
}
