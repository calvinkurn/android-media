package com.tokopedia.navigation_common.ui

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.tokopedia.navigation_common.ui.BottomNavBarAsset.Companion.Id

class BottomNavBarAsset {

    @JvmInline
    value class Key(val value: String) {

        companion object {
            val ImageActive = Key("selected_icon")
            val ImageInactive = Key("unselected_icon")
            val AnimActive = Key("active_icon")
            val AnimInactive = Key("inactive_icon")
        }

        override fun toString(): String {
            return value
        }
    }

    @JvmInline
    value class Variant(val value: String) {
        companion object {
            val Light = Variant("light_mode")
            val Dark = Variant("dark_mode")
        }

        override fun toString(): String {
            return value
        }
    }

    @JvmInline
    value class Id(val value: String) {
        override fun toString(): String {
            return value
        }
    }

    sealed interface Type {
        @JvmInline
        value class Image(val url: String) : Type

        @JvmInline
        value class ImageRes(@DrawableRes val res: Int) : Type

        @JvmInline
        value class Lottie(val url: String) : Type {

            companion object {
                private val regex = Regex.fromLiteral("https://.*.json")
            }
        }

        @JvmInline
        value class LottieRes(@RawRes val res: Int) : Type
    }

    companion object {
        fun Id(key: Key, variant: Variant): Id {
            return Id("${key}_${variant}")
        }
    }
}

operator fun BottomNavBarAsset.Key.plus(variant: BottomNavBarAsset.Variant): BottomNavBarAsset.Id {
    return Id(key = this, variant = variant)
}

