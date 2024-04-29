package com.tokopedia.navigation_common.ui

data class BottomNavBarUiModel(
    val id: Int,
    val title: String,
    val type: BottomNavBarItemType,
    val jumper: BottomNavBarJumper?,
    val assets: Map<String, BottomNavBarAsset>,
    val discoId: DiscoId,
) {
    val uniqueId = BottomNavItemId(type, discoId)
}

data class BottomNavBarJumper(
    val id: Int,
    val title: String,
    val toJumperAsset: BottomNavBarAsset,
    val idleAsset: BottomNavBarAsset,
    val toInitialAsset: BottomNavBarAsset,
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
