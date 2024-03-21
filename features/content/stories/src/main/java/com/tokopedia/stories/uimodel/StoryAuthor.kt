package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 08/08/23
 */

sealed class StoryAuthor {
    abstract val id: String
    abstract val type: AuthorType
    abstract val thumbnailUrl: String
    abstract val name: String
    abstract val appLink: String

    data class Shop(
        val shopName: String,
        val shopId: String,
        val avatarUrl: String,
        val badgeUrl: String,
        override val appLink: String
    ) : StoryAuthor() {
        override val id: String
            get() = shopId
        override val type: AuthorType
            get() = AuthorType.Seller
        override val thumbnailUrl: String
            get() = avatarUrl
        override val name: String
            get() = shopName
    }

    data class Buyer(
        val userName: String,
        val userId: String,
        val avatarUrl: String,
        override val appLink: String
    ) : StoryAuthor() {
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
        override val appLink: String
            get() = ""
    }
}

enum class AuthorType(val value: Int, val type: String) {
    Tokopedia(1, "-"),
    Seller(2, "shop"),
    User(3, "user"),
    Unknown(-1, "");

    companion object {

        private val values = values()
        fun convertValue(value: Int): AuthorType {
            return values
                .firstOrNull { it.value == value } ?: Unknown
        }

        fun getByType(type: String): AuthorType {
            return values
                .firstOrNull { it.type == type } ?: Unknown
        }
    }
}
