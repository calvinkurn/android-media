package com.tokopedia.topchat.chatroom.view.viewmodel

class AskedProduct(
        val imageUrl: String,
        val name: String,
        val price: String,
        val colorVariant: String,
        val colorHexVariant: String,
        val sizeVariant: String
) {
    fun noAskedProduct(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty()
    }

}