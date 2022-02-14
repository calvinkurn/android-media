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
    })

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context)
    }

    fun setLoading() {
        adapter.setItemsAndAnimateChanges(List(2){ ProductSummaryAdapter.Model.Placeholder })
    }

    @OptIn(ExperimentalStdlibApi::class)
    /** TODO: gonna delete this later */
    fun setProductList(productSectionList: List<ProductTagSectionUiModel>) {
        val finalList = buildList {
            productSectionList.forEach { section ->
                add(ProductSummaryAdapter.Model.Header(section.name, section.campaignStatus))
                addAll(section.products.map { product ->
                    ProductSummaryAdapter.Model.Body(product)
                })
            }
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    interface Listener {
        fun onProductDeleteClicked(product: ProductUiModel)
    }
}