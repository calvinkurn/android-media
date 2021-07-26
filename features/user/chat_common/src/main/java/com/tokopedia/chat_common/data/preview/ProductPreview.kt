package com.tokopedia.chat_common.data.preview

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toLongOrZero

open class ProductPreview constructor(
        val id: String = "",
        val imageUrl: String = "",
        val name: String = "",
        val price: String = "",
        val colorVariantId: String = "",
        val colorVariant: String = "",
        val colorHexVariant: String = "",
        val sizeVariantId: String = "",
        val sizeVariant: String = "",
        val url: String = "",
        val productFsIsActive: Boolean = false,
        val productFsImageUrl: String = "",
        val priceBefore: String = "",
        val priceBeforeInt: Int = 0,
        val dropPercentage: String = "",
        val isActive: Boolean = true,
        val remainingStock: Int = 1
) {

    val status: Int get() = if (isActive) 1 else 0

    fun notEnoughRequiredData(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty() || id.isEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return colorVariant.isEmpty() && sizeVariant.isEmpty()
    }

    fun hasColorVariant(): Boolean = colorVariant.isNotEmpty() && colorHexVariant.isNotEmpty()

    fun hasSizeVariant(): Boolean = sizeVariant.isNotEmpty()

    fun generateVariantRequest(): JsonArray {
        val list = JsonArray()

        if (hasColorVariant()) {
            val color = JsonObject()
            val colorOption = JsonObject()
            colorOption.addProperty("id", colorVariantId.toLongOrZero())
            colorOption.addProperty("value", colorVariant)
            colorOption.addProperty("hex", colorHexVariant)
            color.add("option", colorOption)
            list.add(color)
        }

        if (hasSizeVariant()) {
            val size = JsonObject()
            val sizeOption = JsonObject()
            sizeOption.addProperty("id", sizeVariantId.toInt())
            sizeOption.addProperty("value", sizeVariant)
            size.add("option", sizeOption)
            list.add(size)
        }

        return list
    }
}