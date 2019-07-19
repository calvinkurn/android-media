package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.Nullable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener

const val LABEL_GROUP_POSITION_PROMO = "promo"
const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
const val LABEL_GROUP_POSITION_OFFERS = "offers"

abstract class ProductItemViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
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

    protected fun initProductCardContainer(productItem: ProductItemViewModel) {
        getProductCardView()?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        getProductCardView()?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    protected fun initProductImage(productItem: ProductItemViewModel) {
        getProductCardView()?.setImageProductVisible(true)

        setImageProductUrl(productItem)

        getProductCardView()?.setImageProductViewHintListener(productItem, createViewHintListener(productItem))
    }

    private fun createViewHintListener(productItem: ProductItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                productListener.onProductImpressed(productItem, adapterPosition)
            }
        }
    }

    protected fun setImageProductUrl(productItem: ProductItemViewModel) {
        val imageUrl = if (isUsingBigImageUrl()) productItem.imageUrl700 else productItem.imageUrl

        getProductCardView()?.setImageProductUrl(imageUrl)
    }

    protected fun initWishlistButton(productItem: ProductItemViewModel) {
        getProductCardView()?.setButtonWishlistVisible(productItem.isWishlistButtonEnabled)
        getProductCardView()?.setButtonWishlistImage(productItem.isWishlisted)
        getProductCardView()?.setButtonWishlistOnClickListener {
            if (productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    protected fun initPromoLabel(productItem: ProductItemViewModel) {
        val promoLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)

        if(promoLabelViewModel != null) {
            getProductCardView()?.setLabelPromoVisible(true)
            getProductCardView()?.setLabelPromoType(promoLabelViewModel.type)
            getProductCardView()?.setLabelPromoText(promoLabelViewModel.title)
        }
        else {
            getProductCardView()?.setLabelPromoVisible(false)
        }
    }

    @Nullable
    protected fun getFirstLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): LabelGroupViewModel? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    protected fun getLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): List<LabelGroupViewModel>? {
        return productItem.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    protected fun initShopName(productItem: ProductItemViewModel) {
        val isShopNameShown = isShopNameShown(productItem)
        getProductCardView()?.setShopNameVisible(isShopNameShown)

        if(isShopNameShown) {
            getProductCardView()?.setShopNameText(productItem.shopName)
        }
    }

    protected fun isShopNameShown(productItem: ProductItemViewModel): Boolean {
        return productItem.isShopOfficialStore
    }

    protected fun initTitleTextView(productItem: ProductItemViewModel) {
        getProductCardView()?.setProductNameVisible(true)
        getProductCardView()?.setProductNameText(productItem.productName)
    }

    protected fun initSlashedPriceSection(productItem: ProductItemViewModel) {
        val isLabelDiscountVisible = isLabelDiscountVisible(productItem)

        getProductCardView()?.setLabelDiscountVisible(isLabelDiscountVisible)
        getProductCardView()?.setSlashedPriceVisible(isLabelDiscountVisible)

        if (isLabelDiscountVisible) {
            getProductCardView()?.setLabelDiscountText(productItem.discountPercentage)
            getProductCardView()?.setSlashedPriceText(productItem.originalPrice)
        }
    }

    protected fun isLabelDiscountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.discountPercentage > 0
    }

    protected fun initPriceTextView(productItem: ProductItemViewModel) {
        getProductCardView()?.setPriceVisible(true)
        getProductCardView()?.setPriceText(getPriceText(productItem))
    }

    protected fun getPriceText(productItem: ProductItemViewModel) : String {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    protected fun initShopBadge(productItem: ProductItemViewModel) {
        val hasAnyBadgesShown = hasAnyBadgesShown(productItem)
        getProductCardView()?.setShopBadgesVisible(hasAnyBadgesShown)

        if(hasAnyBadgesShown) {
            getProductCardView()?.removeAllShopBadges()
            loopBadgesListToLoadShopBadgeIcon(productItem.badgesList)
        }
    }

    protected fun hasAnyBadgesShown(productItem: ProductItemViewModel): Boolean {
        return productItem.badgesList.any { badge -> badge.isShown }
    }

    protected fun loopBadgesListToLoadShopBadgeIcon(badgesList: List<BadgeItemViewModel>) {
        for (badgeItem in badgesList) {
            if (badgeItem.isShown) {
                loadShopBadgesIcon(badgeItem.imageUrl ?: "")
            }
        }
    }

    protected fun loadShopBadgesIcon(url: String) {
        if(!TextUtils.isEmpty(url)) {
            val view = LayoutInflater.from(context).inflate(R.layout.search_product_card_badge_layout, null)

            ImageHandler.loadImageBitmap2(context, url, object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    loadShopBadgeSuccess(view, bitmap)
                }

                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    super.onLoadFailed(e, errorDrawable)
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

    protected fun initLocationTextView(productItem: ProductItemViewModel) {
        val isShopLocationShown = isShopLocationShown(productItem)
        getProductCardView()?.setShopLocationVisible(isShopLocationShown)

        if(isShopLocationShown) {
            getProductCardView()?.setShopLocationText(productItem.shopCity)
        }
    }

    protected fun isShopLocationShown(productItem: ProductItemViewModel) : Boolean {
        return !TextUtils.isEmpty(productItem.shopCity)
    }

    protected fun initCredibilitySection(productItem: ProductItemViewModel) {
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
    }

    protected fun initRatingAndReview(productItem: ProductItemViewModel) {
        initRatingView(productItem)
        initReviewCount(productItem)
    }

    protected fun initRatingView(productItem: ProductItemViewModel) {
        val isImageRatingVisible = isImageRatingVisible(productItem)

        getProductCardView()?.setImageRatingVisible(isImageRatingVisible)

        if(isImageRatingVisible) {
            getProductCardView()?.setRating(getStarCount(productItem))
        }
    }

    protected fun isImageRatingVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.rating != 0
    }

    protected fun getStarCount(productItem: ProductItemViewModel): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            Math.round(productItem.rating.toFloat())
    }

    protected fun initReviewCount(productItem: ProductItemViewModel) {
        val isReviewCountVisible = isReviewCountVisible(productItem)
        getProductCardView()?.setReviewCountVisible(isReviewCountVisible)

        if(isReviewCountVisible) {
            getProductCardView()?.setReviewCount(productItem.countReview)
        }
    }

    protected fun isReviewCountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.countReview != 0
    }

    protected fun initCredibilityLabel(productItem: ProductItemViewModel) {
        val isCredibilityLabelVisible = isCredibilityLabelVisible(productItem)
        getProductCardView()?.setLabelCredibilityVisible(isCredibilityLabelVisible)

        if (isCredibilityLabelVisible) {
            val credibilityLabelViewModel : LabelGroupViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)!!

            getProductCardView()?.setLabelCredibilityType(credibilityLabelViewModel.type)
            getProductCardView()?.setLabelCredibilityText(credibilityLabelViewModel.title)
        }
    }

    protected fun isCredibilityLabelVisible(productItem: ProductItemViewModel): Boolean {
        return isRatingAndReviewNotVisible(productItem)
                && isCredibilityLabelExists(productItem)
    }

    protected fun isRatingAndReviewNotVisible(productItem: ProductItemViewModel): Boolean {
        return !isImageRatingVisible(productItem) && !isReviewCountVisible(productItem)
    }

    protected fun isCredibilityLabelExists(productItem: ProductItemViewModel): Boolean {
        return getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY) != null
    }

    protected fun initOffersLabel(productItem: ProductItemViewModel) {
        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)

        getProductCardView()?.setLabelOffersVisible(offersLabelViewModel != null)

        if(offersLabelViewModel != null) {
            setOffersLabelContent(offersLabelViewModel)
        }
    }

    protected fun setOffersLabelContent(offersLabelViewModel: LabelGroupViewModel) {
        getProductCardView()?.setLabelOffersType(offersLabelViewModel.type)
        getProductCardView()?.setLabelOffersText(offersLabelViewModel.title)
    }

    protected fun initTopAdsIcon(productItem: ProductItemViewModel) {
        getProductCardView()?.setImageTopAdsVisible(productItem.isTopAds)
    }

    protected fun finishBindViewHolder() {
        getProductCardView()?.realignLayout()
    }
}