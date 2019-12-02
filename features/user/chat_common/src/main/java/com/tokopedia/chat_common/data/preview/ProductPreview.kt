package com.tokopedia.chat_common.data.preview

open class ProductPreview (
        val id: String = "",
        val imageUrl: String = "",
        val name: String = "",
        val price: String = "",
        val colorVariant: String = "",
        val colorHexVariant: String = "",
        val sizeVariant: String = "",
        val url: String = "",
        val productFsIsActive: Boolean = false,
        val productFsImageUrl: String = ""
) {

    fun notEnoughRequiredData(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty() || id.isEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return colorVariant.isEmpty() && sizeVariant.isEmpty()
    }

    fun hasColorVariant(): Boolean = colorVariant.isNotEmpty() && colorHexVariant.isNotEmpty()

    fun hasSizeVariant(): Boolean = sizeVariant.isNotEmpty()
}