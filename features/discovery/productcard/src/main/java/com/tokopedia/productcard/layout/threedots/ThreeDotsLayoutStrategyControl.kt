package com.tokopedia.productcard.layout.threedots

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getDimensionPixelSize
import com.tokopedia.productcard.utils.expandTouchArea
import com.tokopedia.productcard.utils.shouldShowWithAction

class ThreeDotsLayoutStrategyControl: ThreeDotsLayoutStrategy {
    override fun renderThreeDots(
        imageThreeDots: ImageView?,
        constraintLayoutProductCard: ConstraintLayout?,
        productCardModel: ProductCardModel
    ) {
        imageThreeDots.shouldShowWithAction(productCardModel.hasThreeDots) {
            constraintLayoutProductCard?.post {
                imageThreeDots?.expandTouchArea(
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                    it.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
                )
            }
        }
    }
}
