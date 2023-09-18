package com.tokopedia.creation.common.presentation.model

/**
 * Created By : Muhammad Furqan on 06/09/23
 */
enum class ContentCreationTypeEnum(val value: String) {
    SHORT(value = "shortvideo"),
    LIVE(value = "livestream"),
    POST(value = "post"),
    STORY(value = "story");

    companion object {
        fun getTypeByValue(value: String): ContentCreationTypeEnum? =
            ContentCreationTypeEnum.values()
                .firstOrNull {
                    it.value.equals(value, false)
                }
    }
}

enum class ContentCreationAuthorEnum(val value: String) {
    USER("content-user"),
    SHOP("content-shop"),
    NONE("none");

    val asBuyer: Boolean
        get() {
            return this == USER
        }

    companion object {
        fun getTypeByValue(value: String): ContentCreationAuthorEnum {
            return ContentCreationAuthorEnum.values().firstOrNull { it.value == value } ?: NONE
        }
    }

}
