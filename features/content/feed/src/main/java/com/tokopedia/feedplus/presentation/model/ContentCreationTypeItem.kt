package com.tokopedia.feedplus.presentation.model

import android.graphics.drawable.Drawable

data class ContentCreationTypeItem (
    val id: Int = 0,
    val name: String,
    var drawable: Drawable? = null,
    var isActive: Boolean?,
    val type: CreateContentType,
    val applink: String? = null,
    val weblink: String? = null,
    val imageSrc: String? = null
)

enum class CreateContentType(val value: String) {
    CREATE_LIVE("livestream"),
    CREATE_POST("post"),
    CREATE_SHORT_VIDEO("shortvideo"),
    NONE(""),
}
