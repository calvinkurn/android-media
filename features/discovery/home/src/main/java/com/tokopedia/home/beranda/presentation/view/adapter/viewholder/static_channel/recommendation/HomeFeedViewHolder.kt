package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.v2.ProductCardModel

/**
 * Created by Lukas on 2019-07-15
 */

class HomeFeedViewHolder(itemView: View, private val homeFeedView: HomeFeedContract.View) : AbstractViewHolder<HomeFeedViewModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: HomeFeedViewModel) {
        setLayout(element)
    }

    private fun setLayout(element: HomeFeedViewModel){
        val productCardModelLabelGroupList = element.labelGroups.map {
            ProductCardModel.LabelGroup(position = it.position, type = it.type, title = it.title)
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
                            labelGroupList = productCardModelLabelGroupList
                    )
            )
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
                    homeFeedView.onProductImpression(element, adapterPosition)
                }
            })
            setOnClickListener { homeFeedView.onProductClick(element, adapterPosition) }
        }
    }
}

