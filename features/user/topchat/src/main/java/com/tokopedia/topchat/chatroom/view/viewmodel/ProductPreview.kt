package com.tokopedia.topchat.chatroom.view.viewmodel

class ProductPreview(
        val imageUrl: String,
        val name: String,
        val price: String,
        val colorVariant: String,
        val colorHexVariant: String,
        val sizeVariant: String
) {
    fun noProductPreview(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return colorVariant.isEmpty() && sizeVariant.isEmpty()
    }

    fun hasColorVariant(): Boolean = colorVariant.isNotEmpty()

    fun hasSizeVariant(): Boolean = sizeVariant.isNotEmpty()

}