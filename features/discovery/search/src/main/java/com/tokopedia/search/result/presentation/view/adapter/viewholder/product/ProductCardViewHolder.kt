package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_product_card.view.*

private const val LABEL_GROUP_POSITION_PROMO = "promo"
private const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
private const val LABEL_GROUP_POSITION_OFFERS = "offers"

open class ProductCardViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_product_card
    }

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
        if (productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButton(productItem)
        initPromoLabel(productItem)
        initShopName(productItem)
        initTitleTextView(productItem)
        initSlashPrice(productItem)
        initPriceTextView(productItem)
        initShopBadge(productItem)
        initLocationTextView(productItem)
        initCredibilitySection(productItem)
        initOffersLabel(productItem)
        initTopAdsIcon(productItem)
    }

    private fun initProductCardContainer(productItem: ProductItemViewModel) {
        itemView.productCardView?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    private fun initProductImage(productItem: ProductItemViewModel) {
        itemView.productCardView?.setImageUrl(productItem.imageUrl)

        itemView.productCardView?.imageView?.setViewHintListener(productItem){
            productListener.onProductImpressed(productItem, adapterPosition)
        }
    }

    private fun initWishlistButton(productItem: ProductItemViewModel) {
        itemView.productCardView?.setWishlistButtonVisible(productItem.isWishlistButtonEnabled)
        itemView.productCardView?.setWishlistButtonImage(productItem.isWishlisted)
        itemView.productCardView?.setWishlistButtonOnClickListener {
            if (productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    private fun initPromoLabel(productItem: ProductItemViewModel) {
        val promoLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)

        if(promoLabelViewModel != null) {
            itemView.productCardView?.setPromoLabelVisible(true)
            itemView.productCardView?.setPromoLabelType(promoLabelViewModel.type)
            itemView.productCardView?.setPromoLabelText(promoLabelViewModel.title)
        }
        else {
            itemView.productCardView?.setPromoLabelVisible(false)
        }
    }

    @Nullable
    private fun getFirstLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): LabelGroupViewModel? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    private fun getLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): List<LabelGroupViewModel>? {
        return productItem.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    private fun initShopName(productItem: ProductItemViewModel) {
        val isShopNameShown = isShopNameShown(productItem)
        itemView.productCardView?.setTextShopNameVisible(isShopNameShown)

        if(isShopNameShown) {
            itemView.productCardView?.setTextShopName(productItem.shopName)
        }
    }

    private fun isShopNameShown(productItem: ProductItemViewModel): Boolean {
        return productItem.isShopOfficialStore
    }

    private fun initTitleTextView(productItem: ProductItemViewModel) {
        setTitleMarginTop(productItem)

        itemView.productCardView?.setTitle(productItem.productName)
    }

    private fun setTitleMarginTop(productItem: ProductItemViewModel) {
        var paddingTopPixel = 0

        if(!isShopNameShown(productItem)) {
            paddingTopPixel = convertDpToPixel(8f)
        }

        itemView.productCardView?.setTitleMarginsWithNegativeDefaultValue(-1, paddingTopPixel, -1, -1)
    }

    private fun convertDpToPixel(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
    }

    private fun initSlashPrice(productItem: ProductItemViewModel) {
        itemView.productCardView?.setDiscount(productItem.discountPercentage)
        itemView.productCardView?.setSlashedPrice(productItem.originalPrice)
    }

    private fun initPriceTextView(productItem: ProductItemViewModel) {
        itemView.productCardView?.setPrice(getPriceText(productItem))
    }

    private fun getPriceText(productItem: ProductItemViewModel) : String {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    private fun initShopBadge(productItem: ProductItemViewModel) {
        itemView.productCardView?.clearShopBadgesContainer()

        val hasAnyBadgesShown = hasAnyBadgesShown(productItem)
        itemView.productCardView?.setShopBadgesVisible(hasAnyBadgesShown)

        if(hasAnyBadgesShown) {
            loopBadgesListToLoadShopBadgeIcon(productItem.badgesList)
        }
    }

    private fun hasAnyBadgesShown(productItem: ProductItemViewModel): Boolean {
        return productItem.badgesList.any { badge -> badge.isShown }
    }

    private fun loopBadgesListToLoadShopBadgeIcon(badgesList: List<BadgeItemViewModel>) {
        for (badgeItem in badgesList) {
            if (badgeItem.isShown) {
                loadShopBadgesIcon(badgeItem.imageUrl ?: "")
            }
        }
    }

    private fun loadShopBadgesIcon(url: String) {
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

    private fun loadShopBadgeSuccess(view: View, bitmap: Bitmap) {
        val image = view.findViewById<ImageView>(R.id.badge)

        if (bitmap.height <= 1 && bitmap.width <= 1) {
            view.visibility = View.GONE
        } else {
            image.setImageBitmap(bitmap)
            view.visibility = View.VISIBLE
            itemView.productCardView?.addShopBadge(view)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    private fun initLocationTextView(productItem: ProductItemViewModel) {
        val isShopCityShown = isShopCityShown(productItem)
        itemView.productCardView?.setTextLocationVisible(isShopCityShown)

        if(isShopCityShown) {
            itemView.productCardView?.setTextLocation(productItem.shopCity)
            setLocationTextViewMargin(productItem)
        }
    }

    private fun isShopCityShown(productItem: ProductItemViewModel) : Boolean {
        return !TextUtils.isEmpty(productItem.shopCity)
    }

    private fun setLocationTextViewMargin(productItem: ProductItemViewModel) {
        val marginLeftDp = if (hasAnyBadgesShown(productItem)) 4f else 8f
        val marginLeftPixel = convertDpToPixel(marginLeftDp)

        itemView.productCardView?.setTextLocationMarginsWithNegativeDefaultValue(marginLeftPixel, -1, -1, -1)
    }

    private fun initCredibilitySection(productItem: ProductItemViewModel) {
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
    }

    private fun initRatingAndReview(productItem: ProductItemViewModel) {
        initRatingView(productItem)
        initReviewCount(productItem)
    }

    private fun initRatingView(productItem: ProductItemViewModel) {
        val isRatingViewVisible = isRatingViewVisible(productItem)

        itemView.productCardView?.setRatingVisible(isRatingViewVisible(productItem))

        if(isRatingViewVisible) {
            itemView.productCardView?.setRating(getStarCount(productItem))
        }
    }

    private fun isRatingViewVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.rating != 0
    }

    private fun getStarCount(productItem: ProductItemViewModel): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            Math.round(productItem.rating.toFloat())
    }

    private fun initReviewCount(productItem: ProductItemViewModel) {
        val isReviewCountVisible = isReviewCountVisible(productItem)
        itemView.productCardView?.setReviewVisible(isReviewCountVisible)

        if(isReviewCountVisible) {
            itemView.productCardView?.setReviewCount(productItem.countReview)
            setReviewCountMargin(productItem)
        }
    }

    private fun isReviewCountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.countReview != 0
    }

    private fun setReviewCountMargin(productItem: ProductItemViewModel) {
        val marginLeftDp = if (isRatingViewVisible(productItem)) 4f else 8f
        val marginLeftPixel = convertDpToPixel(marginLeftDp)

        itemView.productCardView?.setReviewCountMarginsWithNegativeDefaultValue(marginLeftPixel, -1, -1, -1)
    }

    private fun initCredibilityLabel(productItem: ProductItemViewModel) {
        val isCredibilityLabelVisible = isCredibilityLabelVisible(productItem)
        itemView.productCardView?.setCredibilityLabelVisible(isCredibilityLabelVisible)

        if (isCredibilityLabelVisible) {
            val credibilityLabelViewModel : LabelGroupViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)!!

            itemView.productCardView?.setCredibilityLabelType(credibilityLabelViewModel.type)
            itemView.productCardView?.setCredibilityLabelText(credibilityLabelViewModel.title)
        }
    }

    private fun isCredibilityLabelVisible(productItem: ProductItemViewModel): Boolean {
        return isRatingAndReviewNotVisible(productItem)
                && isCredibilityLabelExists(productItem)
    }

    private fun isRatingAndReviewNotVisible(productItem: ProductItemViewModel): Boolean {
        return !isRatingViewVisible(productItem) && !isReviewCountVisible(productItem)
    }

    private fun isCredibilityLabelExists(productItem: ProductItemViewModel): Boolean {
        return getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY) != null
    }

    private fun initOffersLabel(productItem: ProductItemViewModel) {
        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)

        if(offersLabelViewModel != null) {
            setOffersLabelContent(offersLabelViewModel)
        }
        else {
            itemView.productCardView?.setOffersLabelVisible(false)
        }

        setOffersLabelPosition(productItem)
    }

    private fun setOffersLabelContent(offersLabelViewModel: LabelGroupViewModel) {
        itemView.productCardView?.setOffersLabelVisible(true)
        itemView.productCardView?.setOffersLabelType(offersLabelViewModel.type)
        itemView.productCardView?.setOffersLabelText(offersLabelViewModel.title)
    }

    private fun setOffersLabelPosition(productItem: ProductItemViewModel) {
        when {
            isCredibilityLabelVisible(productItem) -> {
                itemView.productCardView?.setOffersLabelConstraintTopToBottomOfCredibilityLabel()
            }
            isRatingViewVisible(productItem) -> {
                itemView.productCardView?.setOffersLabelConstraintTopToBottomOfRatingView()
            }
            isReviewCountVisible(productItem) -> {
                itemView.productCardView?.setOffersLabelConstraintTopToBottomOfReviewCount()
            }
            else -> {
                itemView.productCardView?.setOffersLabelConstraintTopToBottomOfTextLocation()
            }
        }
    }

    private fun initTopAdsIcon(productItem: ProductItemViewModel) {
        itemView.productCardView?.setTopAdsVisible(productItem.isTopAds)
    }
}