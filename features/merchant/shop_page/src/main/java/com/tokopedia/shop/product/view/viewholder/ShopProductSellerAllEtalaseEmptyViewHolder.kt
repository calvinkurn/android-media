package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.*
import com.tokopedia.shop.product.view.datamodel.ShopSellerEmptyProductAllEtalaseViewModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductSellerAllEtalaseEmptyViewHolder(
        val view: View
) : AbstractViewHolder<ShopSellerEmptyProductAllEtalaseViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_seller_empty_state
    }

    init {
        initLayout()
    }

    lateinit var labelShopProductSellerEmptyState: Typography
    lateinit var imageViewBackgroundPattern: ImageView

    override fun bind(shopSellerEmptyProductAllEtalaseViewModel: ShopSellerEmptyProductAllEtalaseViewModel) {
        labelShopProductSellerEmptyState.text = MethodChecker.fromHtml(
                view.resources.getString(R.string.shop_product_seller_empty_state_label)
        )
        ImageHandler.loadImage(
                itemView.context,
                imageViewBackgroundPattern,
                URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND,
                com.tokopedia.design.R.drawable.ic_loading_image
        )
    }

    private fun initLayout() {
        labelShopProductSellerEmptyState = view.findViewById(R.id.label_shop_product_seller_empty_state)
        imageViewBackgroundPattern = view.findViewById(R.id.image_view_background)
    }
}