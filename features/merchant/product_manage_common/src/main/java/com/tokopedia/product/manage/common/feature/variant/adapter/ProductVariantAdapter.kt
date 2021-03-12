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
        private const val TICKER_POSITION = 0
    }

    fun showTicker(tickerList: List<TickerData>) {
        val currentTicker = data.firstOrNull { it is ProductVariantTicker }
        val variantTicker = ProductVariantTicker(tickerList)

        if(currentTicker == null) {
            data.add(TICKER_POSITION, variantTicker)
            notifyItemInserted(TICKER_POSITION)
        } else {
            updateItem<ProductVariantTicker> { variantTicker }
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

    fun showStockInfo() {
        if(getVariantList().firstOrNull()?.isAllStockEmpty != false) {
            getVariantList().forEach {
                val index = data.indexOf(it)
                data[index] = it.copy(isAllStockEmpty = false)
            }
            notifyDataSetChanged()
        }
    }

    fun hideStockInfo() {
        if(getVariantList().firstOrNull()?.isAllStockEmpty != true) {
            getVariantList().forEach {
                val index = data.indexOf(it)
                data[index] = it.copy(isAllStockEmpty = true)
            }
            notifyDataSetChanged()
        }
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
                data.removeAt(index)
                notifyItemRemoved(index)
            }
        }
    }
}
