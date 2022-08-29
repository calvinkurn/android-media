package com.tokopedia.product.manage.common.view.adapter.base

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

abstract class BaseProductManageAdapter<T, F: AdapterTypeFactory>(
    baseListAdapterTypeFactory: F,
    private val deviceId: String
): BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

    abstract fun updateProduct(itemList: List<Visitable<*>>)

    abstract fun removeEmptyAndUpdateLayout(itemList: List<Visitable<*>>)

    abstract fun checkAllProducts(itemsChecked: MutableList<ProductUiModel>,
                                  onSetItemsChecked: (MutableList<ProductUiModel>) -> Unit)

    abstract fun unCheckMultipleProducts(productIds: List<String>? = null,
                                         itemsChecked: MutableList<ProductUiModel>,
                                         onSetItemsUnchecked: (MutableList<ProductUiModel>) -> Unit)

    abstract fun updateEmptyState(emptyModel: EmptyModel)

    abstract fun updatePrice(productId: String, price: String)

    abstract fun updatePrice(editResult: EditVariantResult)

    abstract fun updateStock(productId: String, stock: Int?, status: ProductStatus?)

    abstract fun deleteProduct(productId: String)

    abstract fun deleteProducts(productIds: List<String>)

    abstract fun updateFeaturedProduct(productId: String, isFeaturedProduct: Boolean)

    abstract fun setProductsStatuses(productIds: List<String>, productStatus: ProductStatus)

    abstract fun setMultiSelectEnabled(multiSelectEnabled: Boolean)

    abstract fun filterProductList(predicate: (ProductUiModel) -> Boolean)

}