package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifycomponents.Toaster


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

            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
                override fun onViewHint() {
                    if (element.productItem.isTopAds) {
                        // Implement Tracking?
                    }
                    // listener
                }
            })

            setOnClickListener {
                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
                if (element.productItem.isTopAds) ImpresionTask().execute(element.productItem.clickUrl)
            }

            setButtonWishlistOnClickListener {}
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String) {
        Toaster.showNormalWithAction(view, message, Snackbar.LENGTH_LONG,
                "Lihat Wishlist", View.OnClickListener {
            RouteManager.route(view.context, ApplinkConst.WISHLIST)
        })
    }

    private fun showSuccessRemoveWishlist(view: View, message: String) {
        Toaster.showNormal(view, message, Snackbar.LENGTH_LONG)
    }

    private fun showError(view: View, throwable: Throwable?) {
        Toaster.showError(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

}