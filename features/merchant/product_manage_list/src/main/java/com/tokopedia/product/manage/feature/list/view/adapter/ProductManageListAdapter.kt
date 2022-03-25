package com.tokopedia.product.manage.feature.list.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.util.ProductManageAdapterLogger
import com.tokopedia.product.manage.common.view.adapter.base.BaseProductManageAdapter
import com.tokopedia.product.manage.feature.list.view.adapter.factory.ProductManageAdapterFactoryImpl
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class ProductManageListAdapter(
    baseListAdapterTypeFactory: ProductManageAdapterFactoryImpl,
    deviceId: String
) : BaseProductManageAdapter<Visitable<*>, ProductManageAdapterFactoryImpl>(
    baseListAdapterTypeFactory,
    deviceId
) {

    override fun showLoading() {
        logUpdate(ProductManageAdapterLogger.MethodName.SHOW_LOADING)
        super.showLoading()
    }

    override fun hideLoading() {
        logUpdate(ProductManageAdapterLogger.MethodName.HIDE_LOADING)
        super.hideLoading()
    }

    override fun removeErrorNetwork() {
        logUpdate(ProductManageAdapterLogger.MethodName.REMOVE_ERROR_NETWORK)
        super.removeErrorNetwork()
    }

    override fun addElement(itemList: MutableList<out Visitable<Any>>?) {
        logUpdate(ProductManageAdapterLogger.MethodName.ADD_ELEMENT)
        super.addElement(itemList)
    }

    override fun clearAllElements() {
        logUpdate(ProductManageAdapterLogger.MethodName.CLEAR_ALL_ELEMENTS)
        super.clearAllElements()
    }

    override fun updateProduct(itemList: List<Visitable<*>>) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_PRODUCT)
        visitables.clear()
        visitables.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun removeEmptyAndUpdateLayout(itemList: List<Visitable<*>>) {
        logUpdate(ProductManageAdapterLogger.MethodName.REMOVE_AND_UPDATE_LAYOUT)
        val items =
            visitables.filter { it !is EmptyModel && it !is LoadingMoreModel }.toMutableList()
                .apply {
                    addAll(itemList)
                }
        updateProduct(items)
    }

    override fun checkAllProducts(
        itemsChecked: MutableList<ProductUiModel>,
        onSetItemsChecked: (MutableList<ProductUiModel>) -> Unit
    ) {
        logUpdate(ProductManageAdapterLogger.MethodName.CHECK_ALL_PRODUCTS)
        val items = visitables.filterIsInstance<ProductUiModel>().toMutableList()
        val newItems = items.map { product ->
            val checkedProduct = itemsChecked.firstOrNull { it.id == product.id }
            if (checkedProduct == null) {
                itemsChecked.add(product)
            }
            product.copy(isChecked = true)
        }
        updateProduct(newItems)
        onSetItemsChecked(itemsChecked)
    }

    override fun unCheckMultipleProducts(
        productIds: List<String>?,
        itemsChecked: MutableList<ProductUiModel>,
        onSetItemsUnchecked: (MutableList<ProductUiModel>) -> Unit
    ) {
        logUpdate(ProductManageAdapterLogger.MethodName.UNCHECK_MULTIPLE_PRODUCTS)
        val items = visitables.filterIsInstance<ProductUiModel>().toMutableList()
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
        updateProduct(newItems)
        onSetItemsUnchecked(itemsChecked)
    }

    override fun updateEmptyState(emptyModel: EmptyModel) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_EMPTY_STATE)
        if (visitables.getOrNull(lastIndex) !is EmptyModel) {
            visitables.add(emptyModel)
            notifyItemInserted(lastIndex + 1)
        }
    }

    override fun updatePrice(productId: String, price: String) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_PRICE)
        submitList(productId) {
            val formattedPrice = price.toIntOrZero().getCurrencyFormatted()
            val editedPrice = PriceUiModel(price, formattedPrice)
            it.copy(minPrice = editedPrice, maxPrice = editedPrice)
        }
    }

    override fun updatePrice(editResult: EditVariantResult) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_PRICE_VARIANT)
        submitList(editResult.productId) { product ->
            val editedMinPrice = editResult.variants.minByOrNull { it.price }?.price.orZero()
            val editedMaxPrice = editResult.variants.maxByOrNull { it.price }?.price.orZero()

            val minPrice =
                PriceUiModel(editedMinPrice.toString(), editedMinPrice.getCurrencyFormatted())
            val maxPrice =
                PriceUiModel(editedMaxPrice.toString(), editedMaxPrice.getCurrencyFormatted())
            product.copy(minPrice = minPrice, maxPrice = maxPrice)
        }
    }

    override fun updateStock(productId: String, stock: Int?, status: ProductStatus?) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_STOCK)
        submitList(productId) {
            var product = it
            stock?.let { product = product.copy(stock = stock) }
            status?.let { product = product.copy(status = status) }
            product
        }
    }

    override fun updateCashBack(productId: String, cashback: Int) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_CASHBACK)
        submitList(productId) { it.copy(cashBack = cashback) }
    }

    override fun deleteProduct(productId: String) {
        logUpdate(ProductManageAdapterLogger.MethodName.DELETE_PRODUCT)
        val deletedProductIndex = visitables.indexOfFirst {
            it is ProductUiModel && it.id == productId
        }
        visitables.removeAt(deletedProductIndex)
        notifyItemRemoved(deletedProductIndex)
    }

    override fun deleteProducts(productIds: List<String>) {
        logUpdate(ProductManageAdapterLogger.MethodName.DELETE_PRODUCTS)
        productIds.forEach { id ->
            visitables.indexOfFirst { it is ProductUiModel && it.id == id }.let { index ->
                visitables.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }

    override fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean) {
        logUpdate(ProductManageAdapterLogger.MethodName.UPDATE_FEATURED_PRODUCT)
        submitList(productId) { it.copy(isFeatured = isFeaturedProduct) }
    }

    override fun setProductsStatuses(productIds: List<String>, productStatus: ProductStatus) {
        logUpdate(ProductManageAdapterLogger.MethodName.SET_PRODUCTS_STATUSES)
        productIds.forEach { id ->
            submitList(id) {
                it.copy(status = productStatus)
            }
        }
    }

    override fun setMultiSelectEnabled(multiSelectEnabled: Boolean) {
        logUpdate(ProductManageAdapterLogger.MethodName.SET_MULTI_SELECT_ENABLED)
        val items = data.filterIsInstance<ProductUiModel>().map {
            it.copy(multiSelectActive = multiSelectEnabled, isChecked = false)
        }
        updateProduct(items)
    }

    override fun filterProductList(predicate: (ProductUiModel) -> Boolean) {
        logUpdate(ProductManageAdapterLogger.MethodName.FILTER_PRODUCT_LIST)
        val productList = data.filterIsInstance<ProductUiModel>().filter { predicate.invoke(it) }
        updateProduct(productList)
    }

    private fun submitList(productId: String, update: (ProductUiModel) -> ProductUiModel) {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is ProductUiModel && visitable.id == productId) {
                visitables[index] = update.invoke(visitable)
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

}