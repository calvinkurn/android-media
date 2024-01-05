package com.tokopedia.feedplus.presentation.model

data class ContentCreationTypeItem(
    val id: Int = 0,
    val name: String,
    var drawableIconId: Int = 0,
    var isActive: Boolean = false,
    val type: CreateContentType,
    val applink: String = "",
    val weblink: String = "",
    val imageSrc: String = "",
    val creatorType: CreatorType
)

/**
 * position matter
 */
enum class CreateContentType(val value: String) {
    ShortVideo("shortvideo"),
    Post("post"),
    Live("livestream"),
    NONE("");

    companion object {
        fun getTypeByValue(value: String): CreateContentType {
            return CreateContentType.values()
                .firstOrNull {
                    it.value.equals(value, false)
                } ?: NONE
        }
    }
}
