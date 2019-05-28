package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachproduct.resultmodel.ResultProduct

class ProductPreview(
        val id: String,
        val imageUrl: String,
        val name: String,
        val price: String,
        val colorVariant: String,
        val colorHexVariant: String,
        val sizeVariant: String,
        val url: String
) {
    fun noProductPreview(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty() || id.isEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return colorVariant.isEmpty() && sizeVariant.isEmpty()
    }

    fun hasColorVariant(): Boolean = colorVariant.isNotEmpty() && colorHexVariant.isNotEmpty()

    fun hasSizeVariant(): Boolean = sizeVariant.isNotEmpty()

    fun generateResultProduct(): ResultProduct {
        return ResultProduct(id.toInt(), url, imageUrl, price, name)
    }

}