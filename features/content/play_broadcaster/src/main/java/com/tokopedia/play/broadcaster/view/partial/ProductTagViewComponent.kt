package com.tokopedia.play.broadcaster.view.partial

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.ProductTagItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.adapter.PlayProductTagAdapter
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class ProductTagViewComponent(
    container: ViewGroup
) : ViewComponent(container, R.id.layout_product_tag) {

    private val rvProductTag: RecyclerView = findViewById(R.id.rv_bro_product_tag)

    private val adapter = PlayProductTagAdapter()

    init {
        rvProductTag.layoutManager = LinearLayoutManager(rvProductTag.context, RecyclerView.HORIZONTAL, false)
        rvProductTag.adapter = adapter
        rvProductTag.addItemDecoration(ProductTagItemDecoration(rvProductTag.context))
    }

    fun setProducts(products: List<ProductContentUiModel>) {
        adapter.setItemsAndAnimateChanges(products)
    }
}