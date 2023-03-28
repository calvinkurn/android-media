package com.tokopedia.effect.model

/**
 * Created By : Jonathan Darwin on March 28, 2023
 */
data class Preset(
    val key: String
) {
    val isUnknown: Boolean
        get() = this == Unknown

    companion object {
        val Unknown: Preset
            get() = Preset(
                key = ""
            )
    }
}
