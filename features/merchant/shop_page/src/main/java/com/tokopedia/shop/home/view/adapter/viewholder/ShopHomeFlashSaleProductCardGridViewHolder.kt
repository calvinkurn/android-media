package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductCardGridViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var productCardGrid: ProductCardGridView? = itemView.findViewById(R.id.fs_product_card_grid)

    fun bindData(uiModel: ShopHomeProductUiModel) {
        productCardGrid?.setProductModel(
            ShopPageHomeMapper.mapToProductCardModel(
            isHasAddToCartButton = false,
            hasThreeDots = false,
            shopHomeProductViewModel = uiModel,
            isWideContent = true
        ))
    }
}