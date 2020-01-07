package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.*
import com.tokopedia.shop.newproduct.view.datamodel.ShopSellerEmptyProductViewModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductAddInfoViewHolder(
        val view: View
) : AbstractViewHolder<ShopSellerEmptyProductViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_seller_empty_state
    }

    init {
        initLayout()
    }

    lateinit var labelShopProductSellerEmptyState: Typography
    lateinit var imageViewBackgroundPattern: ImageView
    lateinit var imageViewStar: ImageView
    lateinit var imageViewTokopediaIcon: ImageView


    override fun bind(shopSellerEmptyProductViewModel: ShopSellerEmptyProductViewModel) {
        labelShopProductSellerEmptyState.text = MethodChecker.fromHtml(
                view.resources.getString(R.string.shop_product_seller_empty_state_label)
        )
        ImageHandler.loadImage(
                itemView.context,
                imageViewBackgroundPattern,
                URL_IMAGE_SELLER_EMPTY_STATE_BACKGROUND_PATTERN,
                R.drawable.ic_loading_image
        )
        ImageHandler.loadImage(
                itemView.context,
                imageViewStar,
                URL_IMAGE_SELLER_EMPTY_STATE_STAR_ICON,
                R.drawable.ic_loading_image
        )
        ImageHandler.loadImage(
                itemView.context,
                imageViewTokopediaIcon,
                URL_IMAGE_SELLER_EMPTY_STATE_TOKOPEDIA_ICON,
                R.drawable.ic_loading_image
        )
    }

    private fun initLayout() {
        labelShopProductSellerEmptyState = view.findViewById(R.id.label_shop_product_seller_empty_state)
        imageViewBackgroundPattern = view.findViewById(R.id.image_view_background_pattern)
        imageViewStar = view.findViewById(R.id.image_view_star)
        imageViewTokopediaIcon = view.findViewById(R.id.image_view_tokopedia_icon)
    }
}