package com.tokopedia.product.detail.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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
import com.tokopedia.topads.sdk.utils.ImpresionTask

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

    inner class RecommendationProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                                ),
                                labelPromo = ProductCardModel.Label(
                                        title = product.labelPromo.title,
                                        type = product.labelPromo.type
                                ),
                                labelCredibility = ProductCardModel.Label(
                                        title = product.labelCredibility.title,
                                        type = product.labelCredibility.type
                                ),
                                labelOffers = ProductCardModel.Label(
                                        title = product.labelOffers.title,
                                        type = product.labelOffers.type
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
                        if (product.isTopAds) {
                            ImpresionTask().execute(product.trackerImageUrl)
                        }
                        productDetailTracking.eventRecommendationImpression(adapterPosition, product, userActiveListener.isUserSessionActive, pageName, recommendationWidget.title)
                    }
                })

                setOnClickListener {
                    if (product.isTopAds) {
                        ImpresionTask().execute(product.clickUrl)
                    }
                    productDetailTracking.eventRecommendationClick(product, adapterPosition, userActiveListener.isUserSessionActive,pageName,recommendationWidget.title)
                    context?.run {
                        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, product.productId.toString())
                    }
                }
            }
        }
    }

    interface UserActiveListener{
        val isUserSessionActive: Boolean
    }
}