package com.tokopedia.createpost.view.bottomSheet

import androidx.annotation.DrawableRes


data class SearchTypeData (
    val id: Int = 0,
    val text: String = "",
    val type: String = "",
    @DrawableRes
    val iconId: Int = 0,
    val iconUrl: String = ""
)
class CreatorListItemType {
    companion object{
        const val TYPE_HEADER = "header_type"
        const val TYPE_CONTENT = "content_type"
    }

}
