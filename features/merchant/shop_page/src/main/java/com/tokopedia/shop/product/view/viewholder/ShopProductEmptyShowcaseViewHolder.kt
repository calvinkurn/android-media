package com.tokopedia.shop.product.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.product.view.datamodel.ShopProductEmptyShowcaseUiModel
import com.tokopedia.shop.product.view.listener.ShopShowcaseEmptySearchListener
import kotlinx.android.synthetic.main.shop_search_product_empty_state.view.*

class ShopProductEmptyShowcaseViewHolder(val view: View,
                                         private val shopShowcaseEmptySearchListener: ShopShowcaseEmptySearchListener?): AbstractViewHolder<ShopProductEmptyShowcaseUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_showcase_product_empty_state
    }
    override fun bind(element: ShopProductEmptyShowcaseUiModel) {
        with(itemView) {
            try {
                if(shopSearchEmptyState?.context?.isValidGlideContext() == true)
                    shopSearchEmptyState?.setImageUrl(ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE)
            } catch (e: Throwable) { }
            shopSearchEmptyState?.setPrimaryCTAClickListener {
                shopShowcaseEmptySearchListener?.onShowcaseEmptyBackButtonClicked()
            }
        }
    }

}