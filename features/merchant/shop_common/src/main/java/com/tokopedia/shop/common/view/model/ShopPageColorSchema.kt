package com.tokopedia.shop.common.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopPageColorSchema(
    val listColorSchema: List<ColorSchema> = listOf()
): Parcelable{
    enum class ColorSchemaName(val value: String) {
        TEXT_HIGH_EMPHASIS("textColorHighEmphasis"),
        TEXT_COLOR_EMPHASIS("textColorLowEmphasis"),
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

}
