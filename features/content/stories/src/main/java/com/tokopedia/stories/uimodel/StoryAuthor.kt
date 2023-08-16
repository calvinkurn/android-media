package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 08/08/23
 */
sealed class StoryAuthor {
    abstract val id: String
    abstract val type: AuthorType //TODO() check if its available in common
    abstract val thumbnailUrl: String
    abstract val name: String

    data class Shop(val shopName: String, val shopId: String, val avatarUrl: String) : //TODO : add badge url for shop
        StoryAuthor() {
        override val id: String
            get() = shopId
        override val type: AuthorType
            get() = AuthorType.Seller
        override val thumbnailUrl: String
            get() = avatarUrl
        override val name: String
            get() = shopName
    }

    data class Buyer(val userName: String, val userId: String, val avatarUrl: String) : StoryAuthor() {
        override val id: String
            get() = userId
        override val type: AuthorType
            get() = AuthorType.User
        override val thumbnailUrl: String
            get() = avatarUrl
        override val name: String
            get() = userName
    }

    object Unknown : StoryAuthor() {
        override val id: String
            get() = ""
        override val type: AuthorType
            get() = AuthorType.Unknown
        override val thumbnailUrl: String
            get() = ""
        override val name: String
            get() = ""
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
