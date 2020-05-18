package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class ProductManageListAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactoryImpl
) : BaseListAdapter<ProductViewModel, ProductManageAdapterFactoryImpl>(baseListAdapterTypeFactory) {

    fun updatePrice(productId: String, price: String) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data[index] = it.copy(price = price, priceFormatted = price.toInt().getCurrencyFormatted())
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateStock(productId: String, stock: Int, status: ProductStatus) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data[index] = it.copy(stock = stock, status = status)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun updateCashback(productId: String, cashback: Int) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data[index] = it.copy(cashBack = cashback)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun deleteProduct(productId: String) {
        data.forEachIndexed { index, it ->
            if (it.id.equals(productId, ignoreCase = true)) {
                data.removeAt(index)
                notifyItemRemoved(index)
                return
            }
        }
    }

    fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        data.forEachIndexed { index, product ->
            if (product.id.equals(productId, ignoreCase = true)) {
                data[index] = product.copy(isFeatured = isFeaturedProduct)
                notifyItemChanged(index)
            }
        }
    }

    fun setProductStatus(productId: String, productStatus: ProductStatus) {
        data.forEachIndexed { index, product ->
            if (product.id.equals(productId, ignoreCase = true)) {
                data[index] = product.copy(status = productStatus)
                return
            }
        }
    }
}