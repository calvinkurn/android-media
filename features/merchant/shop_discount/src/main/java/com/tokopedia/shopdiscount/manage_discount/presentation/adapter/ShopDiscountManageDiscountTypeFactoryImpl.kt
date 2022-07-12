package com.tokopedia.shopdiscount.manage_discount.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountManageDiscountGlobalErrorUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductShimmeringUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountManageDiscountGlobalErrorViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountSetupProductItemViewHolder
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.viewholder.ShopDiscountSetupProductShimmeringViewHolder

class ShopDiscountManageDiscountTypeFactoryImpl(
    private val shopDiscountSetupProductItemListener: ShopDiscountSetupProductItemViewHolder.Listener,
    private val shopDiscountManageDiscountGlobalErrorViewHolder: ShopDiscountManageDiscountGlobalErrorViewHolder.Listener
) : BaseAdapterTypeFactory(), ShopDiscountManageDiscountTypeFactory {

    override fun type(uiModel: ShopDiscountSetupProductUiModel.SetupProductData): Int {
        return ShopDiscountSetupProductItemViewHolder.LAYOUT
    }

    override fun type(uiModel: ShopDiscountSetupProductShimmeringUiModel): Int {
        return ShopDiscountSetupProductShimmeringViewHolder.LAYOUT
    }

    override fun type(shopDiscountManageDiscountGlobalErrorUiModel: ShopDiscountManageDiscountGlobalErrorUiModel): Int {
        return ShopDiscountManageDiscountGlobalErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopDiscountSetupProductItemViewHolder.LAYOUT -> {
                ShopDiscountSetupProductItemViewHolder(view, shopDiscountSetupProductItemListener)
            }
            ShopDiscountSetupProductShimmeringViewHolder.LAYOUT -> {
                ShopDiscountSetupProductShimmeringViewHolder(view)
            }
            ShopDiscountManageDiscountGlobalErrorViewHolder.LAYOUT -> {
                ShopDiscountManageDiscountGlobalErrorViewHolder(
                    view,
                    shopDiscountManageDiscountGlobalErrorViewHolder
                )
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }

}