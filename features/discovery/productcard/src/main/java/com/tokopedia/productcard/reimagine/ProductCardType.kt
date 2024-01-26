package com.tokopedia.productcard.reimagine

import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.productcard.R as productcardR

internal sealed class ProductCardType {

    abstract fun addToCartConstraints(): AddToCartConstraints

    internal object Grid : ProductCardType() {
        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                ConstraintSet.PARENT_ID,
                productcardR.id.productCardShopSection,
                ConstraintSet.PARENT_ID,
                ConstraintSet.PARENT_ID,
            )
    }

    internal object GridCarousel : ProductCardType() {
        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                productcardR.id.productCardGuidelineStartContent,
                productcardR.id.productCardShopSection,
                productcardR.id.productCardGuidelineEndContent,
                productcardR.id.productCardGuidelineBottomContent,
            )
    }

    internal object List : ProductCardType() {
        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                ConstraintSet.PARENT_ID,
                productcardR.id.productCardBarrierFooter,
                ConstraintSet.PARENT_ID,
                ConstraintSet.PARENT_ID,
            )
    }

    internal object ListCarousel : ProductCardType() {
        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                ConstraintSet.PARENT_ID,
                productcardR.id.productCardBarrierFooter,
                ConstraintSet.PARENT_ID,
                ConstraintSet.PARENT_ID,
            )
    }
}
