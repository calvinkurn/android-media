package com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel
import com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.viewholder.ShopDiscountManageProductVariantItemViewHolder
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountManageProductVariantDiscountFragmentListener

class ShopDiscountManageProductVariantDiscountTypeFactoryImpl(
    private val variantItemListener: ShopDiscountManageProductVariantItemViewHolder.Listener,
    private val fragmentListener: ShopDiscountManageProductVariantDiscountFragmentListener
) : BaseAdapterTypeFactory(), ShopDiscountManageProductVariantDiscountTypeFactory {

    override fun type(uiModel: ShopDiscountManageProductVariantItemUiModel): Int {
        return ShopDiscountManageProductVariantItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopDiscountManageProductVariantItemViewHolder.LAYOUT -> {
                ShopDiscountManageProductVariantItemViewHolder(view, variantItemListener, fragmentListener)
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }

}