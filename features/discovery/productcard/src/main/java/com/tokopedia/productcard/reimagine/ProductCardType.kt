package com.tokopedia.productcard.reimagine

internal sealed class ProductCardType {
    internal object Grid : ProductCardType()
    internal object GridCarousel : ProductCardType()
    internal object List : ProductCardType()
    internal object ListCarousel : ProductCardType()
}
