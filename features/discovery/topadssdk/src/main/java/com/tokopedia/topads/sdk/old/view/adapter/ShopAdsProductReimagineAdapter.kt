package com.tokopedia.topads.sdk.old.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.CONST_5
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_5
import com.tokopedia.topads.sdk.old.domain.model.ShopProductModel.ShopProductModelItem
import com.tokopedia.topads.sdk.old.listener.FollowButtonClickListener
import com.tokopedia.topads.sdk.old.listener.ShopAdsProductListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.shopwidget.R as shopwidgetR
import com.tokopedia.gm.common.R as gmcommonR

class ShopAdsProductReimagineAdapter(
    private val shopAdsProductListener: ShopAdsProductListener,
    private val followButtonClickListener:FollowButtonClickListener?,
) : RecyclerView.Adapter<ShopAdsProductReimagineAdapter.ShopAdsProductViewHolder>() {


    private val shopAdsProductItemList = arrayListOf<ShopProductModelItem>()

    fun setList(list: List<ShopProductModelItem>) {
        val itemCount = itemCount
        shopAdsProductItemList.clear()
        notifyItemRangeRemoved(0, itemCount)

        shopAdsProductItemList.addAll(list)
        notifyItemRangeInserted(0, shopAdsProductItemList.size)
    }

    inner class ShopAdsProductViewHolder(itemView: View, private val shopAdsProductListener: ShopAdsProductListener) : RecyclerView.ViewHolder(itemView) {
        private val productImage = itemView.findViewById<ImageView>(R.id.topAdsProductImage)
        private val productLogoShop = itemView.findViewById<ImageView>(R.id.topAdsProductLogoShop)
        private val productShopBadge = itemView.findViewById<ImageView>(R.id.topAdsProductShopBadge)
        private val productShopName = itemView.findViewById<Typography>(R.id.topAdsProductShopName)
        private val shopProductReviews = itemView.findViewById<LinearLayout>(R.id.topAdsShopProductReviews)
        private val reviewCount = itemView.findViewById<Typography>(R.id.topAdsReviewCount)
        private val locationIcon = itemView.findViewById<ImageView>(R.id.topAdsLocationIcon)
        private val locationName = itemView.findViewById<Typography>(R.id.topAdsLocationName)
        private val buttonFollow = itemView.findViewById<UnifyButton>(R.id.topAdsButtonFollow)


        fun bind(shopProductModelItem: ShopProductModelItem) {
            productImage.loadImage(shopProductModelItem.imageUrl)
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
            shopProductModelItem: ShopProductModelItem
        ) {
            if (layoutType == LAYOUT_5) {
                buttonFollow.hide()
            } else {
                if (!shopProductModelItem.isFollowed) {
                    buttonFollow.buttonVariant = UnifyButton.Variant.GHOST
                    buttonFollow.text = itemView.context.getString(R.string.topads_follow)
                } else {
                    buttonFollow.buttonVariant = UnifyButton.Variant.FILLED
                    buttonFollow.text = itemView.context.getString(R.string.topads_followed)
                }
                buttonFollow.setOnClickListener {
                    buttonFollow.buttonVariant = UnifyButton.Variant.FILLED
                    buttonFollow.text = itemView.context.getString(R.string.topads_followed)
                    followButtonClickListener?.onItemClicked(shopProductModelItem)
                }
                buttonFollow.show()
            }
        }

        private fun setLocation(location: String) {
            if (location.isNotEmpty()) {
                locationIcon.show()
                locationName.text = location
                locationName.show()
            }
        }

        private fun setRating(rating: String, countReview: String) {
            val ratingData = rating.toFloatOrZero().toInt()
            if (ratingData in Int.ONE..CONST_5) {
                for (r in Int.ZERO until ratingData) {
                    shopProductReviews.show()
                    (shopProductReviews.getChildAt(r) as ImageView).setImageResource(R.drawable.product_card_ic_rating_active)
                }
                setTextViewReviewCount(countReview)

            } else {
                reviewCount.hide()
                shopProductReviews.invisible()
            }
        }

        private fun setTextViewReviewCount(countReview: String) {
            reviewCount.show()
            reviewCount.text = getCountReview(countReview)
        }

        private fun getCountReview(countReview: String) =
            itemView.resources.getString(R.string.topads_shop_product_count_review, countReview)

        private fun loadBadge(shopProductModelItem: ShopProductModelItem) {
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopProductModelItem)
            productShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopProductModelItem.isOfficial -> productShopBadge.loadImage(R.drawable.ic_official_store)
                    shopProductModelItem.isPMPro -> productShopBadge.loadImage(shopwidgetR.drawable.shopwidget_ic_pm_pro)
                    shopProductModelItem.isGoldShop -> productShopBadge.loadImage(gmcommonR.drawable.ic_power_merchant)
                }
            }
        }

        private fun getIsImageShopBadgeVisible(shopProductModelItem: ShopProductModelItem): Boolean {
            return shopProductModelItem.isOfficial
                    || shopProductModelItem.isPMPro
                    || shopProductModelItem.isGoldShop
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopAdsProductViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.topads_with_one_product_layout_item_reimagine, parent, false)
        return ShopAdsProductViewHolder(view, shopAdsProductListener)
    }

    override fun onBindViewHolder(holder: ShopAdsProductViewHolder, position: Int) {
        holder.bind(shopAdsProductItemList[position])
    }

    override fun getItemCount(): Int {
        return shopAdsProductItemList.count()
    }

}
