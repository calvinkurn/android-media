package com.tokopedia.product.manage.feature.quickedit.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductTicker
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class ProductVariantAdapter(
    adapterFactory: BaseAdapterTypeFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterFactory) {

    companion object {
        const val PRODUCT_TICKER_POSITION = 0
    }

    fun showStockTicker() {
        addElement(PRODUCT_TICKER_POSITION, ProductTicker)
        notifyItemInserted(PRODUCT_TICKER_POSITION)
    }

    fun hideStockTicker() {
        removeElement(ProductTicker)
        notifyItemRemoved(PRODUCT_TICKER_POSITION)
    }

    fun showStockHint()  {
        getVariantList().forEach {
            it.copy(isAllStockEmpty = false)
        }
        notifyDataSetChanged()
    }

    fun hideStockHint()  {
        getVariantList().forEach {
            it.copy(isAllStockEmpty = true)
        }
        notifyDataSetChanged()
    }

    fun updateVariantStatus(id: String, status: ProductStatus) {
        updateVariant(id) { it.copy(status = status) }
    }

    fun updateVariantStock(id: String, stock: Int) {
        updateVariant(id) { it.copy(stock = stock) }
    }

    private fun getVariantList(): List<ProductVariant> {
        return data.filterIsInstance<ProductVariant>()
    }

    private fun updateVariant(
        variantId: String,
        block: (ProductVariant) -> ProductVariant
    ) {
        getVariantList().firstOrNull { it.id == variantId }?.let {
            val index = data.indexOf(it)
            data[index] = block.invoke(it)
            notifyItemChanged(index)
        }
    }
}
