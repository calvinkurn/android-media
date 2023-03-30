package com.tokopedia.productcard.layout.threedots

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.productcard.ProductCardModel

class ThreeDotsLayoutStrategyNone : ThreeDotsLayoutStrategy {
    override fun renderThreeDots(
        imageThreeDots: ImageView?,
        constraintLayoutProductCard: ConstraintLayout?,
        productCardModel: ProductCardModel,
    ) {
        imageThreeDots?.hide()
    }
}
