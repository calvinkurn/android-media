package com.tokopedia.product.manage.feature.list.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.view.adapter.base.BaseProductManageAdapter
import com.tokopedia.product.manage.common.view.adapter.differ.ProductManageDiffer
import com.tokopedia.product.manage.feature.list.extension.findIndex
import com.tokopedia.product.manage.feature.list.view.adapter.differ.ProductListDiffer
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

/**
 * Adapter using diff util
 */
class ProductManageListDiffutilAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactoryImpl,
    deviceId: String
) : BaseProductManageAdapter<Visitable<*>, ProductManageAdapterFactoryImpl>(
    baseListAdapterTypeFactory,
    deviceId
) {

    private val differ: ProductManageDiffer = ProductListDiffer()

    override fun showLoading() {
        if (!isLoading) {
            val items = getItems()
            if (isShowLoadingMore) {
                items.add(loadingMoreModel)
            } else {
                items.add(loadingModel)
            }
            submitList(items)
        }
    }

    override fun hideLoading() {
        if(isLoading) {
            val items = getItems()
            items.lastIndex.takeIf { it != RecyclerView.NO_POSITION }?.let { lastIndex ->
                items.removeAt(lastIndex)
                submitList(items)
            }
        }
    }

    override fun removeErrorNetwork() {
        val items = getItems()
        if (items.remove(errorNetworkModel)) {
            submitList(items)
        }
    }

    override fun addElement(itemList: MutableList<out Visitable<Any>>?) {
        val items = getItems()
        items.addAll(itemList?.toList().orEmpty())
        submitList(items)
    }

    override fun clearAllElements() {
        submitList(emptyList())
    }

    override fun updateProduct(itemList: List<Visitable<*>>) {
        submitList(itemList)
    }

    override fun removeEmptyAndUpdateLayout(itemList: List<Visitable<*>>) {
        val items = data.filter { it !is EmptyModel }.toMutableList().apply {
            addAll(itemList)
        }
        submitList(items)
    }

    override fun checkAllProducts(
        itemsChecked: MutableList<ProductUiModel>,
        onSetItemsChecked: (MutableList<ProductUiModel>) -> Unit
    ) {
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

    override fun unCheckMultipleProducts(
        productIds: List<String>?,
        itemsChecked: MutableList<ProductUiModel>,
        onSetItemsUnchecked: (MutableList<ProductUiModel>) -> Unit
    ) {
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

    override fun updateEmptyState(emptyModel: EmptyModel) {
        submitList(listOf(emptyModel))
    }

    override fun updatePrice(productId: String, price: String) {
        submitList(productId) {
            val formattedPrice = price.toDoubleOrZero().getCurrencyFormatted()
            val editedPrice = PriceUiModel(price, formattedPrice)
            it.copy(minPrice = editedPrice, maxPrice = editedPrice)
        }
    }

    override fun updatePrice(editResult: EditVariantResult) {
        submitList(editResult.productId) {
            val editedMinPrice = editResult.variants.minByOrNull { it.price }?.price.orZero()
            val editedMaxPrice = editResult.variants.maxByOrNull { it.price }?.price.orZero()

            val minPrice =
                PriceUiModel(editedMinPrice.toString(), editedMinPrice.getCurrencyFormatted())
            val maxPrice =
                PriceUiModel(editedMaxPrice.toString(), editedMaxPrice.getCurrencyFormatted())
            it.copy(minPrice = minPrice, maxPrice = maxPrice)
        }
    }

    override fun updateStock(productId: String, stock: Int?, status: ProductStatus?) {
        submitList(productId) {
            var product = it
            stock?.let { product = product.copy(stock = stock) }
            status?.let { product = product.copy(status = status) }
            product
        }
    }

    override fun deleteProduct(productId: String) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        items.findIndex(productId)?.takeIf { it > RecyclerView.NO_POSITION }?.let { index ->
            items.removeAt(index)
            submitList(items)
        }
    }

    override fun deleteProducts(productIds: List<String>) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        productIds.forEach { id ->
            items.findIndex(id)?.takeIf { it > RecyclerView.NO_POSITION }?.let { index ->
                items.removeAt(index)
            }
        }
        submitList(items)
    }

    override fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        submitList(productId) { it.copy(isFeatured = isFeaturedProduct) }
    }

    override fun setProductsStatuses(productIds: List<String>, productStatus: ProductStatus) {
        val items = data.filterIsInstance<ProductUiModel>().toMutableList()
        productIds.forEach { id ->
            items.findIndex(id)?.let { index ->
                items[index] = items[index].copy(status = productStatus)
            }
        }
        submitList(items)
    }

    override fun setMultiSelectEnabled(multiSelectEnabled: Boolean) {
        val items = data.filterIsInstance<ProductUiModel>().map {
            it.copy(multiSelectActive = multiSelectEnabled, isChecked = false)
        }
        submitList(items)
    }

    override fun filterProductList(predicate: (ProductUiModel) -> Boolean) {
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

    private fun submitList(items: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, items)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(items)
        result.dispatchUpdatesTo(this)
    }

    private fun getItems(): MutableList<Visitable<*>> {
        return visitables.toMutableList()
    }

}