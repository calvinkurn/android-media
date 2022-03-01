package com.tokopedia.vouchercreation.product.download

sealed class ImageType(val imageUrl: String) {
    class Square(imageUrl: String): ImageType(imageUrl)
    class Portrait(imageUrl: String): ImageType(imageUrl)
    class Banner(imageUrl: String): ImageType(imageUrl)
}