package com.tokopedia.productcard.layout.threedots

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.productcard.ProductCardModel

internal interface ThreeDotsLayoutStrategy {
    fun renderThreeDots(
        imageThreeDots: ImageView?,
        constraintLayoutProductCard: ConstraintLayout?,
        productCardModel: ProductCardModel,
    )
}
