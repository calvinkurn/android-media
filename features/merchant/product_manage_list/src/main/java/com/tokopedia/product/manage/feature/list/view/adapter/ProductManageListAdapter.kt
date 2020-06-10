package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.view.adapter.base.BaseProductManageAdapter
import com.tokopedia.product.manage.feature.list.extension.findIndex
import com.tokopedia.product.manage.feature.list.view.adapter.differ.ProductListDiffer
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.product.manage.feature.list.view.model.PriceViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class ProductManageListAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactoryImpl
) : BaseProductManageAdapter<ProductViewModel, ProductManageAdapterFactoryImpl>(baseListAdapterTypeFactory, ProductListDiffer()) {

    fun updatePrice(productId: String, price: String) {
        submitList(productId) {
            val formattedPrice = price.toIntOrZero().getCurrencyFormatted()
            val editedPrice = PriceViewModel(price, formattedPrice)
            it.copy(minPrice = editedPrice, maxPrice = editedPrice)
        }
    }

    fun updatePrice(editResult: EditVariantResult) {
        submitList(editResult.productId) {
            val editedMinPrice = editResult.variants.minBy { it.price }?.price.orZero()
            val editedMaxPrice = editResult.variants.maxBy { it.price }?.price.orZero()

            val minPrice = PriceViewModel(editedMinPrice.toString(), editedMinPrice.getCurrencyFormatted())
            val maxPrice = PriceViewModel(editedMaxPrice.toString(), editedMaxPrice.getCurrencyFormatted())
            it.copy(minPrice = minPrice, maxPrice = maxPrice)
        }
    }

    fun updateStock(productId: String, stock: Int, status: ProductStatus) {
        submitList(productId) { it.copy(stock = stock, status = status) }
    }

    fun updateCashBack(productId: String, cashback: Int) {
        submitList(productId) { it.copy(cashBack = cashback) }
    }

    fun deleteProduct(productId: String) {
        val items = data.toMutableList()
        items.findIndex(productId)?.let { index ->
            items.removeAt(index)
            submitList(items)
        }
    }

    fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        submitList(productId) { it.copy(isFeatured = isFeaturedProduct) }
    }

    fun setProductStatus(productId: String, productStatus: ProductStatus) {
        submitList(productId) { it.copy(status = productStatus) }
    }

    fun setMultiSelectEnabled(multiSelectEnabled: Boolean) {
        val items = data.map {
            it.copy(multiSelectActive = multiSelectEnabled, isChecked = false)
        }
        submitList(items)
    }

    fun filterProductList(predicate: (ProductViewModel) -> Boolean) {
        val productList = data.filter { predicate.invoke(it) }
        submitList(productList)
    }

    private fun submitList(productId: String, update: (ProductViewModel) -> ProductViewModel) {
        val items = data.toMutableList()
        val index = items.findIndex(productId)
        index?.let {
            items[it] = update.invoke(items[it])
            submitList(items)
        }
    }
}