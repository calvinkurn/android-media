package com.tokopedia.content.common.ui.model

import android.os.Parcelable
import com.tokopedia.content.analytic.Value
import com.tokopedia.content.analytic.model.ContentAnalyticAuthor
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_UNKNOWN
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
    val type: String,
    val hasUsername: Boolean,
    val hasAcceptTnc: Boolean,
    val enable: Boolean,
): Parcelable {
    val isUser: Boolean
        get() = type == TYPE_USER

    val isShop: Boolean
        get() = type == TYPE_SHOP

    val isUnknown: Boolean
        get() = type.isEmpty() || type == TYPE_UNKNOWN

    val isUserPostEligible: Boolean
        get() = isUser && enable

    fun toAnalyticModel() = ContentAnalyticAuthor(
        id = id,
        type = when {
            isShop -> Value.seller
            isUser -> Value.user
            else -> ""
        }
    )

    companion object {
        val Empty = ContentAccountUiModel(
            id = "",
            name = "",
            iconUrl = "",
            type = "",
            hasUsername = false,
            hasAcceptTnc = false,
            enable = false,
        )
    }
}

fun ContentAccountUiModel?.orUnknown(): ContentAccountUiModel = this ?: ContentAccountUiModel.Empty
