package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactory
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel

class ProductManageListAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactory,
    onCheckableAdapterListener: OnCheckableAdapterListener<ProductViewModel>
) : BaseListCheckableAdapter<ProductViewModel, ProductManageAdapterFactory>(baseListAdapterTypeFactory, onCheckableAdapterListener) {

    fun updatePrice(productId: String, price: String) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data[index] = it.copy(price = price, priceFormatted = price.toInt().getCurrencyFormatted())
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateStock(productId: String, stock: Int) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data[index] = it.copy(stock = stock)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        data.forEachIndexed { index, product ->
            if (product.id.equals(productId, ignoreCase = true)) {
                data[index] = product.copy(isFeatured = isFeaturedProduct)
                notifyItemChanged(index)
                return
            }
        }
    }

}