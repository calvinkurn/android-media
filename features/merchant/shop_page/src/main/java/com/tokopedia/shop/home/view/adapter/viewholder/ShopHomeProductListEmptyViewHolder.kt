package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.databinding.NewShopProductsEmptyStateBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductListEmptyUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductListEmptyViewHolder(
    val itemView: View
) : AbstractViewHolder<ShopHomeProductListEmptyUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.new_shop_products_empty_state
    }

    private val viewBinding: NewShopProductsEmptyStateBinding? by viewBinding()
    private var imageViewEmptyImage: ImageView? = viewBinding?.imageViewEmptyImage

    override fun bind(element: ShopHomeProductListEmptyUiModel) {
        imageViewEmptyImage?.loadImage(URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
    }
}
