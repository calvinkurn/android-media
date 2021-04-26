package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND
import com.tokopedia.shop.product.view.datamodel.ShopSellerEmptyProductAllEtalaseUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductSellerAllEtalaseEmptyViewHolder(
        val view: View
) : AbstractViewHolder<ShopSellerEmptyProductAllEtalaseUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_seller_empty_state
    }

    init {
        initLayout()
    }

    lateinit var labelShopProductSellerEmptyState: Typography
    lateinit var imageViewBackgroundPattern: ImageView

    override fun bind(shopSellerEmptyProductAllEtalaseUiModel: ShopSellerEmptyProductAllEtalaseUiModel) {
        labelShopProductSellerEmptyState.text = MethodChecker.fromHtml(
                view.resources.getString(R.string.shop_product_seller_empty_state_label)
        )
        imageViewBackgroundPattern.loadImage(URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
    }

    private fun initLayout() {
        labelShopProductSellerEmptyState = view.findViewById(R.id.label_shop_product_seller_empty_state)
        imageViewBackgroundPattern = view.findViewById(R.id.image_view_background)
    }
}