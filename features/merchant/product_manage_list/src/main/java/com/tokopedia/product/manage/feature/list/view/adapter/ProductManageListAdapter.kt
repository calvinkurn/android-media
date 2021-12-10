package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.view.adapter.base.BaseProductManageAdapter
import com.tokopedia.product.manage.feature.list.extension.findIndex
import com.tokopedia.product.manage.feature.list.view.adapter.differ.ProductListDiffer
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class ProductManageListAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactoryImpl
) : BaseProductManageAdapter<Visitable<*>, ProductManageAdapterFactoryImpl>(baseListAdapterTypeFactory, ProductListDiffer()) {

    fun updateProduct(itemList: List<Visitable<*>>) {
        submitList(itemList)
    }

    fun removeEmptyAndUpdateLayout(itemList: List<Visitable<*>>) {
        val items = data.filter { it !is EmptyModel }.toMutableList().apply {
            addAll(itemList)
        }
        submitList(items)
    }

    fun checkAllProducts(itemsChecked: MutableList<ProductUiModel>,
                         onSetItemsChecked: (MutableList<ProductUiModel>) -> Unit) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        val newItems = items.map { product ->
            val checkedProduct = itemsChecked.firstOrNull { it.id == product.id }
            if (checkedProduct == null) {
                itemsChecked.add(product)
            }
            product.copy(isChecked = true)
        }
        submitList(newItems)
        onSetItemsChecked(itemsChecked)
    }

    fun unCheckMultipleProducts(productIds: List<String>? = null,
                                itemsChecked: MutableList<ProductUiModel>,
                                onSetItemsUnchecked: (MutableList<ProductUiModel>) -> Unit) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        val productIdList =
            productIds ?: items.map {
                it.id
            }
        val newItems = items.map { product ->
            if (productIdList.contains(product.id)) {
                itemsChecked.firstOrNull { it.id == product.id }?.let { checkedProduct ->
                    itemsChecked.remove(checkedProduct)
                }
                product.copy(isChecked = false)
            } else {
                product
            }
        }
        submitList(newItems)
        onSetItemsUnchecked(itemsChecked)
    }

    fun updateEmptyState(emptyModel: EmptyModel) {
        if (data.getOrNull(lastIndex) !is EmptyModel) {
            val list = data.toMutableList()
            val dataCount = data.filter { it !is EmptyModel }.count().orZero()
            if (dataCount > 0) {
                list.removeAll { it !is EmptyModel }
            }
            list.add(emptyModel)
            submitList(list)
        }
    }

    fun updatePrice(productId: String, price: String) {
        submitList(productId) {
            val formattedPrice = price.toIntOrZero().getCurrencyFormatted()
            val editedPrice = PriceUiModel(price, formattedPrice)
            it.copy(minPrice = editedPrice, maxPrice = editedPrice)
        }
    }

    fun updatePrice(editResult: EditVariantResult) {
        submitList(editResult.productId) {
            val editedMinPrice = editResult.variants.minByOrNull { it.price }?.price.orZero()
            val editedMaxPrice = editResult.variants.maxByOrNull { it.price }?.price.orZero()

            val minPrice = PriceUiModel(editedMinPrice.toString(), editedMinPrice.getCurrencyFormatted())
            val maxPrice = PriceUiModel(editedMaxPrice.toString(), editedMaxPrice.getCurrencyFormatted())
            it.copy(minPrice = minPrice, maxPrice = maxPrice)
        }
    }

    fun updateStock(productId: String, stock: Int?, status: ProductStatus?) {
        submitList(productId) {
            var product = it
            stock?.let { product = product.copy(stock = stock) }
            status?.let { product = product.copy(status = status) }
            product
        }
    }

    fun updateCashBack(productId: String, cashback: Int) {
        submitList(productId) { it.copy(cashBack = cashback) }
    }

    fun deleteProduct(productId: String) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        items.findIndex(productId)?.let { index ->
            items.removeAt(index)
            submitList(items)
        }
    }

    fun deleteProducts(productIds: List<String>) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        productIds.forEach { id ->
            items.findIndex(id)?.let { index ->
                items.removeAt(index)
            }
        }
        submitList(items)
    }

    fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        submitList(productId) { it.copy(isFeatured = isFeaturedProduct) }
    }

    fun setProductsStatuses(productIds: List<String>, productStatus: ProductStatus) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        productIds.forEach { id ->
            items.findIndex(id)?.let { index ->
                items[index] = items[index].copy(status = productStatus)
            }
        }
        submitList(items)
    }

    fun setMultiSelectEnabled(multiSelectEnabled: Boolean) {
        val items = data.filterIsInstance<ProductUiModel>().map {
            it.copy(multiSelectActive = multiSelectEnabled, isChecked = false)
        }
        submitList(items)
    }

    fun filterProductList(predicate: (ProductUiModel) -> Boolean) {
        val productList = data.filterIsInstance<ProductUiModel>().filter { predicate.invoke(it) }
        submitList(productList)
    }

    private fun submitList(productId: String, update: (ProductUiModel) -> ProductUiModel) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        val index = items.findIndex(productId)
        index?.let {
            items[it] = update.invoke(items[it])
            submitList(items)
        }
    }
}