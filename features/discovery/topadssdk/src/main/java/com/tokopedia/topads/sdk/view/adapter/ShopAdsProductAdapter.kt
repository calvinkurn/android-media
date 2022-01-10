package com.tokopedia.topads.sdk.view.adapter

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
import com.tokopedia.topads.sdk.domain.model.ShopProductModel.ShopProductModelItem
import com.tokopedia.topads.sdk.listener.ShopAdsProductListener
import com.tokopedia.unifyprinciples.Typography

class ShopAdsProductAdapter(private val shopAdsProductListener: ShopAdsProductListener) : RecyclerView.Adapter<ShopAdsProductAdapter.ShopAdsProductViewHolder>() {

    private val shopAdsProductItemList = arrayListOf<ShopProductModelItem>()

    fun setList(list: List<ShopProductModelItem>) {
        shopAdsProductItemList.clear()
        shopAdsProductItemList.addAll(list)
        notifyDataSetChanged()

    }

    inner class ShopAdsProductViewHolder(itemView: View, private val shopAdsProductListener: ShopAdsProductListener) : RecyclerView.ViewHolder(itemView) {
        private val productImage = itemView.findViewById<ImageView>(R.id.productImage)
        private val productLogoShop = itemView.findViewById<ImageView>(R.id.productLogoShop)
        private val productShopBadge = itemView.findViewById<ImageView>(R.id.productShopBadge)
        private val productShopName = itemView.findViewById<Typography>(R.id.productShopName)
        private val shopProductReviews = itemView.findViewById<LinearLayout>(R.id.shopProductReviews)
        private val reviewCount = itemView.findViewById<Typography>(R.id.reviewCount)
        private val shopProductRoot = itemView.findViewById<CardView>(R.id.shopProductRoot)


        fun bind(shopProductModelItem: ShopProductModelItem) {

            productImage.loadImage(shopProductModelItem.imageUrl)
            productLogoShop.loadImageCircle(shopProductModelItem.shopIcon)
            loadBadge(shopProductModelItem)
            productShopName.text = shopProductModelItem.shopName
            setRating(shopProductModelItem.ratingAverage, shopProductModelItem.ratingCount)
            shopProductModelItem.impressHolder?.let { impressHolder ->
                shopProductRoot.addOnImpressionListener(impressHolder) {
                    shopAdsProductListener.onItemImpressed(shopProductModelItem.position)
                }
            }

            shopProductRoot.setOnClickListener { shopAdsProductListener.onItemClicked(shopProductModelItem.position) }

        }

        private fun setRating(rating: String, countReview: String) {
            val ratingData = rating.toFloatOrZero().toInt()
            if (ratingData in 1..5) {
                for (r in 0 until ratingData) {
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
            reviewCount.text = String.format("%s%s%s", "(", countReview, ")")
        }

        private fun loadBadge(shopProductModelItem: ShopProductModelItem) {
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(shopProductModelItem)
            productShopBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    shopProductModelItem.isOfficial -> productShopBadge.loadImage(R.drawable.ic_official_store)
                    shopProductModelItem.isPMPro -> productShopBadge.loadImage(com.tokopedia.shopwidget.R.drawable.shopwidget_ic_pm_pro)
                    shopProductModelItem.isGoldShop -> productShopBadge.loadImage(com.tokopedia.gm.common.R.drawable.ic_power_merchant)
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
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.topads_with_one_product_layout_item, parent, false)
        return ShopAdsProductViewHolder(view, shopAdsProductListener)
    }

    override fun onBindViewHolder(holder: ShopAdsProductViewHolder, position: Int) {
        holder.bind(shopAdsProductItemList[position])
    }

    override fun getItemCount(): Int {
        return shopAdsProductItemList.count()
    }

}
