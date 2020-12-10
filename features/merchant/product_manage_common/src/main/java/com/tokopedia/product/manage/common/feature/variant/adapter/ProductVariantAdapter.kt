package com.tokopedia.product.manage.common.feature.variant.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.ticker.TickerData

class ProductVariantAdapter(
    adapterFactory: BaseAdapterTypeFactory
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterFactory) {

    companion object {
        private const val PRODUCT_TICKER_POSITION = 0
    }

    fun showTicker(tickerList: List<TickerData>) {
        val ticker = data.firstOrNull { it is ProductVariantTicker }

        if(ticker == null) {
            addElement(PRODUCT_TICKER_POSITION, ProductVariantTicker(tickerList))
            notifyItemInserted(PRODUCT_TICKER_POSITION)
        } else {
            updateItem<ProductVariantTicker> { ProductVariantTicker(tickerList) }
        }
    }

    fun hideTicker() {
        removeItem<ProductVariantTicker>()
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

    private inline fun <reified T: Visitable<*>> updateItem(
        block: (Visitable<*>) -> Visitable<*>
    ) {
        data.run {
            firstOrNull { it is T }?.let {
                val index = indexOf(it)
                data[index] = block(it)
                notifyItemChanged(index)
            }
        }
    }

    private inline fun <reified T: Visitable<*>> removeItem() {
        data.run {
            firstOrNull { it is T }?.let {
                val index = indexOf(it)
                notifyItemRemoved(index)
            }
        }
    }
}
