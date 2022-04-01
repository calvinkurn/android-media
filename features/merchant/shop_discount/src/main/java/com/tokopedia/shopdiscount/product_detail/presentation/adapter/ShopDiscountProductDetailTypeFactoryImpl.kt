package com.tokopedia.shopdiscount.product_detail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailShimmeringUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailItemViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailListGlobalErrorViewHolder
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.viewholder.ShopDiscountProductDetailShimmeringViewHolder

class ShopDiscountProductDetailTypeFactoryImpl(
    private val shopDiscountProductDetailItemViewHolderListener: ShopDiscountProductDetailItemViewHolder.Listener,
    private val globalErrorListener: ShopDiscountProductDetailListGlobalErrorViewHolder.Listener
) : BaseAdapterTypeFactory(), ShopDiscountProductDetailTypeFactory {
    override fun type(uiModel: ShopDiscountProductDetailUiModel): Int {
        return ShopDiscountProductDetailItemViewHolder.LAYOUT
    }

    override fun type(shopDiscountProductDetailShimmeringUiModel: ShopDiscountProductDetailShimmeringUiModel): Int {
        return ShopDiscountProductDetailShimmeringViewHolder.LAYOUT
    }

    override fun type(shopDiscountProductDetailListGlobalErrorUiModel: ShopDiscountProductDetailListGlobalErrorUiModel): Int {
        return ShopDiscountProductDetailListGlobalErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopDiscountProductDetailItemViewHolder.LAYOUT -> {
                ShopDiscountProductDetailItemViewHolder(
                    view,
                    shopDiscountProductDetailItemViewHolderListener
                )
            }
            ShopDiscountProductDetailShimmeringViewHolder.LAYOUT -> {
                ShopDiscountProductDetailShimmeringViewHolder(view)
            }
            ShopDiscountProductDetailListGlobalErrorViewHolder.LAYOUT -> {
                ShopDiscountProductDetailListGlobalErrorViewHolder(view, globalErrorListener)
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }

}