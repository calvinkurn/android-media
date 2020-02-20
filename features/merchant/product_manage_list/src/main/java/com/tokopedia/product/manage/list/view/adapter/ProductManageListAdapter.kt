package com.tokopedia.product.manage.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.product.manage.list.view.factory.ProductManageFragmentFactoryImpl
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel

class ProductManageListAdapter(baseListAdapterTypeFactory: ProductManageFragmentFactoryImpl,
                               onCheckableAdapterListener: OnCheckableAdapterListener<ProductManageViewModel>)
    : BaseListCheckableAdapter<ProductManageViewModel, ProductManageFragmentFactoryImpl>(baseListAdapterTypeFactory, onCheckableAdapterListener) {

    fun updatePrice(productId: String, price: String, currencyId: String, priceCurrency: String) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                it.productPricePlain = price
                it.productCurrencyId = currencyId.toInt()
                it.productCurrencySymbol = priceCurrency
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateCashback(productId: String, cashback: Int) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                it.productCashback = cashback
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        data.forEachIndexed { index, productManageViewModel ->
            if (productManageViewModel.id.equals(productId, ignoreCase = true)) {
                productManageViewModel.isFeatureProduct = isFeaturedProduct
                notifyItemChanged(index)
                return
            }
        }
    }

}