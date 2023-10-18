package com.tokopedia.feedplus.presentation.model

data class ContentCreationItem(
    var id: String = "",
    var name: String = "",
    var type: CreatorType = CreatorType.NONE,
    var image: String = "",
    var hasUsername: Boolean = false,
    var hasAcceptTnC: Boolean = false,
    var items: List<ContentCreationTypeItem> = emptyList()
)

/**
 * position matter
 */
enum class CreatorType(val value: String) {
    USER("content-user"),
    SHOP("content-shop"),
    NONE("");

    val asBuyer: Boolean get() {
        return this == USER
    }

    companion object {
        fun getTypeByValue(value: String): CreatorType {
            return CreatorType.values().firstOrNull { it.value == value } ?: NONE
        }
    }
}
