package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.product.view.datamodel.ShopProductEmptyShowcaseUiModel
import com.tokopedia.shop.product.view.listener.ShopShowcaseEmptySearchListener

class ShopProductEmptyShowcaseViewHolder(val view: View,
                                         private val shopShowcaseEmptySearchListener: ShopShowcaseEmptySearchListener?): AbstractViewHolder<ShopProductEmptyShowcaseUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_showcase_product_empty_state
    }
    
    private val shopShowcaseEmptyState: EmptyStateUnify? = itemView.findViewById(R.id.shopShowcaseEmptyState)
    
    override fun bind(element: ShopProductEmptyShowcaseUiModel) {
        try {
            if(shopShowcaseEmptyState?.context?.isValidGlideContext() == true)
                shopShowcaseEmptyState.setImageUrl(ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE)
        } catch (e: Throwable) { }
        shopShowcaseEmptyState?.setPrimaryCTAClickListener {
            shopShowcaseEmptySearchListener?.onShowcaseEmptyBackButtonClicked()
        }
    }

}