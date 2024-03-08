package com.tokopedia.productcard.reimagine

import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.productcard.reimagine.cart.AddToCartConstraints
import com.tokopedia.productcard.reimagine.ribbon.RibbonView
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.productcard.R as productcardR

internal sealed class ProductCardType {

    abstract fun cardContainerMarginStart(productCardModel: ProductCardModel): Int

    abstract fun addToCartConstraints(): AddToCartConstraints

    abstract fun ribbonMargin(productCardModel: ProductCardModel): RibbonView.Margin

    internal object Grid : ProductCardType() {

        override fun cardContainerMarginStart(productCardModel: ProductCardModel): Int =
            if (productCardModel.ribbon() != null)
                4.toPx()
            else 0

        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                productcardR.id.productCardGuidelineStartContent,
                productcardR.id.productCardShopSection,
                productcardR.id.productCardGuidelineEndContent,
                productcardR.id.productCardGuidelineBottomContent,
            )

        override fun ribbonMargin(productCardModel: ProductCardModel): RibbonView.Margin =
            RibbonView.Margin(start = 0, top = 4.toPx())
    }

    internal object GridCarousel : ProductCardType() {

        override fun cardContainerMarginStart(productCardModel: ProductCardModel): Int =
            if (productCardModel.ribbon() != null)
                4.toPx()
            else 0

        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                productcardR.id.productCardGuidelineStartContent,
                productcardR.id.productCardShopSection,
                productcardR.id.productCardGuidelineEndContent,
                productcardR.id.productCardGuidelineBottomContent,
            )

        override fun ribbonMargin(productCardModel: ProductCardModel): RibbonView.Margin =
            RibbonView.Margin(start = 0, top = 4.toPx())
    }

    internal object List : ProductCardType() {

        override fun cardContainerMarginStart(productCardModel: ProductCardModel): Int =
            if (productCardModel.ribbon() != null && !productCardModel.isInBackground)
                4.toPx()
            else 0

        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                ConstraintSet.PARENT_ID,
                productcardR.id.productCardBarrierFooter,
                ConstraintSet.PARENT_ID,
                ConstraintSet.PARENT_ID,
            )

        override fun ribbonMargin(productCardModel: ProductCardModel): RibbonView.Margin =
            RibbonView.Margin(
                start = if (productCardModel.isInBackground) 4.toPx() else 0,
                top = if (productCardModel.isInBackground) 12.toPx() else 4.toPx(),
            )
    }

    internal object ListCarousel : ProductCardType() {

        override fun cardContainerMarginStart(productCardModel: ProductCardModel): Int =
            if (productCardModel.ribbon() != null && !productCardModel.isInBackground)
                4.toPx()
            else 0

        override fun addToCartConstraints(): AddToCartConstraints =
            AddToCartConstraints(
                ConstraintSet.PARENT_ID,
                productcardR.id.productCardBarrierFooter,
                ConstraintSet.PARENT_ID,
                ConstraintSet.PARENT_ID,
            )

        override fun ribbonMargin(productCardModel: ProductCardModel): RibbonView.Margin =
            RibbonView.Margin(
                start = if (productCardModel.isInBackground) 4.toPx() else 0,
                top = if (productCardModel.isInBackground) 12.toPx() else 4.toPx(),
            )
    }
}
