package com.tokopedia.byteplus.effect.model

/**
 * Created By : Jonathan Darwin on March 28, 2023
 */
data class Preset(
    val key: String,
    val value: Float,
) {
    val isUnknown: Boolean
        get() = this == Unknown

    companion object {
        val Unknown: Preset
            get() = Preset(
                key = "",
                value = 0f
            )
    }
}
