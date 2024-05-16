@file:SuppressLint("Invalid Data Type")

package com.tokopedia.navigation_common.ui

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class BottomNavBarUiModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: BottomNavBarItemType,
    @SerializedName("jumper")
    val jumper: BottomNavBarJumper?,
    @SerializedName("assets")
    val assets: Map<BottomNavBarAsset.Id, BottomNavBarAsset.Type>,
    @SerializedName("discoId")
    val discoId: DiscoId,
    @SerializedName("queryParams")
    val queryParams: String
) {
    val uniqueId = BottomNavItemId(type, discoId)
}

data class BottomNavBarJumper(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("assets")
    val assets: Map<BottomNavBarAsset.Id, BottomNavBarAsset.Type>
)

fun BottomNavItemId(type: BottomNavBarItemType, discoId: DiscoId = DiscoId.Empty): BottomNavItemId {
    val id = buildString {
        append(type.value)
        if (discoId == DiscoId.Empty) return@buildString

        append(BottomNavItemId.DELIMITER)
        append(discoId.value)
    }
    return BottomNavItemId(id)
}

@JvmInline
value class BottomNavItemId(val value: String) {

    val type: BottomNavBarItemType
        get() {
            return BottomNavBarItemType(
                if (value.contains(DELIMITER)) {
                    value.split(DELIMITER)[0]
                } else {
                    value
                }
            )
        }

    val discoId: DiscoId
        get() {
            return if (value.contains(DELIMITER)) {
                DiscoId(value.split(DELIMITER)[1])
            } else {
                DiscoId.Empty
            }
        }

    companion object {
        internal const val DELIMITER = "-*-"
    }
}

@JvmInline
value class DiscoId(val value: String) {
    companion object {
        val Empty = DiscoId("")
    }
}

@JvmInline
value class BottomNavBarItemType(val value: String)
