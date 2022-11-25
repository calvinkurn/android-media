package com.tokopedia.favorite.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.sdk.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.listener.ShopAdsProductListener
import com.tokopedia.favorite.R
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.sdk.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.ShopProductModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle

class TopAdsShopAdapter_v2 (
    private val shopAdsProductListener: ShopAdsProductListener,
    private val followButtonClickListener: FollowButtonClickListener?
) : RecyclerView.Adapter<TopAdsShopAdapter_v2.ShopAdsProductViewHolder>(){
    private val shopAdsProductItemList = arrayListOf<ShopProductModel.ShopProductModelItem>()

    fun setList(list: List<ShopProductModel.ShopProductModelItem>) {
        shopAdsProductItemList.clear()
        shopAdsProductItemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ShopAdsProductViewHolder(itemView: View, private val shopAdsProductListener: ShopAdsProductListener) : RecyclerView.ViewHolder(itemView) {
//        private val productImage = itemView.findViewById<ImageView>(com.tokopedia.topads.sdk.R.id.productImage)
        private val productLogoShop = itemView.findViewById<ImageView>(R.id.productLogoShop)
        private val productShopBadge = itemView.findViewById<ImageView>(R.id.productShopBadge)
        private val productShopName = itemView.findViewById<Typography>(R.id.productShopName)
//        private val shopProductReviews = itemView.findViewById<LinearLayout>(com.tokopedia.topads.sdk.R.id.shopProductReviews)
//        private val reviewCount = itemView.findViewById<Typography>(com.tokopedia.topads.sdk.R.id.reviewCount)
//        private val locationIcon = itemView.findViewById<ImageView>(com.tokopedia.topads.sdk.R.id.locationIcon)
        private val locationName = itemView.findViewById<Typography>(R.id.locationName)
        private val buttonFollow = itemView.findViewById<UnifyButton>(R.id.buttonFollow)


        fun bind(shopProductModelItem: ShopProductModel.ShopProductModelItem) {

//            productImage.loadImage(shopProductModelItem.imageUrl)
            productLogoShop.loadImageCircle(shopProductModelItem.shopIcon)
            loadBadge(shopProductModelItem)
            productShopName.text = shopProductModelItem.shopName
            setLocation(shopProductModelItem.location)
            shopProductModelItem.impressHolder?.let { impressHolder ->
                itemView.addOnImpressionListener(impressHolder) {
                    shopAdsProductListener.onItemImpressed(shopProductModelItem.position)
                }
            }

            itemView.setOnClickListener { shopAdsProductListener.onItemClicked(shopProductModelItem.position) }
            setFollowButton(shopProductModelItem.layoutType, shopProductModelItem)

        }

        private fun setFollowButton(
            layoutType: Int?,
            shopProductModelItem: ShopProductModel.ShopProductModelItem
        ) {
            if (layoutType == TopAdsConstants.LAYOUT_5) {
                buttonFollow.hide()
            } else {
                if (!shopProductModelItem.isFollowed) {
                    buttonFollow.buttonVariant = UnifyButton.Variant.GHOST
                    buttonFollow.text = itemView.context.getString(com.tokopedia.topads.sdk.R.string.topads_follow)
                } else {
                    buttonFollow.buttonVariant = UnifyButton.Variant.FILLED
                    buttonFollow.text = itemView.context.getString(com.tokopedia.topads.sdk.R.string.topads_followed)
                }
                buttonFollow.setOnClickListener {
                    buttonFollow.buttonVariant = UnifyButton.Variant.FILLED
                    buttonFollow.text = itemView.context.getString(com.tokopedia.topads.sdk.R.string.topads_followed)
                    followButtonClickListener?.onItemClicked(shopProductModelItem)
                }
                buttonFollow.show()
            }
        }

        private fun setLocation(location: String) {
            if (location.isNotEmpty()) {
//                locationIcon.show()
                locationName.text = location
                locationName.show()
            }
        }

//        private fun setRating(rating: String, countReview: String) {
//            val ratingData = rating.toFloatOrZero().toInt()
//            if (ratingData in Int.ONE..TopAdsConstants.CONST_5) {
//                for (r in Int.ZERO until ratingData) {
//                    shopProductReviews.show()
//                    (shopProductReviews.getChildAt(r) as ImageView).setImageResource(com.tokopedia.topads.sdk.R.drawable.product_card_ic_rating_active)
//                }
//                setTextViewReviewCount(countReview)
//
//            } else {
//                reviewCount.hide()
//                shopProductReviews.invisible()
//            }
//        }

//        private fun setTextViewReviewCount(countReview: String) {
//            reviewCount.show()
//            reviewCount.text = String.format("%s%s%s", "(", countReview, ")")
//        }

        private fun loadBadge(shopProductModelItem: ShopProductModel.ShopProductModelItem) {
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopProductModelItem)
            productShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopProductModelItem.isOfficial -> productShopBadge.loadImage(R.drawable.ic_official_store)
                    shopProductModelItem.isPMPro -> productShopBadge.loadImage(com.tokopedia.shopwidget.R.drawable.shopwidget_ic_pm_pro)
                    shopProductModelItem.isGoldShop -> productShopBadge.loadImage(com.tokopedia.gm.common.R.drawable.ic_power_merchant)
                }
            }
        }

        private fun getIsImageShopBadgeVisible(shopProductModelItem: ShopProductModel.ShopProductModelItem): Boolean {
            return shopProductModelItem.isOfficial
                || shopProductModelItem.isPMPro
                || shopProductModelItem.isGoldShop
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopAdsProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.favorite_listview_recommend_shop_v2, parent, false)
        return ShopAdsProductViewHolder(view, shopAdsProductListener)
    }

    override fun onBindViewHolder(holder: ShopAdsProductViewHolder, position: Int) {
        holder.bind(shopAdsProductItemList[position])
    }

    override fun getItemCount(): Int {
        return shopAdsProductItemList.count()
    }
}
