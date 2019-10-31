package com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.productModel.BadgesItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.LabelGroupsItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener
import com.tokopedia.productcard.v2.ProductCardView


const val LABEL_GROUP_POSITION_PROMO = "promo"
const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
const val LABEL_GROUP_POSITION_OFFERS = "offers"

abstract class ProductCardViewHolder(itemView: View,
                                     var productListener: ProductCardListener) : AbstractViewHolder<ProductsItem>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductsItem) {
        if (productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButton(productItem)
        initPromoLabel(productItem)
        initShopName(productItem)
        initTitleTextView(productItem)
        initSlashedPriceSection(productItem)
        initPriceTextView(productItem)
        initShopBadge(productItem)
        initLocationTextView(productItem)
        initCredibilitySection(productItem)
        initOffersLabel(productItem)
        initTopAdsIcon(productItem)
        finishBindViewHolder()
    }

    protected abstract fun getProductCardView(): ProductCardView?

    protected abstract fun isUsingBigImageUrl(): Boolean


    protected fun initProductCardContainer(productItem: ProductsItem) {
        getProductCardView()?.setOnLongClickListener {
            true
        }

        getProductCardView()?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    protected fun initProductImage(productItem: ProductsItem) {
        getProductCardView()?.setImageProductVisible(true)

        setImageProductUrl(productItem)
    }

    protected fun setImageProductUrl(productItem: ProductsItem) {
        val imageUrl = if (isUsingBigImageUrl()) productItem.imageURL700 else productItem.imageURL

        getProductCardView()?.setImageProductUrl(imageUrl)
    }

    protected fun initWishlistButton(productItem: ProductsItem) {
        getProductCardView()?.setButtonWishlistVisible(true)
        getProductCardView()?.setButtonWishlistImage(productItem.wishlist)
        getProductCardView()?.setButtonWishlistOnClickListener {
            if (productItem.isWishListEnabled) {
                productListener.onWishlistButtonClicked(productItem,adapterPosition)
            }
        }
    }

    protected fun initPromoLabel(productItem: ProductsItem) {
        val promoLabelViewModel: LabelGroupsItem? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)

        if (promoLabelViewModel != null) {
            getProductCardView()?.setLabelPromoVisible(true)
            getProductCardView()?.setLabelPromoType(promoLabelViewModel.type)
            getProductCardView()?.setLabelPromoText(promoLabelViewModel.title)
        } else {
            getProductCardView()?.setLabelPromoVisible(false)
        }
    }

    @Nullable
    protected fun getFirstLabelGroupOfPosition(productItem: ProductsItem, position: String): LabelGroupsItem? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if (labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    protected fun getLabelGroupOfPosition(productItem: ProductsItem, position: String): List<LabelGroupsItem?> {
        return productItem.labelGroups.filter { labelGroup -> labelGroup?.position == position }
    }

    protected fun initShopName(productItem: ProductsItem) {
        val isShopNameShown = isShopNameShown(productItem)
        getProductCardView()?.setShopNameVisible(isShopNameShown)

        if (isShopNameShown) {
            getProductCardView()?.setShopNameText(productItem.shop?.name ?: "")
        }
    }

    protected fun isShopNameShown(productItem: ProductsItem): Boolean {
        return productItem.shop.isOfficial
    }

    protected fun initTitleTextView(productItem: ProductsItem) {
        getProductCardView()?.setProductNameVisible(true)
        getProductCardView()?.setProductNameText(productItem.name)
    }

    protected fun initSlashedPriceSection(productItem: ProductsItem) {
        val isLabelDiscountVisible = isLabelDiscountVisible(productItem)

        getProductCardView()?.setLabelDiscountVisible(isLabelDiscountVisible)
        getProductCardView()?.setSlashedPriceVisible(isLabelDiscountVisible)

        if (isLabelDiscountVisible) {
            getProductCardView()?.setLabelDiscountText(productItem.discountPercentage)
            getProductCardView()?.setSlashedPriceText(productItem.originalPrice)
        }
    }

    protected fun isLabelDiscountVisible(productItem: ProductsItem): Boolean {
        return productItem.discountPercentage > 0
    }

    protected fun initPriceTextView(productItem: ProductsItem) {
        getProductCardView()?.setPriceVisible(true)
        getProductCardView()?.setPriceText(getPriceText(productItem))
    }

    protected fun getPriceText(productItem: ProductsItem): String {
        return if (!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    protected fun initShopBadge(productItem: ProductsItem) {
        val hasAnyBadgesShown = hasAnyBadgesShown(productItem)
        getProductCardView()?.setShopBadgesVisible(hasAnyBadgesShown)

        if (hasAnyBadgesShown) {
            getProductCardView()?.removeAllShopBadges()
            loopBadgesListToLoadShopBadgeIcon(productItem.badges)
        }
    }

    protected fun hasAnyBadgesShown(productItem: ProductsItem): Boolean {
        return productItem.badges!!.any { badge -> badge!!.show }
    }

    protected fun loopBadgesListToLoadShopBadgeIcon(badgesList: List<BadgesItem>) {
        for (badgeItem in badgesList) {
            if (badgeItem.show) {
                loadShopBadgesIcon(badgeItem.imageURL ?: "")
            }
        }
    }

    protected fun loadShopBadgesIcon(url: String) {
        if (!TextUtils.isEmpty(url)) {
            val view = LayoutInflater.from(context).inflate(R.layout.category_product_card_badge_layout, null)

            ImageHandler.loadImageBitmap2(context, url, object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    loadShopBadgeSuccess(view, resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    loadShopBadgeFailed(view)
                }
            })
        }
    }

    protected fun loadShopBadgeSuccess(view: View, bitmap: Bitmap) {
        val image = view.findViewById<ImageView>(R.id.badge)

        if (bitmap.height <= 1 && bitmap.width <= 1) {
            view.visibility = View.GONE
        } else {
            image.setImageBitmap(bitmap)
            view.visibility = View.VISIBLE
            getProductCardView()?.addShopBadge(view)
        }
    }

    protected fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    protected fun initLocationTextView(productItem: ProductsItem) {
        val isShopLocationShown = isShopLocationShown(productItem)
        getProductCardView()?.setShopLocationVisible(isShopLocationShown)

        if (isShopLocationShown) {
            getProductCardView()?.setShopLocationText(productItem.shop!!.city)
        }
    }

    protected fun isShopLocationShown(productItem: ProductsItem): Boolean {
        return !TextUtils.isEmpty(productItem.shop!!.city)
    }

    protected fun initCredibilitySection(productItem: ProductsItem) {
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
    }

    protected fun initRatingAndReview(productItem: ProductsItem) {
        initRatingView(productItem)
        initReviewCount(productItem)
    }

    protected fun initRatingView(productItem: ProductsItem) {
        val isImageRatingVisible = isImageRatingVisible(productItem)

        getProductCardView()?.setImageRatingVisible(isImageRatingVisible)

        if (isImageRatingVisible) {
            getProductCardView()?.setRating(getStarCount(productItem))
        }
    }

    protected fun isImageRatingVisible(productItem: ProductsItem): Boolean {
        return productItem.rating != 0
    }

    protected fun getStarCount(productItem: ProductsItem): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            return Math.round(productItem.rating?.toFloat() ?: 0f)
    }

    protected fun initReviewCount(productItem: ProductsItem) {
        val isReviewCountVisible = isReviewCountVisible(productItem)
        getProductCardView()?.setReviewCountVisible(isReviewCountVisible)

        if (isReviewCountVisible) {
            getProductCardView()?.setReviewCount(productItem.countReview)
        }
    }

    protected fun isReviewCountVisible(productItem: ProductsItem): Boolean {
        return productItem.countReview != 0
    }

    protected fun initCredibilityLabel(productItem: ProductsItem) {
        val isCredibilityLabelVisible = isCredibilityLabelVisible(productItem)
        getProductCardView()?.setLabelCredibilityVisible(isCredibilityLabelVisible)

        if (isCredibilityLabelVisible) {
            val credibilityLabelViewModel: LabelGroupsItem =
                    getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)!!

            getProductCardView()?.setLabelCredibilityType(credibilityLabelViewModel.type)
            getProductCardView()?.setLabelCredibilityText(credibilityLabelViewModel.title)
        }
    }

    protected fun isCredibilityLabelVisible(productItem: ProductsItem): Boolean {
        return isRatingAndReviewNotVisible(productItem)
                && isCredibilityLabelExists(productItem)
    }

    protected fun isRatingAndReviewNotVisible(productItem: ProductsItem): Boolean {
        return !isImageRatingVisible(productItem) && !isReviewCountVisible(productItem)
    }

    protected fun isCredibilityLabelExists(productItem: ProductsItem): Boolean {
        return getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY) != null
    }

    protected fun initOffersLabel(productItem: ProductsItem) {
        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)

        getProductCardView()?.setLabelOffersVisible(offersLabelViewModel != null)

        if (offersLabelViewModel != null) {
            setOffersLabelContent(offersLabelViewModel)
        }
    }

    protected fun setOffersLabelContent(offersLabelViewModel: LabelGroupsItem) {
        getProductCardView()?.setLabelOffersType(offersLabelViewModel.type)
        getProductCardView()?.setLabelOffersText(offersLabelViewModel.title)
    }

    protected fun initTopAdsIcon(productItem: ProductsItem) {
        getProductCardView()?.setImageTopAdsVisible(productItem.isTopAds)
    }

    protected fun finishBindViewHolder() {
        getProductCardView()?.realignLayout()
    }


}