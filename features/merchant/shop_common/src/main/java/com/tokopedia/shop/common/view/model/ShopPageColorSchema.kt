package com.tokopedia.shop.common.view.model

import android.graphics.Color
import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ShopPageColorSchema(
    val listColorSchema: @RawValue List<ColorSchema> = listOf()
): Parcelable {
    @Parcelize
    enum class ColorSchemaName(val value: String) : Parcelable{
        TEXT_HIGH_EMPHASIS("textColorHighEmphasis"),
        TEXT_LOW_EMPHASIS("textColorLowEmphasis"),
        DISABLED_TEXT_COLOR("disabledTextColor"),
        CTA_TEXT_LINK_COLOR("ctaTextLinkColor"),
        ICON_ENABLED_HIGH_COLOR("iconEnableHighColor"),
        ICON_CTA_LINK_COLOR("iconCtaLinkColor"),
        BG_PRIMARY_COLOR("bgPrimaryColor"),
        DIVIDER("divider"),
    }
    @Parcelize
    data class ColorSchema(
        val name: String = "",
        val type: String = "",
        val value: String = ""
    ): Parcelable

    fun getColorSchema(colorSchemaName: ColorSchemaName): ColorSchema? {
        return listColorSchema.firstOrNull { it.name == colorSchemaName.value }
    }

    fun getColorIntValue(colorSchemaName: ColorSchemaName): Int {
        return try {
            Color.parseColor(listColorSchema.firstOrNull { it.name == colorSchemaName.value }?.value.orEmpty())
        } catch (e: Exception) {
            //TODO need to add default color from unify, but need to pass context on param
            Int.ZERO
        }
    }

}
