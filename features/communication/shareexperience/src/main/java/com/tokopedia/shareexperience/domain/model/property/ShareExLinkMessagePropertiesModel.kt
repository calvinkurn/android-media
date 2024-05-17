package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import com.tokopedia.shareexperience.domain.model.ShareExMessagePlaceholderEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExLinkMessagePropertiesModel(
    val message: String = "",
    val replacementMap: MutableMap<ShareExMessagePlaceholderEnum, String> = mutableMapOf()
): Parcelable {
    fun getFinalMessage(): String {
        var result = message
        for ((placeholderEnum, replacement) in replacementMap) {
            result = result.replace(placeholderEnum.placeholder, replacement, ignoreCase = true)
        }
        return result
    }

    fun updateReplacementMap(key: ShareExMessagePlaceholderEnum, value: String) {
        replacementMap[key] = value
    }
}
