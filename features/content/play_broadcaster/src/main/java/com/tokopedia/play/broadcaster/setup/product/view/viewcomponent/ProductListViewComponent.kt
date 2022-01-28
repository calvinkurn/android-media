package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.play.broadcaster.setup.product.view.adapter.ProductListAdapter
import com.tokopedia.play.broadcaster.setup.product.view.itemdecoration.ProductListItemDecoration
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
internal class ProductListViewComponent(
    view: RecyclerView,
) : ViewComponent(view) {

    private val adapter = ProductListAdapter()

    init {
        view.adapter = adapter
        view.layoutManager = StaggeredGridLayoutManager(
            2,
            RecyclerView.VERTICAL,
        )
        view.addItemDecoration(ProductListItemDecoration(view.context))
    }

    fun setProductList(productList: List<ProductUiModel>) {
        adapter.setItemsAndAnimateChanges(productList)
    }
}