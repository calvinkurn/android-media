package com.tokopedia.content.common.ui.model

import android.os.Parcelable
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Jonathan Darwin on April 13, 2022
 */

@Parcelize
data class ContentAccountUiModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val badge: String,
    val type: String,
    val hasUsername: Boolean,
    val hasAcceptTnc: Boolean,
): Parcelable {
    val isUser: Boolean
        get() = type == TYPE_USER

    val isShop: Boolean
        get() = type == TYPE_SHOP

    val isUserPostEligible: Boolean
        get() = isUser && hasAcceptTnc

    companion object {
        val Empty = ContentAccountUiModel(
            id = "",
            name = "",
            iconUrl = "",
            badge = "",
            type = "",
            hasUsername = false,
            hasAcceptTnc = false,
        )
    }
}
