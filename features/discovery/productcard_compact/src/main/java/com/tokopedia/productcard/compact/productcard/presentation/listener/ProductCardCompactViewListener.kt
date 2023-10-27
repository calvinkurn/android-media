package com.tokopedia.productcard.compact.productcard.presentation.listener

import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView.ProductCardCompactListener
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactWishlistButtonView.WishlistButtonListener
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener

class ProductCardCompactViewListener(
    val onQuantityChangedListener: (Int) -> Unit = {},
    val clickAddVariantListener: ((Int) -> Unit) = {},
    val blockAddToCartListener: (() -> Unit) = {},
    val wishlistButtonListener: WishlistButtonListener? = null,
    val productCardCompactListener: ProductCardCompactListener? = null,
    val similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener? = null
)
