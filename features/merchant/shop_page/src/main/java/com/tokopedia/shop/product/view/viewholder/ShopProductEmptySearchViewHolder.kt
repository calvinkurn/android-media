package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.product.view.datamodel.ShopProductEmptySearchUiModel
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener

class ShopProductEmptySearchViewHolder(val view: View,
                                       private val shopProductEmptySearchListener: ShopProductEmptySearchListener?): AbstractViewHolder<ShopProductEmptySearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_search_product_empty_state
    }

    private val shopSearchEmptyState: EmptyStateUnify? = itemView.findViewById(R.id.shopSearchEmptyState)

    override fun bind(element: ShopProductEmptySearchUiModel) {
        try {
            if(shopSearchEmptyState?.context?.isValidGlideContext() == true)
                shopSearchEmptyState.setImageUrl(ShopPageConstant.URL_IMAGE_BUYER_SHOP_SEARCH_EMPTY_STATE)
        } catch (e: Throwable) { }
        shopSearchEmptyState?.setPrimaryCTAClickListener {
            shopProductEmptySearchListener?.onPrimaryButtonEmptyClicked()
        }
        shopSearchEmptyState?.setSecondaryCTAClickListener {
            shopProductEmptySearchListener?.onSecondaryButtonEmptyClicked()
        }
    }

}