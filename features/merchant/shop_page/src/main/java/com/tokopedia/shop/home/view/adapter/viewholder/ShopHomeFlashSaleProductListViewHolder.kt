package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeFlashSaleProductListViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private var productCardList: ProductCardListView? = itemView.findViewById(R.id.fs_product_card_list)

    fun bindData(uiModel: ShopHomeProductUiModel) {
        productCardList?.setProductModel(
            ShopPageHomeMapper.mapToProductCardModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = uiModel,
                isWideContent = true
            ))
    }
}