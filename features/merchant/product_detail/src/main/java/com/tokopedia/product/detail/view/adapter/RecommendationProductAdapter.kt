package com.tokopedia.product.detail.view.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class RecommendationProductAdapter(private var recommendationWidget: RecommendationWidget,
                                   private val userActiveListener: UserActiveListener,
                                   private var pageName: String,
                                   private val productDetailTracking: ProductDetailTracking) : RecyclerView.Adapter<RecommendationProductAdapter.RecommendationProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationProductViewHolder {
        return RecommendationProductViewHolder(parent.inflateLayout(R.layout.item_product_recommendation))
    }

    override fun getItemCount(): Int {
        return recommendationWidget.recommendationItemList.size
    }

    override fun onBindViewHolder(holder: RecommendationProductViewHolder, position: Int) {
        holder.bind(recommendationWidget.recommendationItemList[position])
    }

    inner class RecommendationProductViewHolder(itemView: View) : RecommendationCardView.TrackingListener, RecyclerView.ViewHolder(itemView) {
        private val productCardView: ProductCardViewSmallGrid? = itemView.findViewById(R.id.productCardView)

        fun bind(product: RecommendationItem) {
            productCardView?.run {
                setProductModel(
                        ProductCardModel(
                                slashedPrice = product.slashedPrice,
                                productName = product.name,
                                formattedPrice = product.price,
                                productImageUrl = product.imageUrl,
                                isTopAds = product.isTopAds,
                                discountPercentage = product.discountPercentage.toString(),
                                reviewCount = product.countReview,
                                ratingCount = product.rating,
                                shopLocation = product.location,
                                isWishlistVisible = false,
                                isWishlisted = product.isWishlist,
                                shopBadgeList = product.badgesUrl.map {
                                    ProductCardModel.ShopBadge(imageUrl = it?:"")
                                },
                                freeOngkir = ProductCardModel.FreeOngkir(
                                        isActive = product.isFreeOngkirActive,
                                        imageUrl = product.freeOngkirImageUrl
                                )
                        ),
                        BlankSpaceConfig(
                                ratingCount = true,
                                discountPercentage = true,
                                twoLinesProductName = true
                        )
                )
                setImageProductViewHintListener(product, object : ViewHintListener {
                    override fun onViewHint() {
                        productDetailTracking.eventRecommendationImpression(adapterPosition, product, userActiveListener.isUserSessionActive, pageName, recommendationWidget.title)
                    }
                })

                setOnClickListener {
                    productDetailTracking.eventRecommendationClick(product, adapterPosition, userActiveListener.isUserSessionActive,pageName,recommendationWidget.title)
                }
            }
        }

        override fun onImpressionTopAds(item: RecommendationItem) {
        }

        override fun onImpressionOrganic(item: RecommendationItem) {

        }

        override fun onClickTopAds(item: RecommendationItem) {

        }

        override fun onClickOrganic(item: RecommendationItem) {

        }

    }

    interface UserActiveListener{
        val isUserSessionActive: Boolean
    }
}