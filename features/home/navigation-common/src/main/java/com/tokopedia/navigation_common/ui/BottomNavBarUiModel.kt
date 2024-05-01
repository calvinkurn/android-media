package com.tokopedia.navigation_common.ui

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class BottomNavBarUiModel(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: BottomNavBarItemType,
    @SerializedName("jumper")
    val jumper: BottomNavBarJumper?,
    @SerializedName("assets")
    val assets: Map<String, BottomNavBarAsset>,
    @SerializedName("discoId")
    val discoId: DiscoId
) {
    val uniqueId = BottomNavItemId(type, discoId)
}

data class BottomNavBarJumper(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("toJumperAsset")
    val toJumperAsset: BottomNavBarAsset,
    @SerializedName("idleAsset")
    val idleAsset: BottomNavBarAsset,
    @SerializedName("toInitialAsset")
    val toInitialAsset: BottomNavBarAsset
)

fun BottomNavItemId(type: BottomNavBarItemType, discoId: DiscoId = DiscoId.Empty): BottomNavItemId {
    return BottomNavItemId("${type.value}_${discoId.value}")
}

@JvmInline
value class BottomNavItemId(val value: String) {
    val type: BottomNavBarItemType
        get() = BottomNavBarItemType(value.split("_")[0])

    val discoId: DiscoId
        get() = DiscoId(value.split("_")[1])
}

@JvmInline
value class DiscoId(val value: String) {
    companion object {
        val Empty = DiscoId("")
    }
}

@JvmInline
value class BottomNavBarItemType(val value: String)

sealed interface BottomNavBarAsset {

    val url: String

    @JvmInline
    value class Image(override val url: String) : BottomNavBarAsset

    @JvmInline
    value class Lottie(override val url: String) : BottomNavBarAsset {

        companion object {
            private val regex = Regex.fromLiteral("https://.*.json")
        }
    }
}
