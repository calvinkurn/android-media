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
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.newproduct.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.newproduct.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.newproduct.view.listener.ShopProductClickedListener
import com.tokopedia.shop.newproduct.view.listener.ShopProductImpressionListener

import java.text.NumberFormat
import java.text.ParseException

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
    lateinit var productCard: ProductCardGridView
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
        productCard.setProductModel(
                ShopPageProductListMapper.mapToProductCardModel(shopProductViewModel, true)
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

        productCard.setImageProductViewHintListener(shopProductViewModel, object: ViewHintListener{
            override fun onViewHint() {

            }

        })

        productCard.setThreeDotsOnClickListener {
            shopProductClickedListener?.onThreeDotsClicked(shopProductViewModel, shopTrackType)
        }
    }
}