package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatRatingBar

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductImpressionListener

import java.text.NumberFormat
import java.text.ParseException
import java.util.ArrayList

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductViewHolder(
        itemView: View,
        private val shopProductClickedListener: ShopProductClickedListener?,
        private val shopProductImpressionListener: ShopProductImpressionListener?,
        private val isFixWidth: Boolean,
        private val deviceWidth: Int,
        @param:ShopTrackProductTypeDef @field:ShopTrackProductTypeDef private val shopTrackType: Int,
        private val layoutType: Int
) : AbstractViewHolder<ShopProductViewModel>(itemView) {
    private val wishlistImageView: ImageView? = null
    private val wishlistContainer: FrameLayout? = null
    private var productImageView: ImageView? = null
    private var titleTextView: TextView? = null
    private val displayedPriceTextView: TextView? = null
    private val originalPriceTextView: TextView? = null
    private val discountPercentageTextView: TextView? = null
    private val cashBackTextView: TextView? = null
    private val wholesaleTextView: TextView? = null
    private val preOrderTextView: TextView? = null
    private val freeReturnImageView: ImageView? = null
    private val freeOngkirBanner: ImageView? = null
    private val qualityRatingBar: AppCompatRatingBar? = null
    private val totalReview: TextView? = null
    private val soldOutView: View? = null
    lateinit var productCard: ProductCardViewSmallGrid
    private val vgRating: View? = null
    private val badgeContainer: View? = null

    init {
        findViews(itemView)
    }

    companion object {
        @LayoutRes
        val GRID_LAYOUT = R.layout.item_shop_newproduct_grid
        const val RATIO_WITH_RELATIVE_TO_SCREEN = 2.3
    }

    private fun findViews(view: View) {
        productCard = view.findViewById(R.id.product_card)
    }

    override fun bind(shopProductViewModel: ShopProductViewModel) {
        val totalReview = try {
            NumberFormat.getInstance().parse(shopProductViewModel.totalReview).toInt()
        } catch (ignored: ParseException) {
            0
        }
        val discountPercentage = if (shopProductViewModel.discountPercentage == "0") {
            ""
        } else {
            "${shopProductViewModel.discountPercentage}%"
        }
        val freeOngkirObject = ProductCardModel.FreeOngkir(shopProductViewModel.isShowFreeOngkir, shopProductViewModel.freeOngkirPromoIcon!!)
        productCard.setProductModel(
                ProductCardModel(
                        shopProductViewModel.imageUrl!!,
                        shopProductViewModel.isWishList,
                        shopProductViewModel.isShowWishList,
                        ProductCardModel.Label(),
                        "",
                        "",
                        shopProductViewModel.name!!,
                        discountPercentage,
                        shopProductViewModel.originalPrice!!,
                        shopProductViewModel.displayedPrice!!,
                        ArrayList(),
                        "",
                        shopProductViewModel.rating.toInt(),
                        totalReview,
                        ProductCardModel.Label(),
                        ProductCardModel.Label(),
                        freeOngkirObject,
                        false
                ).apply {
                    isProductSoldOut = shopProductViewModel.isSoldOut
                    isProductPreOrder = shopProductViewModel.isPo
                    isProductWholesale = shopProductViewModel.isWholesale
                },
                BlankSpaceConfig()
        )

        productCard.setImageProductViewHintListener(shopProductViewModel, object : ViewHintListener {
            override fun onViewHint() {
                shopProductImpressionListener?.onProductImpression(shopProductViewModel, shopTrackType, adapterPosition)
            }
        })

        if (isFixWidth && deviceWidth > 0 && layoutType == ShopProductViewHolder.GRID_LAYOUT) {
            itemView.layoutParams.width = (deviceWidth / RATIO_WITH_RELATIVE_TO_SCREEN).toInt()
        }

        productCard.setOnClickListener {
            shopProductClickedListener?.onProductClicked(shopProductViewModel, shopTrackType, adapterPosition)
        }
        productCard.setButtonWishlistOnClickListener {
            if (!shopProductViewModel.isSoldOut)
                shopProductClickedListener?.onWishListClicked(shopProductViewModel, shopTrackType)
        }

        productCard.setImageProductViewHintListener(shopProductViewModel, object: ViewHintListener{
            override fun onViewHint() {

            }

        })

        if (shopProductViewModel.isCarousel) {
            if (shopProductViewModel.rating <= 0 && totalReview <= 0) {
                productCard.setImageRatingInvisible(true)
                productCard.setReviewCountInvisible(true)
            }

            if (!freeOngkirObject.isActive || freeOngkirObject.imageUrl.isEmpty()) {
                productCard.setFreeOngkirInvisible(true)
            }
            if (!shopProductViewModel.isPo && !shopProductViewModel.isWholesale) {
                productCard.setLabelPreOrderInvisible(true)
            }
            if (shopProductViewModel.discountPercentage.toIntOrZero() <= 0) {
                productCard.setlabelDiscountInvisible(true)
                productCard.setSlashedPriceInvisible(true)
            }
        }
    }
}