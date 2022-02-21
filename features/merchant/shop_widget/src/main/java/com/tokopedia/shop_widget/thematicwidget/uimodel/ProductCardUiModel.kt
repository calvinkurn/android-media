package com.tokopedia.shop_widget.thematicwidget.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.thematicwidget.typefactory.ProductCardTypeFactory

data class ProductCardUiModel(
    var id: String? = null,
    var name: String? = null,
    var displayedPrice: String? = null,
    var originalPrice: String? = null,
    var discountPercentage: String? = null,
    var imageUrl: String? = null,
    var imageUrl300: String? = null,
    var imageUrl700: String? = null,
    var totalReview: String? = null,
    var rating: Double = 0.toDouble(),
    var cashback: Double = 0.toDouble(),
    var isWholesale: Boolean = false,
    var isPo: Boolean = false,
    var isFreeReturn: Boolean = false,
    var isWishList: Boolean = false,
    var productUrl: String? = null,
    var isShowWishList: Boolean = false,
    var isSoldOut: Boolean = false,
    var isShowFreeOngkir: Boolean = false,
    var freeOngkirPromoIcon: String? = null,
    var labelGroupList: List<LabelGroupUiModel> = listOf(),
    var stockLabel: String = "",
    var hideGimmick: Boolean = false,
    var stockSoldPercentage: Int = 0,
    var recommendationType: String? = null,
    var minimumOrder: Int = 1,
    var isProductPlaceHolder: Boolean = false,
    var totalProduct: Int = 0,
    var totalProductWording: String = "",
    var rvState: Parcelable? = null
): Visitable<ProductCardTypeFactory> {
    override fun type(typeFactory: ProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class LabelGroupUiModel(
    val position: String = "",
    val type: String = "",
    val title: String = "",
    val url: String = ""
)