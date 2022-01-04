package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND
import com.tokopedia.shop.databinding.ItemShopProductSellerEmptyStateBinding
import com.tokopedia.shop.product.view.datamodel.ShopSellerEmptyProductAllEtalaseUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

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

    private val viewBinding : ItemShopProductSellerEmptyStateBinding? by viewBinding()
    private var labelShopProductSellerEmptyState: Typography? = viewBinding?.labelShopProductSellerEmptyState
    private var imageViewBackgroundPattern: ImageView? = viewBinding?.imageViewBackground

    override fun bind(shopSellerEmptyProductAllEtalaseUiModel: ShopSellerEmptyProductAllEtalaseUiModel) {
        labelShopProductSellerEmptyState?.text = MethodChecker.fromHtml(
                view.resources.getString(R.string.shop_product_seller_empty_state_label)
        )
        imageViewBackgroundPattern?.loadImage(URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
    }

}