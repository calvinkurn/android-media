package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView

class ProductRecommendationViewHolder(view: View): AbstractViewHolder<ProductRecommendationViewModel>(view) {

    private val productCardView: ProductCardView by lazy { view.findViewById<ProductCardView>(R.id.product_item) }
    private var textView : AppCompatTextView? = null

    init {
        textView = view.findViewById(R.id.sample_text)
    }

    override fun bind(element: ProductRecommendationViewModel) {
        System.out.println(element)
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.productItem.slashedPrice,
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            isTopAds = element.productItem.isTopAds,
                            discountPercentage = element.productItem.discountPercentage.toString(),
                            reviewCount = element.productItem.countReview,
                            ratingCount = element.productItem.rating,
                            shopLocation = element.productItem.location,
                            isWishlistVisible = false,
                            isWishlisted = element.productItem.isWishlist,
                            shopBadgeList = element.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.isFreeOngkirActive,
                                    imageUrl = element.productItem.freeOngkirImageUrl
                            )
                    )
            )
        }
    }

}