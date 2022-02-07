package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductSummaryAdapter
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
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

    @OptIn(ExperimentalStdlibApi::class)
    fun setProductList(productList: List<ProductUiModel>) {
        /** TODO: fix this logic later */
        val finalList = buildList<ProductSummaryAdapter.Model> {
            // 12.12 Sale
            add(ProductSummaryAdapter.Model.Header("12.12 Sale", CampaignStatus.Ongoing))
            for(i in 1..3) {
                add(ProductSummaryAdapter.Model.Body(
                        ProductUiModel(
                            "$i", "Product $i", "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                            12, OriginalPrice("Rp 120.000", 120000.0)
                        )
                    )
                )
            }

            add(ProductSummaryAdapter.Model.Header("Semua Product", CampaignStatus.Unknown))
            for(i in 4..6) {
                add(ProductSummaryAdapter.Model.Body(
                        ProductUiModel(
                            "$i", "Product $i", "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                            12, DiscountedPrice("Rp 120.000", 120000.0, 20, "Rp 100.000", 100000.0)
                        )
                    )
                )
            }

            add(ProductSummaryAdapter.Model.Header("Rilisan Spesial", CampaignStatus.Ready))
            for(i in 7..9) {
                add(ProductSummaryAdapter.Model.Body(
                        ProductUiModel(
                            "$i", "Product $i", "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
                            12, DiscountedPrice("Rp 120.000", 120000.0, 20, "Rp 100.000", 100000.0)
                        )
                    )
                )
            }
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    interface Listener {
        fun onProductDeleteClicked(product: ProductUiModel)
    }
}