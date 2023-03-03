package com.tokopedia.feedplus.presentation.model

data class ContentCreationTypeItem(
    val id: Int = 0,
    val name: String,
    var drawableIconId: Int = 0,
    var isActive: Boolean?,
    val type: CreateContentType,
    val applink: String = "",
    val weblink: String = "",
    val imageSrc: String = ""
)

enum class CreateContentType(val value: String) {
    CREATE_LIVE("livestream"),
    CREATE_POST("post"),
    CREATE_SHORT_VIDEO("shortvideo"),
    NONE("")
}
