package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductSummaryAdapter
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
internal class ProductSummaryListViewComponent(
    view: RecyclerView,
    listener: Listener,
) : ViewComponent(view) {

    private val adapter = ProductSummaryAdapter(object : ProductSummaryViewHolder.Body.Listener {
        override fun onProductDeleteClicked(product: ProductUiModel) {
            listener.onProductDeleteClicked(product)
        }

        override fun onPinClicked(product: ProductUiModel) {
            listener.onPinClicked(product)
        }

        override fun onImpressPinnedProduct(product: ProductUiModel) {
            listener.onImpressPinnedProduct(product)
        }
    })

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setProductList(productSectionList: List<ProductTagSectionUiModel>, isEligibleForPin: Boolean) {
        val finalList = buildList {
            productSectionList.forEachIndexed { idx, section ->
                /** Don't display section title if its at the top && title is empty */
                if(idx != 0 || section.name.isNotEmpty()) {
                    add(ProductSummaryAdapter.Model.Header(section.name, section.campaignStatus))
                }

                addAll(section.products.map { product ->
                    ProductSummaryAdapter.Model.Body(product, isEligibleForPin)
                })
            }
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    interface Listener {
        fun onProductDeleteClicked(product: ProductUiModel)
        fun onPinClicked(product: ProductUiModel)
        fun onImpressPinnedProduct(product: ProductUiModel)
    }
}