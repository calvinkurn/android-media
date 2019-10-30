package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.app.Activity
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.domain.gql.feed.LabelGroup
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by Lukas on 2019-07-15
 */

class HomeFeedViewHolder(itemView: View, private val homeFeedView: HomeFeedContract.View) : AbstractViewHolder<HomeFeedViewModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item
        private const val LABEL_POSITION_OFFERS = "offers"
        private const val LABEL_POSITION_PROMO = "promo"
        private const val LABEL_POSITION_CREDIBILITY = "credibility"
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardViewSmallGrid>(R.id.productCardView) }

    override fun bind(element: HomeFeedViewModel) {
        setLayout(element)
    }

    private fun setLayout(element: HomeFeedViewModel){
        var labelCredibility = ProductCardModel.Label()
        var labelPromo = ProductCardModel.Label()
        var labelOffers = ProductCardModel.Label()

        for (label: LabelGroup in element.labelGroups){
            when(label.position){
                LABEL_POSITION_CREDIBILITY -> {
                    labelCredibility = if (element.rating == 0 && element.countReview == 0) ProductCardModel.Label(label.title, label.type) else labelCredibility
                }
                LABEL_POSITION_PROMO -> {
                    labelPromo = ProductCardModel.Label(label.title, label.type)
                }
                LABEL_POSITION_OFFERS -> {
                    labelOffers = ProductCardModel.Label(label.title, label.type)
                }
            }
        }
        productCardView.run{
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.slashedPrice,
                            productName = element.productName,
                            formattedPrice = element.price,
                            productImageUrl = element.imageUrl,
                            isTopAds = element.isTopAds,
                            discountPercentage = element.discountPercentage.toString(),
                            reviewCount = element.countReview,
                            ratingCount = element.rating,
                            shopLocation = element.location,
                            isWishlistVisible = true,
                            isWishlisted = element.isWishList,
                            shopBadgeList = element.badges.map {
                                ProductCardModel.ShopBadge(imageUrl = it.imageUrl?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.isFreeOngkirActive,
                                    imageUrl = element.freeOngkirImageUrl
                            ),
                            labelCredibility = labelCredibility,
                            labelOffers = labelOffers,
                            labelPromo = labelPromo
                    )
            )
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
                    homeFeedView.onProductImpression(element, adapterPosition)
                }
            })
            setOnClickListener { homeFeedView.onProductClick(element, adapterPosition) }
            setButtonWishlistOnClickListener {
                homeFeedView.onWishlistClick(element, adapterPosition, !it.isActivated){ isSuccess, throwable ->
                    if(isSuccess){
                        it.isActivated = !it.isActivated
                        element.isWishList = it.isActivated
                        setButtonWishlistImage(it.isActivated)
                        if(it.isActivated){
                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                        } else {
                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                        }
                    } else {
                        Toaster.showError(
                                this.rootView.findViewById(android.R.id.content),
                                ErrorHandler.getErrorMessage(it.context, throwable),
                                Snackbar.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.go_to_wishlist) { RouteManager.route(view.context, ApplinkConst.WISHLIST) }
                .show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}

