package com.tokopedia.productcard.reimagine

import androidx.annotation.IdRes
import com.tokopedia.productcard.R as productcardR

internal sealed class ProductCardType {

    @IdRes abstract fun bottomViewId(): Int

    internal object Grid : ProductCardType() {
        override fun bottomViewId(): Int = productcardR.id.productCardShopSection
    }
    internal object GridCarousel : ProductCardType() {
        override fun bottomViewId(): Int = productcardR.id.productCardShopSection
    }
    internal object List : ProductCardType() {
        override fun bottomViewId(): Int = productcardR.id.productCardBarrierFooter
    }
    internal object ListCarousel : ProductCardType() {
        override fun bottomViewId(): Int = productcardR.id.productCardBarrierFooter
    }
}
